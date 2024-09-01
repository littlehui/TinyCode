package org.tinycode.pay.google.dto;

import lombok.Data;

/**
 * @Description TODO
 * @ClassName VoidPurcharse
 * @Author littlehui
 * @Date 2020/12/22 18:26
 * @Version 1.0
 **/
@Data
public class VoidPurchase {
    /**
     * {
     *   "kind": string,
     *   "purchaseToken": string,
     *   "purchaseTimeMillis": string,
     *   "voidedTimeMillis": string,
     *   "orderId": string,
     *   "voidedSource": integer,
     *   "voidedReason": integer
     * }
     */

    private String kind;

    private String purchaseToken;

    private String purchaseTimeMillis;

    private String voidedTimeMillis;

    private String orderId;

    private Integer voidedSource;

    private Integer voidedReason;
}
