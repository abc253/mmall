package com.li.controller;


import com.li.entity.Orders;
import com.li.service.OrderService;
import com.li.vo.AddressVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lw
 * @since 2021-09-16
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;


    @PostMapping("/submit")
    public ModelAndView saveOrder(Orders orders, AddressVo addressVo) {
        orderService.save(orders,addressVo);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("settlement3");
        mv.addObject("orders",orders);
        return mv;
    }

}

