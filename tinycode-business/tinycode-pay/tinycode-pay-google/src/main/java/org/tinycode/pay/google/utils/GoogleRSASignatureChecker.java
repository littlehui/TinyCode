package org.tinycode.pay.google.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by littlehui on 2020/5/20.
 */
public class GoogleRSASignatureChecker {

    /**
     * 解密
     *
     * @param content 密文
     * @param key     商户私钥
     * @return 解密后的字符串
     */
    public static String decrypt(String content, String key) throws Exception {
        PrivateKey prikey = getPrivateKey(key);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);
        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), "utf-8");
    }

    /**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @return 签名值
     */
    public static String sign(String content, String privateKey) {
        String charset = "utf-8";
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 公钥
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;

        } catch (Exception e) {
            //e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
/*        String content = "{\"orderId\":\"GPA.3396-1451-9236-98802\",\"packageName\":\"com.u17173.og173.sample\",\"productId\":\"2\",\"purchaseTime\":1590040620691,\"purchaseState\":0,\"purchaseToken\":\"omicodhjknjpbilmeihjkofp.AO-J1OxWIPzx_C7K3I5cfwtqIgoBSQcq_gGHyHgx4QtI1fTByN6chUTS3bpTR1umS_RWBGQK2_DBS7e_mQrnekVnN60eoLRQuVCfZNc0Gjoud2rKohC8AcI\",\"obfuscatedAccountId\":\"1590040478768\",\"obfuscatedProfileId\":\"1590040478768\",\"acknowledged\":false}";
        String sign = "Eus3h5186dhqCyuXe4bhTutoQkuNcrjvJtgT1U3YWEQbTdxa2Jl2bR24zBSPpujN/Pj8oyAJtJnXfIflvdp08E3SNNLa846ax8Ya3VtZTibunxmkmjqfgO2aXkpevH2Q9M/jFImcCCoUURivfUsMI+Bl/Arf2lLvQ7vVNsCDlRwMcMkKKUJ7B+yvhwpxXILAI1LP4rm0p3ALbLql6DjGJITPuZyhzNK0TJIB27/2yeR4MRApuA2PyFay4g2+aDdg2jNzfuBbH2S6eYmX9eu84nrnAI77Nw9/De66uaDRMFAYG/wlW/zb/jW3Sj1+9mUochpn9PdUJI0OMplgDXcfYg==";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhOwN6Uyl13cmG+NhMv516ZKlTDmBwemUsnww8BxiY0znhJtHt4+LlV5ugle4QYtEwWQZtr3ym9d0h+I3F+4pT2rapnFGkRtrZ3knEWyKYXVqOhpoMhawZj/zfGeA5kyIDCPv+LiCoeD42ToKMayV53YWeYFZMpey7AkX0E3ztGUtYjEeiipqmWoq5Y8It29yZfQgB+5ZP05G3qBF1PXA8C7BmLsHeUoNCOw6hiejQ8WaOhCdGCUM+x9BN9SGh3VXdMJIX19SoQiMT/Q0il7ob1EDr9st/yU97TOsbpC1KvCdnSCa/0QMhboYCYLANGdcbpmCDYPi/eGUSr5FQWF/mwIDAQAB";
        System.out.println(doCheck(content,sign,publicKey));*/

        String content = null;
        try {
            content = new String((java.util.Base64.getDecoder().decode("eyJvcmRlc" +
                    "klkIjoiR1BBLjMzNjMtNTIzOC01NTk5LTU4OTM0IiwicGFja2F" +
                    "nZU5hbWUiOiJjb20uZzJwLmhjeG0iLCJwcm9kdWN0SWQiOiIyI" +
                    "iwicHVyY2hhc2VUaW1lIjoxNTk3OTE1NjYxNzAyLCJwdXJjaGF" +
                    "zZVN0YXRlIjowLCJwdXJjaGFzZVRva2VuIjoibWpiY2xuaWhna" +
                    "29rbWRiY2JvaW9obG9wLkFPLUoxT3dwSUEtbFAydzFCc21Db04" +
                    "4d1BYVmZjYTFJUTZtS0xKcndLcXJ3cW45WHVsOWtreDdGS3Zre" +
                    "Xh1STV0RzVMVXlfcTg4X2p4cWJQTE5HSlVtT0J3QlJFZDRDWER" +
                    "nIiwib2JmdXNjYXRlZEFjY291bnRJZCI6InVuc2V0Iiwib2Jmd" +
                    "XNjYXRlZFByb2ZpbGVJZCI6IjIwMDUyMDIwMDgyMDA5MjczNjA" +
                    "wMDc2XzIwMjAwODIwMzUwOTA5Nzc2NjcxNzQ0MDEiLCJhY2tub" +
                    "3dsZWRnZWQiOmZhbHNlfQ==")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(content);
        //String content = "{\"orderId\":\"GPA.3396-1451-9236-98802\",\"packageName\":\"com.u17173.og173.sample\",\"productId\":\"2\",\"purchaseTime\":1590040620691,\"purchaseState\":0,\"purchaseToken\":\"omicodhjknjpbilmeihjkofp.AO-J1OxWIPzx_C7K3I5cfwtqIgoBSQcq_gGHyHgx4QtI1fTByN6chUTS3bpTR1umS_RWBGQK2_DBS7e_mQrnekVnN60eoLRQuVCfZNc0Gjoud2rKohC8AcI\",\"obfuscatedAccountId\":\"1590040478768\",\"obfuscatedProfileId\":\"1590040478768\",\"acknowledged\":false}";
        String sign = "K/TVjLPIIbstY5PTyCmA0qeDUycZqMN3ijHMuWymxolylWyehqD4a76QkgVYoidf4QLSalxVm6Qw0szdvEwTBwj232JiiMwl7viUpv/AuI5jOCexuk7OXr7eHdpNKtI8V28PlKunM3iLpiN5XkeIRDaeF7LalJo+QVPHknyEYfvIf2VmioHt4EQmgOPhpr9ajhtLRvic7GbndtlBev5FI93UQUxwr2X2wAtQPaS3EA64448Nf0MQn0RWO3XvqgGGGdzS2wZiySwRwCAAcRvNJ+XRlo+hfT8cqEGCcR4yZdO3UIi2JdQIoPrTkkpOm8BIxWTdajfrPlQ3Cc7FyqmWGw==";
        //String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhOwN6Uyl13cmG+NhMv516ZKlTDmBwemUsnww8BxiY0znhJtHt4+LlV5ugle4QYtEwWQZtr3ym9d0h+I3F+4pT2rapnFGkRtrZ3knEWyKYXVqOhpoMhawZj/zfGeA5kyIDCPv+LiCoeD42ToKMayV53YWeYFZMpey7AkX0E3ztGUtYjEeiipqmWoq5Y8It29yZfQgB+5ZP05G3qBF1PXA8C7BmLsHeUoNCOw6hiejQ8WaOhCdGCUM+x9BN9SGh3VXdMJIX19SoQiMT/Q0il7ob1EDr9st/yU97TOsbpC1KvCdnSCa/0QMhboYCYLANGdcbpmCDYPi/eGUSr5FQWF/mwIDAQAB";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAllVgS0eZPQWSji0XhCEXGHuP4GUQjSAxJha6/EDgjMK/JZw8/QpPPDoIIfmJLevEpoX5VYQaFHXNcvQJZm6x7FiQrAgXXx2ndMk5xrpCvspkGgySinGH9NseYdot67SF4ot/c62RI02y9VHo8ViqLi0RKeuBgP/+YmDdjgaOz110RP9mpzWek3I6lH0pZGdTqZgxT9AtavaqJQxmQIvEYTZU/dpDX/5IDozhxxbigbi2MjnbYRDPYRDtsQXnulOTXy5dsCPCiR/BwKwpzw6dUYDOXV/XL/jotmE/TYjrJDy0Z/IDCER1NKwGUJHmRKXZ0OrL4QXpRFCwUBiFXY5gGQIDAQAB";

        System.out.println(doCheck(content,sign,publicKey));
    }

}
