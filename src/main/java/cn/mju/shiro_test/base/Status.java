package cn.mju.shiro_test.base;

import cn.mju.shiro_test.domain.User;

public enum Status {

    SUCCESS(200,"成功"),
    BAD_REQUEST(400,"请求错误"),
    INTERNAL_SERVER_ERROR(500,"服务器错误"),
    NOT_VALID_PARAM(40005,"参数验证错误"),
    Not_SUPPORTED_OPERATION(40006,"不被支持的操作"),
    NOT_LOGIN(50000,"未登录"),
    NOT_AUTH(50001,"未授权"),
    ACCOUNT_ERROR(60000,"账号不存在"),
    PASSWORD_ERROT(60001,"密码错误"),
    THIS_USERINFO_NOT_EXITS(40004,"该用户不存在"),
    USER_REGISTER(2001,"注册成功"),
    Token_Expried(40007,"token过期"),
    Token_Blank(40008,"token为空" );




    private int code;
    private String standardMessage;


    Status(int code, String standardMessage){
        this.code = code;
        this.standardMessage = standardMessage;
    }

    public static String getStateMessage(int code){
        Status[] values = Status.values();
        for (Status state : values) {
            if(state.code == code){
                return state.standardMessage;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStandardMessage() {
        return standardMessage;
    }

    public void setStandardMessage(String standardMessage) {
        this.standardMessage = standardMessage;
    }
}
