package org.tinycode.pay.apple.dto;

import org.tinycode.pay.apple.command.ResponseDecoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2023/11/23 13:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLookupResponse {
    //0
    //The orderId that you provided in the Look Up Order ID request is valid.
    //
    //1
    //The orderId is invalid.
    private String status;

    private List<String> signedTransactions;

    private Integer errorCode;

    private String errorMessage;

    private List<JWSTransactionDecodePayload> transactionInfos;

    public List<JWSTransactionDecodePayload> getTransactionInfos() {
        if (transactionInfos != null) {
            return transactionInfos;
        } else {
            if (signedTransactions != null) {
                transactionInfos = new ArrayList<>();
                for (String signedTransaction : signedTransactions) {
                    transactionInfos.add(ResponseDecoder.takePayload(signedTransaction, JWSTransactionDecodePayload.class));
                }
            }
        }
        return transactionInfos;
    }
}
