package cn.mju.shiro_test.base;



/**
 * 接口同一返回格式
 */
public class ApiResponse {

    private int code;
    private String message;
    private Object data;

    public ApiResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ApiResponse ofMessage(int code,String message){
        return new ApiResponse(code,message);
    }

    public static ApiResponse ofSuccess(Object data){
        return new ApiResponse(Status.SUCCESS.getCode(),Status.SUCCESS.getStandardMessage(),data);
    }

    public static ApiResponse ofSuccess(){
        return new ApiResponse(Status.SUCCESS.getCode(),Status.SUCCESS.getStandardMessage());
    }


    public static ApiResponse ofStatus(Status status){
        return new ApiResponse(status.getCode(),status.getStandardMessage(),null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
