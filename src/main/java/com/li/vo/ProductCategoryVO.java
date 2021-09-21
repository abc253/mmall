package com.li.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryVO {
    private Integer id;
    private String name;
    private String topImage;
    private String bannerImage;
    private List<ProductCategoryVO> children;
    private List<ProductVO> productVOList;

    public ProductCategoryVO() {
    }

    public ProductCategoryVO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
