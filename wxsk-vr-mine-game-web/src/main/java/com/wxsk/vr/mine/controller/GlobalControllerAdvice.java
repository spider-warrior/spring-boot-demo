package com.wxsk.vr.mine.controller;

import com.wxsk.common.exception.BusinessException;
import com.wxsk.common.json.JSONResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@ControllerAdvice
public class GlobalControllerAdvice {

    private static final Logger logger = LogManager.getLogger(GlobalControllerAdvice.class);

    /**
     * 404
     */
    @ResponseBody
    @ExceptionHandler({NoHandlerFoundException.class})
    public Object noHandlerFound(NoHandlerFoundException e) {
        logger.error("no handler found", e);
        return JSONResult.faild().setErrorCode("404");
    }

    @ResponseBody
    @RequestMapping(value = "/404")
    public Object resourceNotFound(HttpServletRequest request, HttpServletResponse response) {
        logger.error("page not found, url: {}", request.getRequestURL());
        return JSONResult.faild().setErrorCode("page_not_found");
    }

    /**
     * 500
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object exception(Exception e) {
        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            logger.error("business exception, error code: {}, error msg: {}", businessException.getErrorCode(), businessException.getErrorMessage());
            return JSONResult.faild().setErrorCode(businessException.getErrorCode()).setMessage(businessException.getErrorMessage());
        } else {
            logger.error("runtime exception", e);
            return JSONResult.faild().setErrorCode("500");
        }

    }

}
