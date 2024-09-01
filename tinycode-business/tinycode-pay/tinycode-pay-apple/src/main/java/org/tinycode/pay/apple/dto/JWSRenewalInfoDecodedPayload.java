package org.tinycode.pay.apple.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/11/03 15:32
 */
@Data
public class JWSRenewalInfoDecodedPayload implements Serializable {

   //The product identifier of the product that renews at the next billing period.
   private String autoRenewProductId;

   //0:Automatic renewal is off. The customer has turned off automatic renewal for the subscription, and it won’t renew at the end of the current subscription period.
   //1:Automatic renewal is on. The subscription renews at the end of the current subscription period.
   private Integer autoRenewStatus;

   private String environment;

   //The reason a subscription expired.
   //1: The customer canceled their subscription.
   //2:Billing error; for example, the customer’s payment information is no longer valid.
   //3:The customer didn’t consent to an auto-renewable subscription price increase that requires customer consent, allowing the subscription to expire.
   //4:The product wasn’t available for purchase at the time of renewal.
   private Integer expirationIntent;

   //gracePeriodExpiresDate
   private Long gracePeriodExpiresDate;

   //A Boolean value that indicates whether the App Store is attempting to automatically renew a subscription that expired due to a billing issue.
   private Boolean isInBillingRetryPeriod;

   //The identifier that contains the promo code or the promotional offer identifier.
   //The offerIdentifier applies only when the offerType has a value of 2 or 3.
   //
   //The offerIdentifier provides details about the subscription offer in effect for the transaction. Its value is either the promo code or the promotional offer. For more information about promo codes, see Promo codes overview. For more information about promotional offers, see Set up promotional offers for auto-renewable subscriptions.
   private String offerIdentifier;

   //The type of subscription offer.
   //1:An introductory offer.
   //2:A promotional offer.
   //3:An offer with a subscription offer code.
   private Integer offerType;

   //The original transaction identifier of a purchase.
   private String originalTransactionId;

   //The status that indicates whether an auto-renewable subscription is subject to a price increase.
   //0:The customer hasn’t yet responded to an auto-renewable subscription price increase that requires customer consent.
   //1:The customer consented to an auto-renewable subscription price increase that requires customer consent, or the App Store has notified the customer of an auto-renewable subscription price increase that doesn’t require consent.
   private Integer priceIncreaseStatus;

   private String productId;

   private Long recentSubscriptionStartDate;

   //The UNIX time, in milliseconds, that the App Store signed the JSON Web Signature data.
   private Long signedDate;
}
