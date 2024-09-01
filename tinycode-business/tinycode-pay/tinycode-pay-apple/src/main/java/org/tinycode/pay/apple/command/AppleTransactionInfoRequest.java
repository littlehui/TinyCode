package org.tinycode.pay.apple.command;

import com.google.gson.reflect.TypeToken;
import org.tinycode.pay.apple.dto.TransactionInfoResponse;

import java.util.List;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2023/8/8 13:57
 */
public class AppleTransactionInfoRequest extends AbstractAppleRequest<TransactionInfoResponse>{

    private final static String REFUND_HISTORY_URL = "/inApps/v1/transactions/";

    public AppleTransactionInfoRequest(String url) {
        super(url);
    }

    public AppleTransactionInfoRequest(String gatewayUrl
            , String accessToken
            , String transactionId
            , TypeToken<TransactionInfoResponse> responseTypeToken
            , List<String> proxyUrls) {
        super(gatewayUrl + REFUND_HISTORY_URL + transactionId);
        setAccessToken(accessToken);
        setResponseTypeToken(responseTypeToken);
        setProxyUrls(proxyUrls);
    }
}
