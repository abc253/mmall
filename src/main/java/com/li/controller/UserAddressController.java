package com.li.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.User;
import com.li.entity.UserAddress;
import com.li.service.UserAddressService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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
@RequestMapping("/userAddress")
public class UserAddressController {
    @Resource
    private UserAddressService userAddressService;
    /**
     * 用户地址信息
     */
    @GetMapping("/userAddressList")
    public ModelAndView userAddressList(HttpSession session){
        User user = (User) session.getAttribute("user");

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",user.getId());
        List<UserAddress> addressList = userAddressService.list(wrapper);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userAddressList");
        modelAndView.addObject("addressList",addressList);
        return modelAndView;
    }

    @Transactional
    @GetMapping("/deleteUserAddress/{id}")
    public String deleteUserAddress(@PathVariable("id") Integer id){
        boolean remove = userAddressService.removeById(id);
        if(remove) {
            return "forward:/userAddress/userAddressList";
        }
        return null;
    }
}

