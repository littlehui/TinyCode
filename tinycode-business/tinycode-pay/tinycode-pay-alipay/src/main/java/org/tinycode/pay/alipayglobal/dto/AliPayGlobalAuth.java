package org.tinycode.pay.alipayglobal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2024/1/24 16:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AliPayGlobalAuth {

    String merchantPrivateKey;

    String alipayPublicKey;

    String clientId;

    Boolean sandboxFlag;
}
