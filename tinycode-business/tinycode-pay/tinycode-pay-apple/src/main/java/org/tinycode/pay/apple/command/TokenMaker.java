package org.tinycode.pay.apple.command;

import cn.hutool.json.JSONObject;
import org.tinycode.pay.apple.utils.AppleJwtUtils;
import org.tinycode.pay.apple.utils.GsonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 16:39
 */
@Slf4j
public class TokenMaker {

    private final static String KEY_HEADER = "-----BEGIN PRIVATE KEY-----";

    private final static String KEY_FOOTER = "-----END PRIVATE KEY-----";

    public TokenPayLoad tokenPayLoad;

    public String privateKeyStr;

    private String kid;

    public TokenMaker(String kid, TokenPayLoad tokenPayLoad, String privateKeyStr) {
        this.tokenPayLoad = tokenPayLoad;
        this.kid = kid;
        this.privateKeyStr = privateKeyStr;
    }

    public String takeJwtToken() {
        ECPrivateKey ecPrivateKey = null;
        Map<String, Object> headerMap = new HashMap<>();
        try {
            headerMap.put("alg", SignatureAlgorithm.ES256.getValue());
            headerMap.put("kid", kid);
            headerMap.put("typ", "JWT");
            ecPrivateKey = getEcPrivateKey(privateKeyStr);
        } catch (InvalidKeySpecException e) {
            log.warn("token异常.{}.InvalidKeySpecException", e);
        } catch (NoSuchAlgorithmException e) {
            log.warn("token异常.{}.NoSuchAlgorithmException", e);
        }
        Map<String, Object> payloadMap = GsonUtil.jsonToMap(GsonUtil.toJson(tokenPayLoad));
        String compact = Jwts.builder().setClaims(payloadMap).setHeaderParams(headerMap).signWith(ecPrivateKey, SignatureAlgorithm.ES256).compact();
        return compact;
    }

    public ECPrivateKey getEcPrivateKey(String privateKeyFileString) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory eclipticCurve = KeyFactory.getInstance("EC");
        String privateKeyEncodedString = privateKeyFileString
                .replaceAll(KEY_FOOTER,"")
                .replaceAll(KEY_HEADER, "")
                .replaceAll("\n","")
                .replaceAll("\r\n", "")
                .replaceAll(" ", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyEncodedString);
        KeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return (ECPrivateKey)eclipticCurve.generatePrivate(keySpec);
    }

    public static void main1(String[] args) {
        //String encoded = "eyJhbGciOiJFUzI1NiIsImtpZCI6Iko5Sk4zNFU0QkMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiI2OWE2ZGU3YS1jZDk4LTQ3ZTMtZTA1My01YjhjN2MxMWE0ZDEiLCJpYXQiOjE2Njg2ODA2NzEsImV4cCI6MTY2ODY4MTAyOCwiYXVkIjoiYXBwc3RvcmVjb25uZWN0LXYxIiwiYmlkIjoiY29tLmN5b3Uuamlhb2ppYW8uaW9zYXBwMSJ9.sNUXqrIal5QterU22mB5QzFymuOdMXbDYuOCz-LnxsA-4BFkbEWmb_b8i46bWiiBloHO1VqkMgFPrWetsAxAyg";
        String encoded = "eyJraWQiOiJjb20uY3lvdS5qaWFvamlhby5pb3NhcHAxIiwidHlwIjoiSldUIiwiYWxnIjoiRVMyNTYifQ.eyJpc3MiOiI2OWE2ZGU3YS1jZDk4LTQ3ZTMtZTA1My01YjhjN2MxMWE0ZDEiLCJpYXQiOjEuNjY4NjgxM0U5LCJleHAiOjEuNjY4Njg0OUU5LCJhdWQiOiJhcHBzdG9yZWNvbm5lY3QtdjEiLCJiaWQiOiJKOUpOMzRVNEJDIn0.M489bzW8eWu0R1F0d4K-xW6_COBgmX_45dYIkYjx_QS_hkoUWxvFfVTw0o9zoXWs7_MsWUkfKHbnQ8S62IsVKg";
        String signedPayloadDecoded = new String(Base64.getUrlDecoder().decode(encoded.split("\\.")[0]));//new String(com.org.tinycode.pay.google.api.client.util.Base64.decodeBase64(signedPayload.split("\\.")[0]));
        String payLoad = new String(Base64.getUrlDecoder().decode(encoded.split("\\.")[1]));//new String(com.org.tinycode.pay.google.api.client.util.Base64.decodeBase64(signedPayload.split("\\.")[0]));
        String sign = encoded.split("\\.")[2];//new String(com.org.tinycode.pay.google.api.client.util.Base64.decodeBase64(signedPayload.split("\\.")[0]));

        JSONObject jsonObject = GsonUtil.fromJson(signedPayloadDecoded);
        Jws<Claims> notifyPayload = AppleJwtUtils.verifyJWT(jsonObject.get("kid").toString(), encoded);
        log.info("jws result:{}", notifyPayload);
    }

    public static void main(String[] args) {
        String key = "-----BEGIN PRIVATE KEY-----\n" +
                "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgqt2pHczBuKZIhEhB\n" +
                "TTnzgq6+obZ6iuC49oYFqzPPjqKgCgYIKoZIzj0DAQehRANCAASVeXpX2lb+xzcB\n" +
                "vtGMRZWFUhN1zp/TxpoIwK881IUTF7FGQRW0+JDOVw5pg1VyArz3+vMlXpcrDZnT\n" +
                "YvsNS1St\n" +
                "-----END PRIVATE KEY-----";
        TokenMaker tokenMaker = new TokenMaker("",new TokenPayLoad("", 1L ,2L, "" ,""),"");
        try {
            tokenMaker.getEcPrivateKey(key);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
