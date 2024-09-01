package org.tinycode.pay.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 19:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionQueryParams {

    private String originalTransactionId;

    private String revision;
}
