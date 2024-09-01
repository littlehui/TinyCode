package org.tinycode.pay.alipayglobal.request;

import com.alipay.global.api.model.ams.*;
import org.tinycode.pay.alipayglobal.dto.AlipayGlobalOrderParam;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2024/1/25 11:42
 */
public class PaymentHelper {




    public static TerminalType takeTerminalType(String terminal) {
        for (TerminalType terminalType : TerminalType.values()) {
            if (terminalType.name().equals(terminal)) {
                return terminalType;
            }
        }
        return null;
    }

    public static OsType takeOsType(String osType) {
        for (OsType type : OsType.values()) {
            if (type.name().equals(osType)) {
                return type;
            }
        }
        return null;
    }

    public static Order takeAlipayOrder(AlipayGlobalOrderParam alipayGlobalOrderParam) {
        //设置order
        Order order = new Order();
        order.setReferenceOrderId(alipayGlobalOrderParam.getReferenceOrderId());
        order.setOrderAmount(takeAmount(alipayGlobalOrderParam));
        order.setOrderDescription(alipayGlobalOrderParam.getOrderDescription());
        return order;
    }

    public static Amount takeAmount(AlipayGlobalOrderParam alipayGlobalOrderParam) {
        //设置amunt
        Amount amount = new Amount();
        amount.setCurrency(alipayGlobalOrderParam.getCurrency());
        amount.setValue(alipayGlobalOrderParam.getValue() + "");
        return amount;
    }

    public static Env takeEnv(AlipayGlobalOrderParam alipayGlobalOrderParam) {
        Env env = new Env();
        env.setTerminalType(takeTerminalType(alipayGlobalOrderParam.getTerminal()));
        if (!(env.getTerminalType() == TerminalType.WEB)) {
            env.setOsType(takeOsType(alipayGlobalOrderParam.getOsType()));
        }
        return env;
    }

    public static PaymentMethod takePaymentMethod(AlipayGlobalOrderParam alipayGlobalOrderParam) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentMethodType(alipayGlobalOrderParam.getPaymentMethodType());
        return paymentMethod;
    }

    public static SettlementStrategy takeSettlementStrategy(AlipayGlobalOrderParam alipayGlobalOrderParam, Boolean testFlag) {
        SettlementStrategy settlementStrategy = new SettlementStrategy();
        if (testFlag) {
            settlementStrategy.setSettlementCurrency("USD");
        } else {
            settlementStrategy.setSettlementCurrency(alipayGlobalOrderParam.getSettlementCurrency());
        }
        return settlementStrategy;
    }
}
