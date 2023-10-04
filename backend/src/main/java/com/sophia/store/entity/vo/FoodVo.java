package com.sophia.store.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FoodVo {
    private Integer id;
    private String name;
    private Float count;
    private Float restCount;
    private Long inDate;
    private Long expireDate;
    private String imageUrl;
    private String description;
    private Integer categoryId;
    private String categoryName;
}
