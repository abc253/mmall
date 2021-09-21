package com.li.service.impl;

import com.li.entity.Product;
import com.li.mapper.ProductMapper;
import com.li.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lw
 * @since 2021-09-16
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Resource
    private ProductMapper productMapper;


    @Override
    public List<Product> findByCategoryId(String type, Integer id) {
        Map<String,Object> map = new HashMap<>();
        map.put("categorylevel"+type+"_id",id);
        return productMapper.selectByMap(map);
    }
}
