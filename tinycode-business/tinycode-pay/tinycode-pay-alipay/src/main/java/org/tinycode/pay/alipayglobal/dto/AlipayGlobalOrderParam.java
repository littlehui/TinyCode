package org.tinycode.pay.alipayglobal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * {
 *   "env": {
 *     "osType": "ANDROID",
 *     "terminalType": "APP"
 *   },
 *   "order": {
 *     "orderAmount": {
 *       "currency": "KRW",
 *       "value": "1314"
 *     },
 *     "orderDescription": "Cappuccino #grande (Mika's coffee shop)",
 *     "referenceOrderId": "ORDER_0798799064929XXXX"
 *   },
 *   "paymentAmount": {
 *     "currency": "KRW",
 *     "value": "1314"
 *   },
 *   "paymentMethod": {
 *     "paymentMethodType": "KAKAOPAY"
 *   },
 *   "paymentNotifyUrl": "https://www.gaga.com/notify",
 *   "paymentRedirectUrl": "imeituan://",
 *   "paymentRequestId": "PAY_0798799064929XXXX",
 *   "productCode": "CASHIER_PAYMENT",
 *   "settlementStrategy": {
 *     "settlementCurrency": "USD"
 *   }
 * }
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2024/1/24 17:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlipayGlobalOrderParam {

    //IN_STORE_PAYMENT
    //private String productCode;

    //WEB,WAP,APP
    private String terminal;

    //ANDROID
    private String osType;

    //请求幂等性id
    private String paymentRequestId;

    //order信息
    //OrderAmount
    private String referenceOrderId;

    private String currency;

    private Integer value;

    private String orderDescription;

    //paymentMethod KAKAOPAY,TOSS
    private String paymentMethodType;

    //付款到期时间
    //private Date paymentExpiryTime;

/*    private String paymentRedirectUrl;

    private String paymentNotifyUrl;*/

    private String settlementCurrency;

    private String redirectUrl;

    private String notifyUrl;
}
