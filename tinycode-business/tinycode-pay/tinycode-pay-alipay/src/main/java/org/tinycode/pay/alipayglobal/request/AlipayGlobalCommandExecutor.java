package org.tinycode.pay.alipayglobal.request;

import cn.hutool.json.JSONUtil;
import com.alipay.global.api.DefaultAlipayClient;
import com.alipay.global.api.exception.AlipayApiException;
import com.alipay.global.api.model.Result;
import com.alipay.global.api.model.ResultStatusType;
import com.alipay.global.api.model.ams.*;
import com.alipay.global.api.request.ams.pay.AlipayPayQueryRequest;
import com.alipay.global.api.request.ams.pay.AlipayPayRequest;
import com.alipay.global.api.response.ams.pay.AlipayPayQueryResponse;
import com.alipay.global.api.response.ams.pay.AlipayPayResponse;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.tinycode.pay.alipayglobal.dto.*;
import org.tinycode.pay.alipayglobal.exception.AlipaySdkException;
import org.tinycode.pay.alipayglobal.utils.GsonUtil;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/7/27 22:09
 */
@Slf4j
public class AlipayGlobalCommandExecutor {

    private AliPayGlobalAuth aliPayGlobalAuth;

    DefaultAlipayClient defaultAlipayClient;

    public AlipayGlobalCommandExecutor(String gatewayUrl, AliPayGlobalAuth aliPayGlobalAuth) {
        this.aliPayGlobalAuth = aliPayGlobalAuth;
        defaultAlipayClient = new DefaultAlipayClient(gatewayUrl
                , aliPayGlobalAuth.getMerchantPrivateKey()
                , aliPayGlobalAuth.getAlipayPublicKey());
    }

    public AlipayGlobalPayResult createAlipayGlobalOrder(AlipayGlobalOrderParam alipayGlobalOrderParam) throws AlipaySdkException {
        AlipayPayRequest alipayPayRequest = new AlipayPayRequest();
        String path = "/ams/api/v1/payments/pay";
        if (aliPayGlobalAuth.getSandboxFlag() != null && aliPayGlobalAuth.getSandboxFlag()) {
            //sandbox环境
            path = "/ams/sandbox/api/v1/payments/pay";
        }
        alipayPayRequest.setClientId(aliPayGlobalAuth.getClientId());
        alipayPayRequest.setPath(path);
        alipayPayRequest.setProductCode(ProductCodeType.CASHIER_PAYMENT);
        alipayPayRequest.setPaymentRequestId(alipayGlobalOrderParam.getPaymentRequestId());

        alipayPayRequest.setEnv(PaymentHelper.takeEnv(alipayGlobalOrderParam));
        alipayPayRequest.setOrder(PaymentHelper.takeAlipayOrder(alipayGlobalOrderParam));
        alipayPayRequest.setPaymentAmount(PaymentHelper.takeAmount(alipayGlobalOrderParam));
        alipayPayRequest.setPaymentMethod(PaymentHelper.takePaymentMethod(alipayGlobalOrderParam));
        alipayPayRequest.setPaymentNotifyUrl(alipayGlobalOrderParam.getNotifyUrl());
        alipayPayRequest.setPaymentRedirectUrl(alipayGlobalOrderParam.getRedirectUrl());
        alipayPayRequest.setPaymentRequestId(alipayGlobalOrderParam.getPaymentRequestId());
        alipayPayRequest.setProductCode(ProductCodeType.CASHIER_PAYMENT);
        alipayPayRequest.setSettlementStrategy(PaymentHelper.takeSettlementStrategy(alipayGlobalOrderParam, aliPayGlobalAuth.getSandboxFlag()));
        alipayPayRequest.setKeyVersion(1);

        try {
            log.info("AlipayGlobal-Request:{}", JSONUtil.toJsonPrettyStr(alipayPayRequest));
            AlipayPayResponse alipayPayResponse = defaultAlipayClient.execute(alipayPayRequest);
            AlipayGlobalPayResult alipayGlobalPayResult = AlipayGlobalPayResult.builder().build();
            log.info("AlipayGlobal-Response:{}", GsonUtil.toJson(alipayPayResponse));
            if (alipayPayResponse.getResult().getResultStatus() == ResultStatusType.F) {
                log.error("创建支付宝订单失败:{}", GsonUtil.toJson(alipayPayResponse));
                throw new AlipaySdkException("创建支付宝订单失败:" + GsonUtil.toJson(alipayPayResponse));
            }
            if (alipayPayResponse.getNormalUrl() != null) {
                alipayGlobalPayResult.setNormalUrl(alipayPayResponse.getNormalUrl());
            }
            if (alipayPayResponse.getApplinkUrl() != null) {
                alipayGlobalPayResult.setAppLinkUrl(alipayPayResponse.getApplinkUrl());
            }
            if (alipayPayResponse.getPaymentActionForm() != null) {
                alipayGlobalPayResult.setForm(alipayPayResponse.getPaymentActionForm());
            }
            if (alipayPayResponse.getPaymentId() != null) {
                alipayGlobalPayResult.setPaymentId(alipayPayResponse.getPaymentId());
            }
            alipayGlobalPayResult.setPayRequestId(alipayPayRequest.getPaymentRequestId());
            return alipayGlobalPayResult;
        } catch (Exception e) {
            log.error("Alipay Exception", e);
            if (e instanceof AlipaySdkException) {
                throw (AlipaySdkException)e;
            }
            throw new AlipaySdkException(e);
        }
    }

    public InquiryPaymentDTO inquirePayment(String paymentId, String paymentRequestId) throws AlipaySdkException {
        String path = "/ams/api/v1/payments/inquiryPayment";
        if (aliPayGlobalAuth.getSandboxFlag() != null && aliPayGlobalAuth.getSandboxFlag()) {
            //sandbox环境
            path = "/ams/sandbox/api/v1/payments/inquiryPayment";
        }
        AlipayPayQueryRequest alipayPayRequest = new AlipayPayQueryRequest();
        alipayPayRequest.setClientId(aliPayGlobalAuth.getClientId());
        alipayPayRequest.setPath(path);
        if (paymentId != null) {
            alipayPayRequest.setPaymentId(paymentId);
        }
        if (paymentRequestId != null) {
            alipayPayRequest.setPaymentRequestId(paymentRequestId);
        }
        alipayPayRequest.setKeyVersion(1);
        try {
            log.info("AlipayGlobal-Request：{}", GsonUtil.toJson(alipayPayRequest));
            AlipayPayQueryResponse inquiryPaymentResponse = defaultAlipayClient.execute(alipayPayRequest);
            Result result = inquiryPaymentResponse.getResult();
            log.info("AlipayGlobal-Response：{}", GsonUtil.toJson(inquiryPaymentResponse));
            if (result.getResultStatus() != ResultStatusType.S) {
                log.error("AlipayGlobal-Response failure :paymentId:{},paymentRequestId:{}", paymentId, paymentRequestId);
                throw new AlipaySdkException("请求支付宝订单详情失败,paymentID:" + paymentId);
            }
            InquiryPaymentDTO inquiryPaymentDTO = GsonUtil.fromJson(GsonUtil.toJson(inquiryPaymentResponse), new TypeToken<InquiryPaymentDTO>(){});
            return inquiryPaymentDTO;
        } catch (AlipayApiException e) {
            log.error("alipay global调用错误", e);
            throw new AlipaySdkException("请求异常");
        }
    }

    private AlipayGlobalResult takeResult(Result result) {
        AlipayGlobalResult alipayGlobalResult = AlipayGlobalResult.builder()
                .resultCode(result.getResultCode())
                .resultMessage(result.getResultMessage())
                .resultStatus(result.getResultStatus().name())
                .build();
        return alipayGlobalResult;
    }
}
