package com.li.controller;


import com.li.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
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
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;


    @GetMapping("/list/{type}/{id}")
    public ModelAndView list(@PathVariable("type") String type, @PathVariable("id") Integer id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productList");
        mv.addObject("productList",productService.findByCategoryId(type,id));
        return mv;
    }

    @GetMapping("/findById/{id}")
    public ModelAndView detail(@PathVariable("id") Integer id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("productDetail");
        mv.addObject("productDetail",productService.getById(id));
        return mv;
    }
}

