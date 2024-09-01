package org.tinycode.pay.apple.command;

import com.google.gson.reflect.TypeToken;
import org.tinycode.pay.apple.dto.StatusResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 17:18
 */
@Slf4j
public class AppleSubscribeStatusRequest extends AbstractAppleRequest<StatusResponse> {

    private final static String SUBSCRIBE_STATUS_URL = "/inApps/v1/subscriptions/";

    public AppleSubscribeStatusRequest(String gateway, String token, String originalTransactionId, List<String> proxyUrls) {
        super(gateway + SUBSCRIBE_STATUS_URL + originalTransactionId);
        super.setAccessToken(token);
        super.setResponseTypeToken(new TypeToken<StatusResponse>(){});
        super.setProxyUrls(proxyUrls);
    }
}
