package org.tinycode.pay.alipayglobal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2024/1/25 11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InquiryPaymentDTO {

    private AlipayGlobalResult result;

    private String paymentRequestId;

    private String paymentStatus;

    private String paymentId;

    private PaymentAmount paymentAmount;

    private PaymentAmount actualPaymentAmount;

    private Date paymentTime;
}
