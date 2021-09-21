package com.li.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.vo.CartVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lw
 * @since 2021-09-16
 */
public interface CartService extends IService<Cart> {
    List<CartVO> findAllCartVO(Integer userId);
}
