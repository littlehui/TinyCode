package org.tinycode.pay.apple.dto;

import org.tinycode.pay.apple.command.ResponseDecoder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 16:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestTransactionItem implements Serializable {

    private String originalTransactionId;
    /**
     * 1
     * The auto-renewable subscription is active.
     *
     * 2
     * The auto-renewable subscription is expired.
     *
     * 3
     * The auto-renewable subscription is in a billing retry period.
     *
     * 4
     * The auto-renewable subscription is in a billing grace period.
     *
     * 5
     * The auto-renewable subscription is revoked.
     */
    private Integer status;

    private String signedRenewalInfo;

    private String signedTransactionInfo;

    private JWSTransactionDecodePayload transactionInfo;

    private JWSRenewalInfoDecodedPayload renewalInfo;

    public JWSRenewalInfoDecodedPayload getRenewalInfo() {
        if (renewalInfo != null) {
            return renewalInfo;
        }
        if (signedRenewalInfo != null) {
            renewalInfo = ResponseDecoder.takePayload(signedRenewalInfo, JWSRenewalInfoDecodedPayload.class);
        }
        return renewalInfo;
    }

    public JWSTransactionDecodePayload getTransactionInfo() {
        if (transactionInfo != null) {
            return transactionInfo;
        }
        if (signedTransactionInfo != null) {
            transactionInfo = ResponseDecoder.takePayload(signedTransactionInfo, JWSTransactionDecodePayload.class);
        }
        return transactionInfo;
    }
}
