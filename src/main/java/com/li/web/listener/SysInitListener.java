package com.li.web.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.li.entity.Product;
import com.li.entity.ProductCategory;
import com.li.mapper.ProductCategoryMapper;
import com.li.mapper.ProductMapper;
import com.li.service.ProductCategoryService;
import com.li.service.impl.ProductCategoryServiceImpl;
import com.li.vo.ProductCategoryVO;
import com.li.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SysInitListener implements ApplicationListener<ContextRefreshedEvent> {

    private ProductCategoryMapper categoryMapper = null;
    private ProductMapper productMapper = null;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("---> 初始化数据字典！！");
        //获取context域和web的ioc容器
        ApplicationContext applicationContext = event.getApplicationContext();

        categoryMapper = applicationContext.getBean(ProductCategoryMapper.class);
        productMapper = applicationContext.getBean(ProductMapper.class);

        //查询所有的ProductCategoryVO，并放入数据字典中
        if(categoryMapper != null) {
            List<ProductCategoryVO> allProductCategoryVO = getAllProductCategoryVO();

            ServletContext context = applicationContext.getBean(ServletContext.class);
            context.setAttribute("list",allProductCategoryVO);
            log.info("---> 数据字典初始化结束！！");
        } else {
            log.info("---> 数据字典初始化失败！！");
        }

    }


    public List<ProductCategoryVO> getAllProductCategoryVO() {
        //条件构造
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type",1);

        //查询类型为1的商品
        List<ProductCategory> levelOne = categoryMapper.selectList(wrapper);
        //快速转为ProductCategoryVO
        List<ProductCategoryVO> levelOneVO = levelOne.stream().map(e -> new ProductCategoryVO(e.getId(), e.getName()))
                .collect(Collectors.toList());
        //图片赋值
        //商品信息赋值
        for(int i=0;i<levelOneVO.size();i++) {
            levelOneVO.get(i).setBannerImage("/images/banner"+i+".png");
            levelOneVO.get(i).setTopImage("/images/top"+i+".png");

            wrapper = new QueryWrapper();
            wrapper.eq("categorylevelone_id",levelOneVO.get(i).getId());

            //查询商品信息
            List<Product> productList = productMapper.selectList(wrapper);
            List<ProductVO> productVOList = productList.stream().
                    map(e -> new ProductVO(
                            e.getId(),
                            e.getName(),
                            e.getPrice(),
                            e.getFileName()
                    )).collect(Collectors.toList());

            levelOneVO.get(i).setProductVOList(productVOList);
        }

        //获取三层的商品类别
        for(ProductCategoryVO productCategoryOneVO : levelOneVO) {
            wrapper = new QueryWrapper();
            wrapper.eq("type",2);
            wrapper.eq("parent_id",productCategoryOneVO.getId());

            //查询类型为2且上一级(parent_id)的type为1的商品
            List<ProductCategory> levelTwo = categoryMapper.selectList(wrapper);

            List<ProductCategoryVO> levelTwoVO = levelTwo.stream().map(e -> new ProductCategoryVO(e.getId(), e.getName()))
                    .collect(Collectors.toList());
            //关联levelOneVo
            productCategoryOneVO.setChildren(levelTwoVO);
            for(ProductCategoryVO productCategoryTwoVO : levelTwoVO) {
                wrapper = new QueryWrapper();
                wrapper.eq("type",3);
                wrapper.eq("parent_id",productCategoryTwoVO.getId());

                //查询类型为2且上一级(parent_id)的type为1的商品
                List<ProductCategory> levelThree = categoryMapper.selectList(wrapper);

                List<ProductCategoryVO> levelThreeVO = levelThree.stream().map(e -> new ProductCategoryVO(e.getId(), e.getName()))
                        .collect(Collectors.toList());
                //关联levelOneVo
                productCategoryTwoVO.setChildren(levelThreeVO);
            }
        }
        return levelOneVO;
    }
}
