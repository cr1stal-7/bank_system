package com.demo_bank_v1.interceptors;

import com.demo_bank_v1.models.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class AppInterceptor  implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("In Pre Handle Interceptor Method");
        //Проверяем URI запроса
        if(request.getRequestURI().startsWith("/app")){
            HttpSession session = request.getSession();

            //Получить токен, сохраненный в сеансе
            String token = (String) session.getAttribute("token");
            System.out.println(token);
            //Получить обьект, сохраненный в сеансе
            User user = (User)session.getAttribute("user");

            if(token == null || user == null){
                response.sendRedirect("/login");
                return false;
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("In Post Handle Interceptor Method");
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("In After Completion Interceptor Method");
    }
}
// End Of Interceptor Class.
