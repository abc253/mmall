package com.li.service;

import com.li.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;
import com.li.vo.AddressVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lw
 * @since 2021-09-16
 */
public interface OrderService extends IService<Orders> {
    boolean save(Orders orders, AddressVo addressVo);
}
