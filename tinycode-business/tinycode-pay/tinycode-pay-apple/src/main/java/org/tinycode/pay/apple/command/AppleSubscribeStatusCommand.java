package org.tinycode.pay.apple.command;

import org.tinycode.pay.apple.dto.StatusResponse;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2022/11/15 17:29
 */
public class AppleSubscribeStatusCommand implements Command<String, StatusResponse> {


    AppleCommandExecutor executor;

    public AppleSubscribeStatusCommand(AppleCommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public StatusResponse execute(String s, String accessToken) {
        return executor.subscribeStatusResponse(s, accessToken);
    }
}
