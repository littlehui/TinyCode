package org.tinycode.pay.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 16:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionGroupIdentifierItem implements Serializable {

    private String subscriptionGroupIdentifier;

    private List<LatestTransactionItem> lastTransactions;

}
