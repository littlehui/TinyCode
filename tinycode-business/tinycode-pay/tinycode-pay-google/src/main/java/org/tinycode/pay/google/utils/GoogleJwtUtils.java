package org.tinycode.pay.google.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.google.api.services.androidpublisher.model.VoidedPurchasesListResponse;
import com.google.auth.oauth2.ServiceAccountCredentials;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/11/02 13:04
 */
@Slf4j
public class GoogleJwtUtils {

    private final static String KEY_HEADER = "-----BEGIN PRIVATE KEY-----";

    private final static String KEY_FOOTER = "-----END PRIVATE KEY-----";

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            boolean bverify = signature.verify(com.sun.org.apache.xerces.internal.impl.dv.util.Base64.decode(sign));
            return bverify;

        } catch (Exception e) {
            //e.printStackTrace();
        }
        return false;
    }

    public static Jws<Claims> verifyJWT(String x5c, String jws){
        try {
            X509Certificate cert = getCert(x5c);
            if (!cert.getSubjectDN().getName().contains("Apple Inc")){
                log.info("not apple cert . name = {}", cert.getIssuerX500Principal().getName());
                return null;
            }
            return Jwts.parserBuilder().setSigningKey(cert.getPublicKey()).build().parseClaimsJws(jws);
        }catch (JwtException exc){
            log.info("jws verify failure.", exc);
            return null;
        } catch (Exception exc){
            log.info("jws verify error.", exc);
            return null;
        }
    }

    public static X509Certificate getCert(String x5c) throws CertificateException {
        String stripped = x5c.replaceAll("-----BEGIN (.*)-----", "");
        stripped = stripped.replaceAll("-----END (.*)----", "");
        stripped = stripped.replaceAll("\r\n", "");
        stripped = stripped.replaceAll("\n", "");
        stripped.trim();
        byte[] keyBytes = Base64.getDecoder().decode(stripped);
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        return (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(keyBytes));
    }

    public static ECPrivateKey getEcPrivateKey(String privateKeyFileString) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory eclipticCurve = KeyFactory.getInstance("EC");
        String privateKeyEncodedString = privateKeyFileString
                .replaceAll(KEY_FOOTER,"")
                .replaceAll(KEY_HEADER, "")
                .replaceAll("\n","")
                .replaceAll("\r\n", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyEncodedString);
        KeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return (ECPrivateKey)eclipticCurve.generatePrivate(keySpec);
    }

    public static PrivateKey getRsaPrivateKeySpec(String privateKeyFileString) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory eclipticCurve = KeyFactory.getInstance("EC");
        String privateKeyEncodedString = privateKeyFileString
                .replaceAll(KEY_FOOTER,"")
                .replaceAll(KEY_HEADER, "")
                .replaceAll("\n","")
                .replaceAll("\r\n", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyEncodedString);
        KeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        Keys.secretKeyFor(SignatureAlgorithm.RS256);
        return (PrivateKey)eclipticCurve.generatePrivate(keySpec);
    }

    public static void main1(String[] args) throws IOException {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "RS256");
        headerMap.put("typ", "JWT");

        Long currentSeconds = System.currentTimeMillis()/1000;

        Map<String, Object> payloadMap = new HashMap<>();
        payloadMap.put("iss", "epay-service@api-5831105460633546838-299949.iam.gserviceaccount.com");
        payloadMap.put("scope", "https://www.googleapis.com/auth/androidpublisher");
        payloadMap.put("aud", "https://oauth2.googleapis.com/token");
        payloadMap.put("iat", currentSeconds);
        payloadMap.put("exp", currentSeconds + 30 * 60);

        String fileContent = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"api-5831105460633546838-299949\",\n" +
                "  \"private_key_id\": \"44f2114af2c781188aebd172e1a779825ed4fb2c\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1TCYnynhKIIQ4\\nJOAOaECiSBUO3i3ad08J9BW74K+KSwjVa3GAFTonP6hMTaXhleACXcXFSiyo+Jhn\\nVLn4tk3R0AdUGqXJ0XEbSMH0OfAp31ihBjjX7mkp0hc9njjFqu+JNypW8NmoXkV/\\nvVKaBtycymxGBLYeYPmcDY0WxnjhRKzdfhF1fBNgGbCkwFhAb1mUxAfPPgbfOMuO\\nxH/5Wv4r+6ZxoD1z8FoDfBRub/riBgmJXYhSpk7wFrmYpjJX1E4IwpcPGDbAAsLd\\nQbHd69llw2tWovJsFZs/1di7lGUxHwW85gXJYiRh+sg1T0T0EEARbqV8EJSv4kvU\\n6eBJRQxlAgMBAAECggEAF8KyUM5kg9Pi0ELmSdfC+iHNVLoCCMWl37g+2EqNvGey\\nHC/ChoFXB57RrVJoi6SrYP6uNU76hG4Jof1E3wJG4lkhJMDV+pnO+OmeYHuZbLt0\\nr6yTlZqNlilj2gkFssOrihsR8FFiOAKcRC4UCH4eur59gJMMf/XAsQGbp0uoEF8e\\nv6eom/KhiYQG0PQnY0xVevw9LNnJfj1WZpVNZX0c/C01syJ9V2gcItyBU0Hq09tN\\n8JyZ8FZWu6eeIZsxLZPQk0Wp8uiSTxpWMYegAubz9DOXgGuAO+cygVU/3QVwpw5H\\nqqRtBpiR9OtccCSXO0LOL8TTPuTMiD60o7nS3s7dDwKBgQDyhIw4z7vccsEQnMN0\\nDeGVgbWicdGitnkZv96Znr5CjB/Ut4Lg9BvnXepfydcK1JEB90yIejHwfydF2q8V\\n2HGFRtIVTyea4o3Yos7C+EoGU8yEu51RXe1+Kopsec/4zK/fyiI2dEja6v1WkBXk\\nym6DXUh5FQERG8xDiv0SzmDN4wKBgQC/YFRE9ei+rK6v41arMmXsWfDcAYfhjYyi\\napGDOAc6FpsZ9YUz3kiIMlV6pkd1UUEsyiDb7q5Jr+hmUXNZkyvwQGZeeTEx2nyc\\nV4i6tm6ZVplYycGR311q5zGQ8FN3nnYml2el2midyI6J8j/RN6Tc/X+zxTLFQKAA\\ny3vBIwjPFwKBgQCzAqHaYYQCofXehEhFhuWHjPn8kVUzbMuQ9S6jaCh6SKJKSrbh\\nw25BZvJUiFEiHmt27wrgBoqMQd9EUbpBPsX0oaT6s7XBPw+C5BIImmtRplNlenMS\\nF2nxYgdRMM0Rkk0TvElqtoT8is47MhnnCoFul9GiBp4F+eXZyWMqmYmdDwKBgAwR\\noh7bNztXHokfGCchvYXDQkpNmmkiCR1xBQ42aVZDeLKRZj4YJZDZQ8TBWb5PSoHt\\nmVblXX5uJoTVXCXo/xtepFYO5IhVVAh0CL5lpOHssS0FRDzTzktgmyKd1VW025Sh\\ndbri5DgXHs4aReYG0VHSdsw6gnIoGi8SphJPCE4vAoGAKS2UAF6OKUjSv8M40/aI\\nz68sD+g/xD35+LhOjVE4WimmZ6nca7yt2R+HT9cyEd5hehquYJXvxxlajTKs23To\\njZTxVAP6Sx8+KWOl0BMp+Ob6RYYPsJTnJOZkwVneUSTyGaO2O52us16sX7ZapzI1\\nqUVc+HpFMJPOBCgPRLkeAI0=\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"epay-service@api-5831105460633546838-299949.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"110153878898807803663\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/epay-service%40api-5831105460633546838-299949.iam.gserviceaccount.com\"\n" +
                "}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        ServiceAccountCredentials serviceAccountCredentials = ServiceAccountCredentials.fromStream(inputStream);
        String compact = Jwts.builder().setClaims(payloadMap).setHeaderParams(headerMap).signWith(SignatureAlgorithm.HS256, serviceAccountCredentials.getPrivateKey()).compact();
        System.out.println(compact);
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        ExecutorService executorService = new ThreadPoolExecutor(2, 2, 20, TimeUnit.SECONDS, new ArrayBlockingQueue(3));
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                    String packageName = "com.u17173.og173.sample";
                    String purchaseToken = "dkmodfifncdjfcofegikfmnc.AO-J1OxYTn_ps7V21ZOVVj2MrHeiCPmyXdAF8yf5YvlPXwOAP7mpSQq2WN_l61RNJ3NXtiw36aKhD2mAf3jOwvDFUwmhfITz4D7m9GuqK-lJgitZfYgDi4c";
                    String productId = "1";
                    AndroidPublisher androidPublisher = getAndroidPublisher();
                    AndroidPublisher.Purchases.Products.Get product = androidPublisher.purchases().products().get(packageName, productId, purchaseToken);
                    ProductPurchase productResult = product.execute();
                    System.out.println(productResult);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                    AndroidPublisher androidPublisher = getAndroidPublisher();
                    String packageName = "com.u17173.og173.sample";
                    AndroidPublisher.Purchases.Voidedpurchases.List voidPurchaseList = androidPublisher.purchases().voidedpurchases().list(packageName);
                    VoidedPurchasesListResponse voidedPurchasesListResponse = voidPurchaseList.execute();
                    System.out.println(voidedPurchasesListResponse);
                } catch (IOException | InterruptedException | BrokenBarrierException | GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.shutdown();
    }

    static AndroidPublisher getAndroidPublisher() throws IOException, GeneralSecurityException {
        String fileContent = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"api-5831105460633546838-299949\",\n" +
                "  \"private_key_id\": \"44f2114af2c781188aebd172e1a779825ed4fb2c\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1TCYnynhKIIQ4\\nJOAOaECiSBUO3i3ad08J9BW74K+KSwjVa3GAFTonP6hMTaXhleACXcXFSiyo+Jhn\\nVLn4tk3R0AdUGqXJ0XEbSMH0OfAp31ihBjjX7mkp0hc9njjFqu+JNypW8NmoXkV/\\nvVKaBtycymxGBLYeYPmcDY0WxnjhRKzdfhF1fBNgGbCkwFhAb1mUxAfPPgbfOMuO\\nxH/5Wv4r+6ZxoD1z8FoDfBRub/riBgmJXYhSpk7wFrmYpjJX1E4IwpcPGDbAAsLd\\nQbHd69llw2tWovJsFZs/1di7lGUxHwW85gXJYiRh+sg1T0T0EEARbqV8EJSv4kvU\\n6eBJRQxlAgMBAAECggEAF8KyUM5kg9Pi0ELmSdfC+iHNVLoCCMWl37g+2EqNvGey\\nHC/ChoFXB57RrVJoi6SrYP6uNU76hG4Jof1E3wJG4lkhJMDV+pnO+OmeYHuZbLt0\\nr6yTlZqNlilj2gkFssOrihsR8FFiOAKcRC4UCH4eur59gJMMf/XAsQGbp0uoEF8e\\nv6eom/KhiYQG0PQnY0xVevw9LNnJfj1WZpVNZX0c/C01syJ9V2gcItyBU0Hq09tN\\n8JyZ8FZWu6eeIZsxLZPQk0Wp8uiSTxpWMYegAubz9DOXgGuAO+cygVU/3QVwpw5H\\nqqRtBpiR9OtccCSXO0LOL8TTPuTMiD60o7nS3s7dDwKBgQDyhIw4z7vccsEQnMN0\\nDeGVgbWicdGitnkZv96Znr5CjB/Ut4Lg9BvnXepfydcK1JEB90yIejHwfydF2q8V\\n2HGFRtIVTyea4o3Yos7C+EoGU8yEu51RXe1+Kopsec/4zK/fyiI2dEja6v1WkBXk\\nym6DXUh5FQERG8xDiv0SzmDN4wKBgQC/YFRE9ei+rK6v41arMmXsWfDcAYfhjYyi\\napGDOAc6FpsZ9YUz3kiIMlV6pkd1UUEsyiDb7q5Jr+hmUXNZkyvwQGZeeTEx2nyc\\nV4i6tm6ZVplYycGR311q5zGQ8FN3nnYml2el2midyI6J8j/RN6Tc/X+zxTLFQKAA\\ny3vBIwjPFwKBgQCzAqHaYYQCofXehEhFhuWHjPn8kVUzbMuQ9S6jaCh6SKJKSrbh\\nw25BZvJUiFEiHmt27wrgBoqMQd9EUbpBPsX0oaT6s7XBPw+C5BIImmtRplNlenMS\\nF2nxYgdRMM0Rkk0TvElqtoT8is47MhnnCoFul9GiBp4F+eXZyWMqmYmdDwKBgAwR\\noh7bNztXHokfGCchvYXDQkpNmmkiCR1xBQ42aVZDeLKRZj4YJZDZQ8TBWb5PSoHt\\nmVblXX5uJoTVXCXo/xtepFYO5IhVVAh0CL5lpOHssS0FRDzTzktgmyKd1VW025Sh\\ndbri5DgXHs4aReYG0VHSdsw6gnIoGi8SphJPCE4vAoGAKS2UAF6OKUjSv8M40/aI\\nz68sD+g/xD35+LhOjVE4WimmZ6nca7yt2R+HT9cyEd5hehquYJXvxxlajTKs23To\\njZTxVAP6Sx8+KWOl0BMp+Ob6RYYPsJTnJOZkwVneUSTyGaO2O52us16sX7ZapzI1\\nqUVc+HpFMJPOBCgPRLkeAI0=\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"epay-service@api-5831105460633546838-299949.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"110153878898807803663\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/epay-service%40api-5831105460633546838-299949.iam.gserviceaccount.com\"\n" +
                "}";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        GoogleCredential credential = GoogleCredential.fromStream(inputStream)
                .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));
        credential.refreshToken();
        System.out.println("accessToken:" + credential.getAccessToken());
        AndroidPublisher androidPublisher = new AndroidPublisher
                .Builder(GoogleNetHttpTransport.newTrustedTransport()
                , JacksonFactory.getDefaultInstance()
                , credential)
                .setApplicationName("epay-service")
                .build();
        return androidPublisher;
    }
}
