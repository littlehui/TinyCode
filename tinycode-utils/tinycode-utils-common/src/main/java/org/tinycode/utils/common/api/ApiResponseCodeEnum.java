package org.tinycode.utils.common.api;

import lombok.Getter;

/**
 * @author littlehui
 * @version 1.0
 * @description TODO
 * @date 2023/4/20 16:04
 */
public enum ApiResponseCodeEnum {

    UNKNOWN(200,"404", "未知"),

    PAY_TYPE_LIMIT(200,"42201", "支付方式不可用");

    @Getter
    private String code;

    @Getter
    private String message;

    @Getter
    private Integer httpStatusCode;

    private ApiResponseCodeEnum(Integer httpStatusCode, String code, String message) {
        this.code = code;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    public static ApiResponseCodeEnum valueOfCode(String code) {
        ApiResponseCodeEnum[] httpResponseStatusEnums = values();
        for (ApiResponseCodeEnum httpResponseStatusEnum : httpResponseStatusEnums) {
            if (httpResponseStatusEnum.code.equals(code)) {
                return httpResponseStatusEnum;
            }
        }
        return UNKNOWN;
    }
}
