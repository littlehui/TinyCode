package org.tinycode.pay.alipayglobal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2024/1/25 17:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlipayGlobalPayResult {

    private String normalUrl;

    private String appLinkUrl;

    private String form;

    private String paymentId;

    private String payRequestId;
}
