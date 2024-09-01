package org.tinycode.pay.apple.dto;

import org.tinycode.pay.apple.command.ResponseDecoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2023/8/8 15:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInfoResponse {

    /**
     * @description https://developer.apple.com/documentation/appstoreserverapi/jwstransaction
     * @param null
     * @author littlehui
     * @date 2023/8/8 15:29
     * @return
     */
    private String signedTransactionInfo;

    private Integer errorCode;

    private String errorMessage;

    private JWSTransactionDecodePayload transactionInfo;

    public JWSTransactionDecodePayload getTransactionInfo() {
        if (transactionInfo != null) {
            return transactionInfo;
        } else {
            if (signedTransactionInfo != null) {
                transactionInfo = ResponseDecoder.takePayload(signedTransactionInfo, JWSTransactionDecodePayload.class);
            }
        }
        return transactionInfo;
    }
}
