package com.yqg.common.exceptions;

import com.yqg.common.config.CommonConfig;
import com.yqg.common.core.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常输出
 * Created by gao on 2017/12/19.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommonConfig commonConfig;

    /**
     * 全局异常输出
     *
     * @param req
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResponse<String> jsonErrorHandler(HttpServletRequest req, Exception exception) throws Exception {

        BaseResponse response = new BaseResponse<>();
        //业务异常
        if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
//            logger.error("\r\n请求服务路径:{}\r\n业务异常信息:{}", req.getRequestURI(),
//                    JSON.toJSONString(response));
            response = new BaseResponse<>().errorResponse(businessException.getExceptionEnum(), commonConfig.isI18nOpen());
        }else if(exception instanceof HttpMediaTypeNotSupportedException){
            response = new BaseResponse<>().errorResponse(BaseExceptionEnums.PARAM_CONTENT_TYPE_UNSUPPORTED, commonConfig.isI18nOpen());
        }
        else if (exception instanceof MultipartException && exception.getMessage().contains("maximum")) {
//            logger.error("\r\n请求服务路径:{}\r\n系统异常堆栈信息", req.getRequestURI(), exception);
            response = new BaseResponse<>().errorResponse(
                    BaseExceptionEnums.UPLOAD_FILE_OVER_MAX_ERROR, commonConfig.isI18nOpen());

        } else {//其他系统异常或运行时异常
            logger.error("\r\n请求服务路径:{}\r\n系统异常堆栈信息", req.getRequestURI(), exception);
            response = new BaseResponse<>().errorResponse(BaseExceptionEnums.SYSTERM_ERROR, commonConfig.isI18nOpen());
        }
        return response;
    }


}
