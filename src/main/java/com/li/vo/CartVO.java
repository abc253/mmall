package com.li.vo;

import lombok.Data;

@Data
public class CartVO {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private Integer stock;
    private String name;
    private Float cost;
    private Float price;
    private String fileName;
}
