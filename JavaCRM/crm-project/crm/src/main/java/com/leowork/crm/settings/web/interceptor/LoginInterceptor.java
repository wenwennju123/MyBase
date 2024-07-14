package com.leowork.crm.settings.web.interceptor;

import com.leowork.crm.commons.contants.Contants;
import com.leowork.crm.settings.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用于登录验证的拦截器
 * @author Leo
 * @version 1.0
 * @className LoginInterceptor
 * @since 1.0
 **/
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 登录验证
     * @param request 请求
     * @param response 响应
     * @param handler 拦截器处理器
     * @return 拦截器
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*根据请求获取session（Http协议规定）取出保存的user 判断是否为空*/
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        if (null == user){
            /*未登录或者已过期 重定向到首页 重定向是一次全新的请求需要加项目名*/
            response.sendRedirect(request.getContextPath());
            /*拦截*/
            return false;
        }

        /*如果登录，放行*/
        return true;
    }
}
