package com.li.service;

import com.li.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lw
 * @since 2021-09-16
 */
public interface ProductService extends IService<Product> {

    List<Product> findByCategoryId(String type, Integer id);
}
