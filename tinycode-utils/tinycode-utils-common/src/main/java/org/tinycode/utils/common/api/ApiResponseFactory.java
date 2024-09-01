package org.tinycode.utils.common.api;

/**
 * 常用ApiResponse工厂.
 * 
 * @Company : cyou
 * @author littlehui
 * @date 2012-9-29 下午02:29:23
 */
public class ApiResponseFactory {
    /**
     * 获取默认操作成功ApiResponse.
     * 
     * @return
     * @author littlehui
     * @date 2012-9-29 下午02:30:07
     */
    public static <T> ApiResponse<T> getDefaultSuccessResponse() {
        ApiResponse<T> apiResponse = new ApiResponse<T>();
        apiResponse.setResult(ApiResponse.RESULT_SUCCESS);
        return apiResponse;
    }

    public static <T> ApiResponse<T> getDefaultSuccessResponse(String msg) {
        ApiResponse<T> apiResponse = getDefaultSuccessResponse();
        apiResponse.setMessage(msg);
        return apiResponse;
    }

    /**
     * 获取默认操作失败ApiResponse.
     * 
     * @return
     * @author littlehui
     * @date 2012-11-14 下午04:37:39
     */
    public static <T> ApiResponse<T> getDefaultFailureResponse() {
        ApiResponse<T> apiResponse = new ApiResponse<T>();
        apiResponse.setResult(ApiResponse.RESULT_FAILURE);
        return apiResponse;
    }

    public static <T> ApiResponse<T> getDefaultFailureResponse(String msg) {
        ApiResponse<T> apiResponse = getDefaultFailureResponse();
        apiResponse.setMessage(msg);
        return apiResponse;
    }

    /**
     * 获取默认输入操作失败ApiResponse.
     * 
     * @return
     */
    public static <T> ApiResponse<T> getDefaultInputFailureResponse() {
        ApiResponse<T> apiResponse = new ApiResponse<T>();
        apiResponse.setResult(ApiResponse.RESULT_INPUT);
        return apiResponse;
    }

    public static <T> ApiResponse<T> getDefaultInputFailureResponse(String msg) {
        ApiResponse<T> apiResponse = getDefaultInputFailureResponse();
        apiResponse.setMessage(msg);
        return apiResponse;
    }

    public static <T> ApiResponse<T> getDefaultSuccessDataResponse(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<T>();
        apiResponse.setResult(ApiResponse.RESULT_SUCCESS);
        apiResponse.setData(data);
        return apiResponse;
    }
}