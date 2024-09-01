package org.tinycode.pay.apple.command;

import com.google.gson.reflect.TypeToken;
import org.tinycode.pay.apple.dto.RefundHistoryResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 19:01
 */
@Slf4j
public class AppleRefundHistoryRequest extends AbstractAppleRequest<RefundHistoryResponse> {

    private final static String REFUND_HISTORY_URL = "/inApps/v2/refund/lookup/";

    public AppleRefundHistoryRequest(String requestUrl
            , String accessToken
            ,String originalTransactionId
            , Map<String
            , Object> params
            , List<String> proxyUrls) {
        super(requestUrl + REFUND_HISTORY_URL + originalTransactionId);
        super.setAccessToken(accessToken);
        super.setUrlParams(params);
        super.setResponseTypeToken(new TypeToken<RefundHistoryResponse>(){});
        super.setProxyUrls(proxyUrls);
    }
}
