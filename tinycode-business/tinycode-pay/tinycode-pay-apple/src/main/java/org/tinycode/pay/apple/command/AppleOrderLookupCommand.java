package org.tinycode.pay.apple.command;

import org.tinycode.pay.apple.dto.OrderLookupResponse;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 19:03
 */
public class AppleOrderLookupCommand implements Command<String, OrderLookupResponse> {

    public AppleCommandExecutor executor;

    public AppleOrderLookupCommand(AppleCommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public OrderLookupResponse execute(String orderId, String accessToken) {
        return executor.lookupOrder(orderId, accessToken);
    }
}
