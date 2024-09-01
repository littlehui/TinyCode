package org.tinycode.pay.apple.dto;

import org.tinycode.pay.apple.command.ResponseDecoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse implements Serializable {

    private Long appAppleId;

    private String bundleId;

    private String environment;

    private boolean hasMore;

    private String revision;

    private List<String> signedTransactions;

    private List<JWSTransactionDecodePayload> transactionInfos;

    public List<JWSTransactionDecodePayload> getTransactionInfos() {
        if (transactionInfos != null) {
            return transactionInfos;
        } else {
            transactionInfos = new ArrayList<>(signedTransactions != null ? signedTransactions.size() : 1);
            for (String signed : signedTransactions) {
                transactionInfos.add(ResponseDecoder.takePayload(signed, JWSTransactionDecodePayload.class));
            }
        }
        return transactionInfos;
    }
}
