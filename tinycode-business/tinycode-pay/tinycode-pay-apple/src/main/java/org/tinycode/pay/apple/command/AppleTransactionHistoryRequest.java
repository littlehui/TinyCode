package org.tinycode.pay.apple.command;

import com.google.gson.reflect.TypeToken;
import org.tinycode.pay.apple.dto.HistoryResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 19:03
 */
@Slf4j
public class AppleTransactionHistoryRequest extends AbstractAppleRequest<HistoryResponse> {

    private final static String TRANSACTION_HISTORY_URL = "/inApps/v1/history/";

    public AppleTransactionHistoryRequest(String url
            , String accessToken
            , String originalTransactionId
            , Map<String, Object> urlParams
            , List<String> proxyUrls) {
        super(url + TRANSACTION_HISTORY_URL + originalTransactionId);
        super.setAccessToken(accessToken);
        super.setUrlParams(urlParams);
        super.setResponseTypeToken(new TypeToken<HistoryResponse>(){});
        super.setProxyUrls(proxyUrls);
    }
}
