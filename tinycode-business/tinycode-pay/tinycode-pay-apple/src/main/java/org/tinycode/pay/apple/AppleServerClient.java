package org.tinycode.pay.apple;

import org.tinycode.pay.apple.command.*;
import org.tinycode.pay.apple.dto.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 17:27
 */
public class AppleServerClient {

    private static final String ENV_SANDBOX = "Sandbox";

    private static final String ENV_PRODUCT = "Production";

    private static volatile AppleServerClient serverClientSandbox;

    private static volatile AppleServerClient serverClientProduction;

    private Command<String, StatusResponse> appleSubscribeStatusCommand;

    private Command<TransactionQueryParams, HistoryResponse> appleTransactionHistoryCommand;

    private Command<RefundQueryParams, RefundHistoryResponse> appleRefundHistoryCommand;

    private Command<String, TransactionInfoResponse> transactionInfoResponseCommand;

    private Command<String, OrderLookupResponse> orderLookupResponseCommand;

    private static Map<String, String> GATEWAY_URL_MAP = new HashMap<>();

    static {
        GATEWAY_URL_MAP.put(ENV_SANDBOX, "https://api.storekit-sandbox.itunes.apple.com");
        GATEWAY_URL_MAP.put(ENV_PRODUCT, "https://api.storekit.itunes.apple.com");
    }

    public static AppleServerClient getSandboxInstance(List<String> proxyUrls) {
        if (serverClientSandbox == null) {
            synchronized (AppleServerClient.class) {
                if (serverClientSandbox == null) {
                    serverClientSandbox = new AppleServerClient(ENV_SANDBOX, proxyUrls);
                }
            }
        }
        return serverClientSandbox;
    }

    public static AppleServerClient getProductionInstance(List<String> proxyUrls) {
        if (serverClientProduction == null) {
            synchronized (AppleServerClient.class) {
                if (serverClientProduction == null) {
                    serverClientProduction = new AppleServerClient(ENV_PRODUCT, proxyUrls);
                }
            }
        }
        return serverClientProduction;
    }

    public static AppleServerClient getInstance(String env, List<String> proxyUrls) {
        if (ENV_PRODUCT.equals(env)) {
            return getProductionInstance(proxyUrls);
        }
        if (ENV_SANDBOX.equals(env)) {
            return getSandboxInstance(proxyUrls);
        }
        return getSandboxInstance(proxyUrls);
    }

    public AppleServerClient(String environment, List<String> proxyUrls) {
        String url = "";
        url = GATEWAY_URL_MAP.get(environment);
        AppleCommandExecutor appleCommandExecutor = new AppleCommandExecutor(url, proxyUrls);
        appleSubscribeStatusCommand = new AppleSubscribeStatusCommand(appleCommandExecutor);
        appleTransactionHistoryCommand = new AppleTransactionHistoryCommand(appleCommandExecutor);
        appleRefundHistoryCommand = new AppleRefundHistoryCommand(appleCommandExecutor);
        transactionInfoResponseCommand = new AppleTransactionCommand(appleCommandExecutor);
        orderLookupResponseCommand = new AppleOrderLookupCommand(appleCommandExecutor);
    }

    /**
     * 获取订阅历史
     * @description subscribeStatus
     * @param originalTransactionId
     * @param key
     * @param bundleId
     * @param kid
     * @param iss
     * @author littlehui
     * @date 2022/11/15 18:02
     * @return org.tinycode.pay.apple.dto.StatusResponse
     */
    public StatusResponse subscribeStatus(String originalTransactionId, String key, String bundleId, String kid, String iss) {
        return appleSubscribeStatusCommand.execute(originalTransactionId, makeToken(key, bundleId, kid, iss));
    }

    /**
     * 退款历史
     * @description refundHistory
     * @param refundQueryParams
     * @param key
     * @param bundleId
     * @param kid
     * @param iss
     * @author littlehui
     * @date 2022/11/17 15:22
     * @return org.tinycode.pay.apple.dto.RefundHistoryResponse
     */
    public RefundHistoryResponse refundHistory(RefundQueryParams refundQueryParams, String key, String bundleId, String kid, String iss) {
        return appleRefundHistoryCommand.execute(refundQueryParams, makeToken(key, bundleId, kid, iss));
    }

    /**
     * 购买历史
     * @description transactionHistory
     * @param queryParams
     * @param key
     * @param bundleId
     * @param kid
     * @param iss
     * @author littlehui
     * @date 2022/11/17 15:23
     * @return org.tinycode.pay.apple.dto.HistoryResponse
     */
    public HistoryResponse transactionHistory(TransactionQueryParams queryParams, String key, String bundleId, String kid, String iss) {
        return appleTransactionHistoryCommand.execute(queryParams, makeToken(key, bundleId, kid, iss));
    }

    /**
     * 获取购买详情
     * @description transactionInfo
     * @param transactionId
     * @param key
     * @param bundleId
     * @param kid
     * @param iss
     * @author littlehui
     * @date 2023/8/8 15:38
     * @return org.tinycode.pay.apple.dto.TransactionInfoResponse
     */
    public TransactionInfoResponse transactionInfo(String transactionId, String key, String bundleId, String kid, String iss) {
        return transactionInfoResponseCommand.execute(transactionId, makeToken(key, bundleId, kid,iss));
    }

    /**
     * 苹果客户端ID查询票据信息
     * @description orderLookup
     * @param orderId
     * @param key
     * @param bundleId
     * @param kid
     * @param iss
     * @author littlehui
     * @date 2023/11/23 13:39
     * @return org.tinycode.pay.apple.dto.OrderLookupResponse
     */
    public OrderLookupResponse orderLookup(String orderId, String key, String bundleId, String kid, String iss) {
        return orderLookupResponseCommand.execute(orderId, makeToken(key, bundleId, kid, iss));
    }

    private String makeToken(String key, String bundleId, String kid, String iss) {
        Long currentSeconds = System.currentTimeMillis()/1000;
        Long expireSeconds = currentSeconds + 10 * 60;
        TokenPayLoad tokenPayLoad = TokenPayLoad.builder()
                .aud("appstoreconnect-v1")
                .bid(bundleId)
                .exp(expireSeconds)
                .iat(currentSeconds)
                .iss(iss)
                .build();
        TokenMaker tokenMaker = new TokenMaker(kid, tokenPayLoad, key);
        return tokenMaker.takeJwtToken();
    }
}
