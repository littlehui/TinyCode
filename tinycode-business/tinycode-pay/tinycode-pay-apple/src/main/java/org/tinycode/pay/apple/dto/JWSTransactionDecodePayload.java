package org.tinycode.pay.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 15:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWSTransactionDecodePayload implements Serializable {

    //A UUID that associates the transaction with a user on your own service. If your app doesn’t provide an appAccountToken, this string is empty. For more information, see appAccountToken(_:).
    private String appAccountToken;

    private String bundleId;

    private String environment;

    private Long expiresDate;

    //A string that describes whether the transaction was purchased by the user, or is available to them through Family Sharing.
    //FAMILY_SHARED:The transaction belongs to a family member who benefits from the service.
    //PURCHASED: The transaction belongs to the purchaser.
    private String inAppOwnershipType;

    //A Boolean value that indicates whether the user upgraded to another subscription.
    //If isUpgraded is true, the user has upgraded the subscription represented by this transaction to another subscription. This value appears in the transaction only when the value is true. To determine the service that the customer is entitled to, look for another transaction that has a subscription with a higher level of service. For more information about subscription groups and levels of service, see Auto-renewable Subscriptions.
    private Boolean isUpgraded;

    //The identifier that contains the promo code or the promotional offer identifier.
    //The offerIdentifier applies only when the offerType has a value of 2 or 3.
    private String offerIdentifier;

    //1:An introductory offer.
    //2:A promotional offer.
    //3:An offer with a subscription offer code.
    private Integer offerType;

    private Long originalPurchaseDate;

    private String originalTransactionId;

    private String productId;

    private Long purchaseDate;

    private Integer quantity;

    //The UNIX time, in milliseconds, that the App Store refunded the transaction or revoked it from Family Sharing.
    private Long revocationDate;

    //The reason that the App Store refunded the transaction or revoked it from Family Sharing.
    private Integer revocationReason;

    //The UNIX time, in milliseconds, that the App Store signed the JSON Web Signature (JWS) data.
    private Long signedDate;

    //The identifier of the subscription group that the subscription belongs to.
    private String subscriptionGroupIdentifier;

    private String transactionId;

    //Auto-Renewable Subscription
    //Non-Consumable
    //Consumable
    //Non-Renewing Subscription
    private String type;

    //The unique identifier of subscription purchase events across devices, including subscription renewals.
    private String webOrderLineItemId;

}
