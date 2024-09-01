package org.tinycode.pay.apple.exception;

import lombok.Getter;


public class AppleServerRequestException extends Exception {

    @Getter
    private Integer code;

    public AppleServerRequestException(String hunterResponseMsg, Integer code) {
        super(hunterResponseMsg);
        this.code = code;
    }

    public AppleServerRequestException(Throwable e) {
        super(e);
        this.code = code;
    }

}
