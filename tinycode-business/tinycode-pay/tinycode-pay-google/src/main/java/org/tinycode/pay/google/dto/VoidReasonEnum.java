package org.tinycode.pay.google.dto;

/**
 * @Description TODO
 * @ClassName VoidReasonEnum
 * @Author littlehui
 * @Date 2020/12/22 18:16
 * @Version 1.0
 **/
public enum VoidReasonEnum {

    /*
    The reason why the purchase was voided, possible values are:
    0. Other
    1. Remorse
    2. Not_received
    3. Defective
    4. Accidental_purchase
    5. Fraud
    6. Friendly_fraud
    7. Chargeback
     */
    OTHER("其他", 0),
    REMORSE("悔恨", 1),
    NOT_RECEIVED("未收到", 2),
    DEFECTIVE("缺陷", 3),
    ACCIDENTAL_PURCHASE("意外购买", 4),
    FRAUD("欺诈", 5),
    FRIENDLY_FRAUD("友好欺诈", 5),
    CHARGEBACK("拒付", 6);


    private Integer value;

    private String name;

    private VoidReasonEnum(String name, Integer value) {
        this.value = value;
        this.name = name;
    }
}
