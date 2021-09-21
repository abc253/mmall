package com.li.web.filter;

import com.li.entity.User;
import com.li.vo.CartVO;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


@WebFilter(urlPatterns = {"/cart/*","/user/userInfo","/userAddress/*","/settlement2","/settlement2.html","/settlement3","/settlement3.html"})
public class ShoppingCartFilter extends OncePerRequestFilter {

    /*
     * 过滤所有访问购物车功能的请求
     * 判断是否登录，登录过则可以访问所有购物车的功能
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        HttpSession session = request.getSession();

        //访问访问购物车功能，需判断是否登录过
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            //当购物车为空时，不能进入结算页面
            if("/settlement2".equals(uri) || "/settlement2.html".equals(uri)) {
                List<CartVO> cartList = (List<CartVO>) session.getAttribute("cartList");
                if (cartList == null || cartList.size() == 0) {
                    //转回到购物车页面
                    response.sendRedirect("/settlement1.html");
                }
            } else if("/settlement3".equals(uri) || "/settlement3.html".equals(uri)) {
                //直接访问订单生成页面(settlement3.html)是不合理的
                //转回到购物车页面
                response.sendRedirect("/settlement1.html");
            } else {
                filterChain.doFilter(request, response);
            }

        } else {
            response.sendRedirect("/login.html");
        }
    }
}
