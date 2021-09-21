package com.li.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 因为使用了thymeleaf模板，用户不能直接去访问静态页面，只能通过controller去跳转到相应的页面
 * 故直接建立一个controller，用来实现用户访问静态页面
 */
@Controller
public class PageVisitController {

    @RequestMapping("/{url}")
    public String pageVisit(@PathVariable("url") String url) {
        return url;
    }


    /*
    忽略favicon.ico的解决方案
     */
    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

    /*
     *直接访问订单生成页面是不合理的
     */
}
