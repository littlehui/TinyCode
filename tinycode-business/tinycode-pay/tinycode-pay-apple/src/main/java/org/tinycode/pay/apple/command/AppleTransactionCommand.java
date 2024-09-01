package org.tinycode.pay.apple.command;

import org.tinycode.pay.apple.dto.TransactionInfoResponse;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 19:03
 */
public class AppleTransactionCommand implements Command<String, TransactionInfoResponse> {

    public AppleCommandExecutor executor;

    public AppleTransactionCommand(AppleCommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public TransactionInfoResponse execute(String transactionId, String accessToken) {
        return executor.getTransactionInfo(transactionId, accessToken);
    }
}
