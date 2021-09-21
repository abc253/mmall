package com.li.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.*;
import com.li.mapper.*;
import com.li.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.vo.AddressVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lw
 * @since 2021-09-16
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private UserAddressMapper userAddressMapper;
    @Resource
    private OrderDetailMapper orderDetailMapper;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private HttpSession session;

    @Transactional
    @Override
    public boolean save(Orders orders, AddressVo addressVo) {
        //判断是否是新地址
        if("newAddress".equals(addressVo.getSelectAddress())) {
            //把新地址存入数据库
            UserAddress userAddress = new UserAddress();
            BeanUtils.copyProperties(addressVo,userAddress);
            userAddress.setId(orders.getUserId());
            userAddress.setIsdefault(Integer.valueOf(1));
            //存入数据库
            userAddressMapper.insert(userAddress);
            //给订单附上新地址
            orders.setUserAddress(addressVo.getAddress());
        }
        //存储orders
        String seriaNumber = null;
        try {
            StringBuffer result = new StringBuffer();
            for(int i=0;i<32;i++) {
                result.append(Integer.toHexString(new Random().nextInt(16)));
            }
            seriaNumber =  result.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        orders.setSerialnumber(seriaNumber);
        //orders存入数据库
        orderMapper.insert(orders);

        //存储ordersdetail
        Integer stock = null;
        Integer quantity = null;
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id",orders.getUserId());
        List<Cart> cartList = cartMapper.selectList(wrapper);
        for (Cart cart : cartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setId(null);
            orderDetail.setOrderId(orders.getId());
            //product库存减少
            Product product = productMapper.selectById(orderDetail.getProductId());
            quantity = orderDetail.getQuantity();
            stock = product.getStock() -quantity;
            product.setStock(stock);
            productMapper.updateById(product);
            orderDetailMapper.insert(orderDetail);
        }

        //清空购物车
        wrapper = new QueryWrapper();
        wrapper.eq("user_id",orders.getUserId());
        cartMapper.delete(wrapper);

        //把session域中的cartList重置
        session.setAttribute("cartList",null);
        return true;
    }
}
