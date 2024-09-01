package org.tinycode.pay.apple.command;

import org.tinycode.pay.apple.dto.RefundHistoryResponse;
import org.tinycode.pay.apple.dto.RefundQueryParams;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 19:01
 */
public class AppleRefundHistoryCommand implements Command<RefundQueryParams, RefundHistoryResponse> {

    public AppleCommandExecutor executor;

    public AppleRefundHistoryCommand(AppleCommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public RefundHistoryResponse execute(RefundQueryParams refundQueryParams, String accessToken) {
        return executor.getRefundHistory(refundQueryParams, accessToken);
    }
}
