package com.li.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.User;
import com.li.service.CartService;
import com.li.service.UserService;
import com.li.vo.CartVO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private CartService cartService;


    @PostMapping("/login")
    public String login(String loginName, String password, HttpServletRequest request) {
        //查询该用户是否存在
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("login_name",loginName);
        wrapper.eq("password",password);

        User user = userService.getOne(wrapper);
        if(user != null) {
            //把用户放入session域
            request.getSession().setAttribute("user",user);
            //把购物车的数据放入session中
            List<CartVO> cartVOList = cartService.findAllCartVO(user.getId());
            request.getSession().setAttribute("cartList",cartVOList);
            return "redirect:/main.html";
        } else {
            return "login";
        }
    }

    @GetMapping("/signout")
    public String signOut(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        boolean save = false;
        try {
            save = userService.save(user);
        } catch (Exception e) {
            model.addAttribute("err",user.getLoginName()+"账号已存在！！请重新输入！");
            model.addAttribute("user",user);
        }
        if(save) {
            return "redirect:/login.html";
        }
        return "register";
    }

    /**
     * 用户信息
     */
    @GetMapping("/userInfo")
    public ModelAndView userInfo(HttpSession session){
        User user = (User) session.getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userInfo");
        modelAndView.addObject("cartList",cartService.findAllCartVO(user.getId()));
        return modelAndView;
    }
}

