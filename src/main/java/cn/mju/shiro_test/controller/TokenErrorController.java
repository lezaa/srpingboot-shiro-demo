package cn.mju.shiro_test.controller;

import cn.mju.shiro_test.Exception.SystemException;
import cn.mju.shiro_test.base.ApiErrorResult;
import cn.mju.shiro_test.base.ApiResponse;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * filter自定义异常
 */
@RestController
public class TokenErrorController extends BasicErrorController {


    public TokenErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    /**
     * produces 设置返回的数据类型：application/json
     *
     * @param request 请求
     * @return 自定义的返回实体类
     */
    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        // 获取错误信息
        String message = body.get("message").toString();
        String code = body.get("status").toString();
        ApiErrorResult apiErrorResult = new ApiErrorResult(false,code,message);
        return new ResponseEntity<>(apiErrorResult, status);
    }
}
