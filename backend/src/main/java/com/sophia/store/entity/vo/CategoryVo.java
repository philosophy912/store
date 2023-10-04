package com.sophia.store.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryVo {
    private Integer id;
    private String name;
    private Set<FoodVo> foods;
    private Boolean needExpire;
}
