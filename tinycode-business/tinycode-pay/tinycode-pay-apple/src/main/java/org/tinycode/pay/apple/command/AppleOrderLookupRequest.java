package org.tinycode.pay.apple.command;

import com.google.gson.reflect.TypeToken;
import org.tinycode.pay.apple.dto.OrderLookupResponse;

import java.util.List;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2023/8/8 13:57
 */
public class AppleOrderLookupRequest extends AbstractAppleRequest<OrderLookupResponse>{

    private final static String REFUND_HISTORY_URL = "/inApps/v1/lookup/";

    public AppleOrderLookupRequest(String url) {
        super(url);
    }

    public AppleOrderLookupRequest(String gatewayUrl
            , String accessToken
            , String orderId
            , TypeToken<OrderLookupResponse> responseTypeToken
            , List<String> proxyUrls) {
        super(gatewayUrl + REFUND_HISTORY_URL + orderId);
        setAccessToken(accessToken);
        setResponseTypeToken(responseTypeToken);
        setProxyUrls(proxyUrls);
    }
}
