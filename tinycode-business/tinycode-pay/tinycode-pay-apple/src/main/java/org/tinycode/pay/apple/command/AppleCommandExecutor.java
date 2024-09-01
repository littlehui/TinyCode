package org.tinycode.pay.apple.command;

import com.google.gson.reflect.TypeToken;
import org.tinycode.pay.apple.dto.*;
import org.tinycode.pay.apple.exception.AppleServerRequestException;
import org.tinycode.pay.apple.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/8/5 16:22
 */
@Slf4j
public class AppleCommandExecutor {

    private String gatewayUrl;

    private List<String> proxyUrls;
    public AppleCommandExecutor(String gatewayUrl, List<String> proxyUrls) {
        this.gatewayUrl = gatewayUrl;
        this.proxyUrls = proxyUrls;
    }

    public RefundHistoryResponse getRefundHistory(RefundQueryParams refundQueryParams, String accessToken) {
        Map<String, Object> paramMap = GsonUtil.jsonToMap(GsonUtil.toJson(refundQueryParams));
        paramMap.remove("originalTransactionId");
        AppleRequest appleRequest = new AppleRefundHistoryRequest(this.gatewayUrl
                , accessToken
                , refundQueryParams.getOriginalTransactionId()
                , paramMap
                , proxyUrls);
        try {
            RefundHistoryResponse response = appleRequest.doRequest();
            return response;
        } catch (AppleServerRequestException e) {
            log.error("apple server request error:{}", e);
        }
        return null;
    }

    public HistoryResponse getHistoryResponse(TransactionQueryParams params, String accessToken) {
        try {
            Map<String, Object> paramMap = GsonUtil.jsonToMap(GsonUtil.toJson(params));
            paramMap.remove("originalTransactionId");
            AppleRequest appleRequest = new AppleTransactionHistoryRequest(this.gatewayUrl
                    , accessToken
                    , params.getOriginalTransactionId()
                    , paramMap
                    , proxyUrls);
            appleRequest.setUrlParams(paramMap);
            HistoryResponse response = appleRequest.doRequest();
            return response;
        } catch (AppleServerRequestException e) {
            log.error("apple server request error:{}", e);
        }
        return null;
    }

    public StatusResponse subscribeStatusResponse(String originalTransaction, String accessToken) {
        AppleRequest appleRequest = new AppleSubscribeStatusRequest(this.gatewayUrl
                , accessToken
                , originalTransaction
                , proxyUrls);
        try {
            StatusResponse response = appleRequest.doRequest();
            return response;
        } catch (AppleServerRequestException e) {
            log.error("apple server request error:{}", e);
        }
        return null;
    }

    public TransactionInfoResponse getTransactionInfo(String transactionId, String accessToken) {
        AppleRequest appleRequest = new AppleTransactionInfoRequest(this.gatewayUrl
                , accessToken
                , transactionId
                , new TypeToken<TransactionInfoResponse>(){}
                , proxyUrls);
        try {
            TransactionInfoResponse transactionInfoResponse = appleRequest.doRequest();
            return transactionInfoResponse;
        } catch (AppleServerRequestException e) {
            log.error("apple server request error:{}", e);
        }
        return null;
    }

    public OrderLookupResponse lookupOrder(String orderId, String accessToken) {
        AppleRequest appleRequest = new AppleOrderLookupRequest(this.gatewayUrl
                , accessToken
                , orderId
                , new TypeToken<OrderLookupResponse>(){}
                , proxyUrls);
        try {
            OrderLookupResponse orderLookupResponse = appleRequest.doRequest();
            return orderLookupResponse;
        } catch (AppleServerRequestException e) {
            log.error("apple server request error:{}", e);
        }
        return null;
    }
}
