package org.tinycode.pay.apple.command;

import cn.hutool.json.JSONObject;
import org.tinycode.pay.apple.utils.AppleJwtUtils;
import org.tinycode.pay.apple.utils.GsonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 17:45
 */
@Slf4j
public class ResponseDecoder {

    public static <T> T takePayload(String signedPayload, Class<T> tClass) {
        String signedPayloadDecoded = new String(Base64.getUrlDecoder().decode(signedPayload.split("\\.")[0]));//new String(com.org.tinycode.pay.google.api.client.util.Base64.decodeBase64(signedPayload.split("\\.")[0]));
        JSONObject jsonObject = GsonUtil.fromJson(signedPayloadDecoded);
        Jws<Claims> notifyPayload = AppleJwtUtils.verifyJWT(jsonObject.getJSONArray("x5c").get(0).toString(), signedPayload);
        log.info("jws result:{}", notifyPayload);
        T payload = GsonUtil.fromJson(GsonUtil.toJson(notifyPayload.getBody()), tClass);
        return payload;
    }
}
