package com.fayayo.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CrossDomainFilter
 * @Data 2019/9/12 19:12
 * @Version 1.0
 **/
public class CrossDomainFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ((HttpServletResponse)servletResponse).setHeader("Access-Control-Allow-Origin","*");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
