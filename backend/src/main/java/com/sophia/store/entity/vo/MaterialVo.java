package com.sophia.store.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MaterialVo {

    private Integer id;
    private String name;
    private Integer capacity;
    private String unit;
    private Float price;
    private Float pricePerUnit;
}
