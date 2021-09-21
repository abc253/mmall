package com.li.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Cart;
import com.li.entity.Product;
import com.li.entity.User;
import com.li.entity.UserAddress;
import com.li.service.CartService;
import com.li.service.ProductService;
import com.li.service.UserAddressService;
import com.li.vo.CartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lw
 * @since 2021-09-16
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    @Resource
    private CartService cartService;
    @Resource
    private UserAddressService userAddressService;

    //用来避免用户直接访问settlement1，settlement2，settlement3，这是不合法的
    public static int PAGE_INDEX = 1;

    /*
     *把数据添加到购物车中
     */
    @GetMapping("/add")
    public String addCart(Cart cart, Float price, HttpSession session) {
        //获取user_id
        User user = (User) session.getAttribute("user");
        Integer userId = user.getId();
        //userId赋给cart
        cart.setUserId(userId);
        cart.setCost(price*(cart.getQuantity()));

        //添加购物车到数据库
        boolean isSave = cartService.save(cart);

        if(isSave) {
            return "redirect:/cart/cartList";
            //return "settlement1";
        }

        return "main";
    }

    /*
     *查找购物车中的所有数据
     */
    @GetMapping("/cartList")
    public ModelAndView findAllCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<CartVO> cartVOList = cartService.findAllCartVO(user.getId());

        ModelAndView mv = new ModelAndView();

        mv.setViewName("settlement1");
        mv.addObject("cartList",cartVOList);

        return mv;
    }

    /*
     * 修改购物车的信息
     */
    @PostMapping("/updateCart/{cartId}/{quantity}/{cost}")
    @ResponseBody
    public String updateCart(@PathVariable("cartId") Integer cartId,
                             @PathVariable("quantity") Integer quantity,
                             @PathVariable("cost") Float cost) {
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setQuantity(quantity);
        cart.setCost(cost);

        boolean flag = cartService.updateById(cart);
        if(flag) {
            return "success";
        }

        return "fail";
    }

    /*
     *删除指定的购物车记录
     */
    @GetMapping("/removeCart/{cartId}")
    public String deleteById(@PathVariable("cartId") Integer cartId) {

        cartService.removeById(cartId);

        return "redirect:/cart/cartList";
    }


    @GetMapping("/settlement2")
    public ModelAndView settlement1(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Integer userId = user.getId();
        List<CartVO> newCartVOList = cartService.findAllCartVO(userId);
        //跟新session中购物车的记录
        session.setAttribute("cartList",newCartVOList);
        //获取当前用户的的地址信息
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",userId);
        List<UserAddress> addressList = userAddressService.list(wrapper);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("settlement2");
        mv.addObject("addressList",addressList);

        return mv;
    }


    @GetMapping("/orderList")
    public String orderList() {
        return "orderList";
    }
}

