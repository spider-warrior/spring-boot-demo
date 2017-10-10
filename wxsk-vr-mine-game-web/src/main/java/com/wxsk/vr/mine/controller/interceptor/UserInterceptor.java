package com.wxsk.vr.mine.controller.interceptor;

import com.wxsk.cas.client.interceptor.AccessRequiredInteceptor;
import com.wxsk.passport.model.User;
import com.wxsk.vr.mine.common.util.AppContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class UserInterceptor extends AccessRequiredInteceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        boolean ok;
        if (o instanceof HandlerMethod) {
            ok = super.preHandle(request, response, o);
            if (ok) {
                AppContext.initRequestContext((User) request.getAttribute("curent_login_user"), new Date());
            }
        }
        else {
            ok = true;
            AppContext.setCurrentRequestTimePoint(new Date());
        }
        return ok;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        AppContext.clearRequestContext();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    }
}
