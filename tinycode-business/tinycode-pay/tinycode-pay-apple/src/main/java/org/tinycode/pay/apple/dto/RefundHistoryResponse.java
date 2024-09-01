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
 * @date 2022/11/15 15:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundHistoryResponse {

    private boolean hasMore;

    private String revision;

    private List<String> signedTransactions;

    private List<JWSTransactionDecodePayload> jwsTransactionInfos;

    public List<JWSTransactionDecodePayload> getJwsTransactionInfos() {
        if (jwsTransactionInfos != null) {
            return jwsTransactionInfos;
        } else {
            jwsTransactionInfos = new ArrayList<>(signedTransactions != null ? signedTransactions.size() : 1);
            for (String signed : signedTransactions) {
                jwsTransactionInfos.add(ResponseDecoder.takePayload(signed, JWSTransactionDecodePayload.class));
            }
        }
        return jwsTransactionInfos;
    }
}
