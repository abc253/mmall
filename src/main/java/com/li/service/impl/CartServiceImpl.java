package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Cart;
import com.li.entity.Product;
import com.li.mapper.CartMapper;
import com.li.mapper.ProductMapper;
import com.li.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.vo.CartVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lw
 * @since 2021-09-16
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Resource
    private CartMapper cartMapper;
    @Resource
    private ProductMapper productMapper;


    @Transactional
    @Override
    public List<CartVO> findAllCartVO(Integer userId) {
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);

        List<CartVO> cartVOList = new ArrayList<>();
        List<Cart> cartList = cartMapper.selectList(wrapper);

        for(Cart cart : cartList) {
            CartVO cartVO = new CartVO();
            //通过cart中的productId查询product
            Product product = productMapper.selectById(cart.getProductId());
            //把product对应的cartVO属性赋值
            BeanUtils.copyProperties(product,cartVO);
            //把cart的属性赋给cartVO
            BeanUtils.copyProperties(cart,cartVO);
            //把对象cartVO添加到集合中
            cartVOList.add(cartVO);
        }

        return cartVOList;
    }
}
