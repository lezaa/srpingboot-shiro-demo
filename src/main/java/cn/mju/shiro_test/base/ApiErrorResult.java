package cn.mju.shiro_test.base;

import java.util.LinkedHashMap;

public class ApiErrorResult extends LinkedHashMap<String,Object> {
    private static final String SUCCESS_KEY = "success";
    private static final String CODE_KEY = "code";
    private static final String MESSAGE_KEY = "message";

    public ApiErrorResult(boolean success, String code, String message) {
        this.put(SUCCESS_KEY, success);
        this.put(CODE_KEY, code);
        this.put(MESSAGE_KEY, message);
    }
}
