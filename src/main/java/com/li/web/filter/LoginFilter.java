package com.li.web.filter;

import com.li.entity.User;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(urlPatterns = {"/*"})
public class LoginFilter extends OncePerRequestFilter {

    //免登录就可访问的路径(比如:注册,登录,注册页面上的一些获取数据等)
    String[] includeUrls = new String[]{"/login.html","/login","/user/login","/register.html","/user/register"};
    //给静态资源放行
    String[] staticResources = new String[]{"/js/","/images/","/css/"};
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取当前请求
        String uri = request.getRequestURI();

        //判断是否是静态资源
        boolean istrue = isStaticResource(uri);
        if(istrue) {
            filterChain.doFilter(request,response);
        } else {
            //判断url是否需要过滤
            boolean needFilter = isNeedFilter(uri);
            if (!needFilter) {
                filterChain.doFilter(request, response);
            } else {
                //访问除登录页面外的其他页面，需判断是否登录过
                User user = (User) request.getSession().getAttribute("user");
                if (user != null) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendRedirect("/login.html");
                }
            }
        }
    }

    /**
     * @Author: wdd
     * @Description: 是否需要过滤
     * @Date: 2019-02-21 13:20:54
     * @param uri
     */
    public boolean isNeedFilter(String uri) {

        for (String includeUrl : includeUrls) {
            if (includeUrl.equals(uri)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @Author: wdd
     * @Description: 是否是静态资源
     * @Date: 2019-02-21 13:20:54
     * @param uri
     */
    public boolean isStaticResource(String uri) {

        for (String staticResource : staticResources) {
            if (uri.indexOf(staticResource) >=0 ) {
                return true;
            }
        }

        return false;
    }
}
