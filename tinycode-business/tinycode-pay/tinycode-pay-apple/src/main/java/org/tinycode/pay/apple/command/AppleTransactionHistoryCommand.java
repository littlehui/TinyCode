package org.tinycode.pay.apple.command;

import org.tinycode.pay.apple.dto.HistoryResponse;
import org.tinycode.pay.apple.dto.TransactionQueryParams;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 19:03
 */
public class AppleTransactionHistoryCommand implements Command<TransactionQueryParams, HistoryResponse> {

    public AppleCommandExecutor executor;

    public AppleTransactionHistoryCommand(AppleCommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public HistoryResponse execute(TransactionQueryParams transactionQueryParams, String accessToken) {
        return executor.getHistoryResponse(transactionQueryParams, accessToken);
    }
}
