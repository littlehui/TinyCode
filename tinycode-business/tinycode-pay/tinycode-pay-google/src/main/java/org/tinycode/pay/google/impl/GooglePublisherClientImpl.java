package org.tinycode.pay.google.impl;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.*;
import org.tinycode.pay.google.GooglePublisherClient;
import org.tinycode.pay.google.exception.CallbackException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/12/15 10:02
 */
@Slf4j
public class GooglePublisherClientImpl implements GooglePublisherClient {

    private static HttpTransport HTTP_TRANSPORT;

    private static Map<String, GoogleCredential> credentialMap = new ConcurrentHashMap<>();

    private String proxyUrl;

    private String proxyPort;

    private static final Integer HTTP_CONNECTION_TIMEOUT = 5 * 1000;

    private static final Integer HTTP_READ_TIMEOUT = 5 * 1000;

    private static final Long FIVE_MINUTES_MILLS = 5 * 60 * 1000L;

    /**
     * 延迟10分钟确认
     */
    private static final Long DELAY_TIME_MILLS = 10 * 60 * 1000L;

    private ThreadPoolExecutor acknowledgeExecutor = new ThreadPoolExecutor(2
            , 10
            ,20
            , TimeUnit.SECONDS
            , new ArrayBlockingQueue(100)
            , new ThreadPoolExecutor.CallerRunsPolicy());

    private ThreadPoolExecutor acknowledgeDelayedExecutor = new ThreadPoolExecutor(2
            , 10
            , 20
            , TimeUnit.SECONDS
            , new DelayQueue()
            , new ThreadPoolExecutor.CallerRunsPolicy());

    private abstract class AcknowledgeDelayed implements Runnable, Delayed {

        /* 触发时间*/
        private long time;

        public AcknowledgeDelayed(long time, TimeUnit unit) {
            this.time = System.currentTimeMillis() + (time > 0 ? unit.toMillis(time) : 0);
        }

        @Override
        public int compareTo(Delayed o) {
            AcknowledgeDelayed item = (AcknowledgeDelayed) o;
            long diff = this.time - item.time;
            if (diff <= 0) {
                //改成>=会造成问题
                return -1;
            } else {
                return 1;
            }
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return time - System.currentTimeMillis();
        }
    }

    private HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                requestInitializer.initialize(httpRequest);
                httpRequest.setConnectTimeout(HTTP_CONNECTION_TIMEOUT);
                httpRequest.setReadTimeout(HTTP_READ_TIMEOUT);
            }
        };
    }

    private GoogleCredential getCachedCredential(String keyJson) {
        final String key = keyJson;
        GoogleCredential credential = credentialMap.get(key);
        return credential;
    }

    /**
     * 客户端链接初始化
     * @description init
     * @param keyJson
     * @author littlehui
     * @date 2022/12/15 10:03
     * @return AndroidPublisher
     */
    public AndroidPublisher init(String keyJson, String packageName) throws IOException, GeneralSecurityException {
        Long currentTimeMills = System.currentTimeMillis();
        GoogleCredential currentCredential = null;
        //创建信任传输
        if (HTTP_TRANSPORT == null) {
            if (proxyUrl != null && proxyPort != null) {
                HTTP_TRANSPORT = new NetHttpTransport.Builder()
                        .trustCertificates(GoogleUtils.getCertificateTrustStore())
                        .setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, Integer.parseInt(proxyPort))))
                        .build();
            } else {
                HTTP_TRANSPORT = new NetHttpTransport.Builder()
                        .trustCertificates(GoogleUtils.getCertificateTrustStore())
                        .build();
            }
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(keyJson));
        currentCredential = getCachedCredential(keyJson);
        if (currentCredential == null) {
            synchronized (this) {
                if (currentCredential == null) {
                    currentCredential = GoogleCredential.fromStream(inputStream, HTTP_TRANSPORT, Utils.getDefaultJsonFactory())
                            .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));
                    currentCredential.refreshToken();
                }
            }
            if (currentCredential != null) {
                credentialMap.put(keyJson, currentCredential);
            }
        } else {
            if (currentCredential.getAccessToken() != null && !"".equals(currentCredential.getAccessToken())) {
                //已经有accessToken
                Long expirationTimeMilliseconds = currentCredential.getExpirationTimeMilliseconds();
                if ((expirationTimeMilliseconds - currentTimeMills) < FIVE_MINUTES_MILLS) {
                    //还有5分钟过期，必须刷新一次
                    currentCredential.refreshToken();
                }
            } else {
                currentCredential.refreshToken();
            }
        }
        AndroidPublisher androidPublisher = new AndroidPublisher
                .Builder(HTTP_TRANSPORT
                , JacksonFactory.getDefaultInstance()
                , setHttpTimeout(currentCredential))
                .setApplicationName(packageName)
                //.setApplicationName("com.u17173.og173.sample")
                .build();
        return androidPublisher;
    }

    @Override
    public void setProxy(String proxyUrl, String proxyPort) {
        this.proxyPort = proxyPort;
        this.proxyUrl = proxyUrl;
    }

    @Override
    public ProductPurchase productPurchase(String keyJson, String packageName, String purchaseToken, String productId) {
        try {
            AndroidPublisher androidPublisher = this.init(keyJson, packageName);
            AndroidPublisher.Purchases.Products.Get productGet= androidPublisher.purchases().products().get(packageName, productId, purchaseToken);
            return productGet.execute();
        } catch (IOException e) {
            log.error("初始化androidPublisher或者google服务器访问异常：{}", e);
        } catch (GeneralSecurityException e) {
            log.error("初始化androidPublisher或者google服务器访问异常：{}", e);
        }
        return null;
    }

    @Override
    public List<VoidedPurchase> voidedPurchases(String keyJson, String packageName, Long startTimeMills, Long endTimeMills) {
        List<VoidedPurchase> voidedPurchases = new ArrayList<>();
        try {
            AndroidPublisher androidPublisher = this.init(keyJson, packageName);
            AndroidPublisher.Purchases.Voidedpurchases.List voidPurchaseList = androidPublisher.purchases().voidedpurchases().list(packageName);
            voidPurchaseList.setStartTime(startTimeMills);
            voidPurchaseList.setEndTime(endTimeMills);
            voidPurchaseList.setMaxResults(20L);
            VoidedPurchasesListResponse voidedPurchasesListResponse = voidPurchaseList.execute();
            if (voidedPurchasesListResponse.getVoidedPurchases() != null && voidedPurchasesListResponse.getVoidedPurchases().size() > 0) {
                voidedPurchases.addAll(voidedPurchasesListResponse.getVoidedPurchases());
                TokenPagination tokenPagination = voidedPurchasesListResponse.getTokenPagination();
                while (tokenPagination != null && tokenPagination.getNextPageToken() != null) {
                    voidPurchaseList.setToken(tokenPagination.getNextPageToken());
                    voidedPurchasesListResponse = voidPurchaseList.execute();
                    if (voidedPurchasesListResponse.getVoidedPurchases() != null && voidedPurchasesListResponse.getVoidedPurchases().size() > 0) {
                        voidedPurchases.addAll(voidedPurchasesListResponse.getVoidedPurchases());
                    }
                    tokenPagination = voidedPurchasesListResponse.getTokenPagination();
                }
            }
            if (voidedPurchases != null) {
                log.info("总共获取到退款单量：{}", voidedPurchases.size());
            }
            return voidedPurchases;
        } catch (IOException e) {
            log.error("访问google服务器失败：", e);
            log.info("访问google服务器发生异常，，总共获取到退款数量：{}", voidedPurchases.size());
        } catch (GeneralSecurityException e) {
            log.error("访问google服务器失败：", e);
            log.info("访问google服务器发生异常，总共获取到退款数量：{}", voidedPurchases.size());
        }
        return voidedPurchases;
    }

    @Override
    public Future<Boolean> productAcknowledge(String keyJson, String packageName, String productId, String purchaseToken) throws CallbackException {
        try {
            AndroidPublisher androidPublisher = this.init(keyJson, packageName);
            Future<Boolean> future = acknowledgeExecutor.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    try {
                        ProductPurchasesAcknowledgeRequest acknowledgeRequest = new ProductPurchasesAcknowledgeRequest();
                        AndroidPublisher.Purchases.Products.Acknowledge acknowledge = androidPublisher.purchases().products().acknowledge(packageName, productId, purchaseToken, acknowledgeRequest);
                        acknowledge.execute();
                        return true;
                    } catch (Exception e) {
                        log.error("google确认订单失败", e);
                        return false;
                    }
                }
            });
            return future;
        } catch (Exception e) {
            log.error("google确认订单失败：{}", e);
            throw new CallbackException(e);
        }
    }

    @Override
    public void productDelayAcknowledge(String keyJson, String packageName, String productId, String purchaseToken) {
        try {
            AndroidPublisher androidPublisher = this.init(keyJson, packageName);
            acknowledgeDelayedExecutor.submit(new AcknowledgeDelayed(DELAY_TIME_MILLS, TimeUnit.MILLISECONDS) {
                @Override
                public void run() {
                    try {
                        ProductPurchasesAcknowledgeRequest acknowledgeRequest = new ProductPurchasesAcknowledgeRequest();
                        AndroidPublisher.Purchases.Products.Acknowledge acknowledge = androidPublisher.purchases().products().acknowledge(packageName, productId, purchaseToken, acknowledgeRequest);
                        acknowledge.execute();
                    } catch (Exception e) {
                        log.error("google确认订单失败", e);
                    }
                }
            });
        } catch (IOException e) {
            log.error("androidPublisher 初始化异常", e);
        } catch (GeneralSecurityException e) {
            log.error("androidPublisher 初始化异常", e);
        }
    }

    @Override
    public String takeToken(String serviceAccountBase64) {
        GoogleCredential googleCredential = credentialMap.get(serviceAccountBase64);
        if (googleCredential != null) {
            return googleCredential.getAccessToken();
        } else {
            return "";
        }
    }
}
