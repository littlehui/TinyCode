package org.tinycode.pay.alipayglobal;

import org.tinycode.pay.alipayglobal.dto.AliPayGlobalAuth;
import org.tinycode.pay.alipayglobal.dto.AlipayGlobalOrderParam;
import org.tinycode.pay.alipayglobal.dto.AlipayGlobalPayResult;
import org.tinycode.pay.alipayglobal.dto.InquiryPaymentDTO;
import org.tinycode.pay.alipayglobal.exception.AlipaySdkException;
import org.tinycode.pay.alipayglobal.request.AlipayGlobalCommandExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2024/1/24 16:20
 */
public class AlipayGlobalClient {

    private static final String ENV_PROD = "Sandbox";

    private static final String ENV_DEV = "Production";

    private final static Map<String, String> GATEWAY_URL_MAP = new HashMap<>();

    AlipayGlobalCommandExecutor alipayGlobalCommandExecutor;

    static {
        GATEWAY_URL_MAP.put(ENV_PROD, "https://open-sea-global.alipay.com");
        GATEWAY_URL_MAP.put(ENV_DEV, "https://open-sea-global.alipay.com");
    }

    public AlipayGlobalClient(String gatewayUrl, AliPayGlobalAuth aliPayGlobalAuth) {
        String url = "";
        if (gatewayUrl.startsWith("http")) {
            //是地址
            url = gatewayUrl;
        } else {
            //是环境
            url = GATEWAY_URL_MAP.get(gatewayUrl);
        }
        alipayGlobalCommandExecutor = new AlipayGlobalCommandExecutor(url, aliPayGlobalAuth);
    }

    /**
     * 支付宝支付
     * @description alipayGlobalPay
     * @param orderParam
     * @author littlehui
     * @date 2024/1/25 13:36
     * @return java.lang.String
     */
    public AlipayGlobalPayResult alipayGlobalPay(AlipayGlobalOrderParam orderParam) throws AlipaySdkException {
        return alipayGlobalCommandExecutor.createAlipayGlobalOrder(orderParam);
    }

    /**
     * 查询支付状态
     * @description paymentResult
     * @param paymentId
     * @param paymentRequestId
     * @author littlehui
     * @date 2024/1/25 13:37
     * @return org.tinycode.pay.org.tinycode.pay.alipayglobal.dto.InquiryPaymentDTO
     */
    public InquiryPaymentDTO paymentResult(String paymentId, String paymentRequestId) throws AlipaySdkException {
        return alipayGlobalCommandExecutor.inquirePayment(paymentId, paymentRequestId);
    }
}
