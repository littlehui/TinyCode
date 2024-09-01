package org.tinycode.pay.apple.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/11/02 13:04
 */
@Slf4j
public class AppleJwtUtils {

    private final static String KEY_HEADER = "-----BEGIN PRIVATE KEY-----";

    private final static String KEY_FOOTER = "-----END PRIVATE KEY-----";

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

    public static void main(String[] args) {
        String key = "-----BEGIN PRIVATE KEY-----\n" +
                "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgqt2pHczBuKZIhEhB\n" +
                "TTnzgq6+obZ6iuC49oYFqzPPjqKgCgYIKoZIzj0DAQehRANCAASVeXpX2lb+xzcB\n" +
                "vtGMRZWFUhN1zp/TxpoIwK881IUTF7FGQRW0+JDOVw5pg1VyArz3+vMlXpcrDZnT\n" +
                "YvsNS1St\n" +
                "-----END PRIVATE KEY-----";
        String header = "{\n" +
                "\"alg\": \"ES256\",\n" +
                "\"kid\": \"J9JN34U4BC\",\n" +
                "\"typ\": \"JWT\"\n" +
                "}";
        Map<String, Object> headerMap = GsonUtil.fromJson(header, Map.class);
/*        String payload = "{\n" +
                "  \"iss\": \"69a6de7a-cd98-47e3-e053-5b8c7c11a4d1\",\n" +
                "  \"iat\":" + System.currentTimeMillis() / 1000 + ",\n" +
                "  \"exp\":" + (System.currentTimeMillis() / 1000 + 3600) + ",\n" +
                "  \"aud\": \"appstoreconnect-v1\",\n" +
                "  \"bid\": \"com.cyou.jiaojiao.iosapp1\"\n" +
                "}";*/

        String payload = "{\n" +
                "  \"iss\": \"69a6de7a-cd98-47e3-e053-5b8c7c11a4d1\",\n" +
                "  \"iat\": 1668680671,\n" +
                "  \"exp\": 1668684628,\n" +
                "  \"aud\": \"appstoreconnect-v1\",\n" +
                "  \"bid\": \"com.cyou.jiaojiao.iosapp1\"\n" +
                "}";
        ECPrivateKey ecPrivateKey = null;

        try {
            ecPrivateKey = getEcPrivateKey(key);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Map<String, Object> payloadMap = GsonUtil.fromJson(payload, Map.class);
        String compact = Jwts.builder().setClaims(payloadMap).setHeaderParams(headerMap).signWith(ecPrivateKey, SignatureAlgorithm.ES256).compact();
        System.out.println(compact);
    }
}
