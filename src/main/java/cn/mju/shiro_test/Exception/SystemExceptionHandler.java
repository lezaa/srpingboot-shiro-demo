package cn.mju.shiro_test.Exception;


import cn.mju.shiro_test.base.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * token异常或其他异常
 */
@ControllerAdvice
@ResponseBody
public class SystemExceptionHandler {

    @ExceptionHandler(value = SystemException.class)
    public ApiResponse Handler(SystemException e){
            return ApiResponse.ofMessage(e.getCode(),e.getMessage());

    }
}
