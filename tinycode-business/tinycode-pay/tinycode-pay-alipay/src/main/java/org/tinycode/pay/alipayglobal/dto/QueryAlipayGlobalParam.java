package org.tinycode.pay.alipayglobal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2024/1/25 11:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryAlipayGlobalParam {

    private String paymentRequestId;

    private String paymentId;

    private PaymentAmount paymentAmount;

    private String paymentCreateTime;

    private String paymentTime;

    private String transactions;
}
