package com.fayayo.filter;

import com.fayayo.bean.SCCUser;
import com.fayayo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author dalizu on 2019/9/25.
 * @version v1.0
 * @desc
 */
public class LoginFilter implements Filter {

    private static Logger logger  = LoggerFactory.getLogger(LoginFilter.class);

    private UserService userService;

    public LoginFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("注册登录过滤器");
    }

    //不需要验证的URI(TODO:配置文件的读取)
    private static final String[] EXCLUDE_CHECK_URI= {"/queryparaminfo"};


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest=(HttpServletRequest)request;
        String path=httpRequest.getRequestURI();
        logger.info("path={}",path);

        if(path.indexOf("/login")<0){

            boolean isExclude = false;
            for (String excludeURI :EXCLUDE_CHECK_URI){
                if(path.indexOf(excludeURI)>=0){
                    isExclude = true;
                    break;
                }
            }

            //1.不需要登录验证
            if(isExclude){
                filterChain.doFilter(request,response);
            }else if(userService.userExistInCache(request.getParameter("token"))){
                filterChain.doFilter(request,response);
            }else{
                //验证未通过，跳转到登录页面
                request.getRequestDispatcher("/loginexpire").forward(request,response);
            }


        }else {
            //登录

            SCCUser sccUser  = userService.login(request.getParameter("username"),request.getParameter("password"));
            //1.正确登陆
            if(sccUser == null){
                //2.未通过登录重定向到登录页
                request.getRequestDispatcher("/loginfail").forward(request,response);
            }else{
                //正确登陆,TODO 这里缺个token返回前端
                request.getRequestDispatcher("/loginsuccess").forward(request,response);
            }
        }
    }

    @Override
    public void destroy() {
        logger.info("注销登录过滤器");
    }


}
