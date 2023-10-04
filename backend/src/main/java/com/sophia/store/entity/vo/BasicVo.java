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
public class BasicVo {

    private Integer id;
    private String name;
    private String unit;
    private Integer capacity;
    private Float price;
    private Set<FormulaVo> formulaVos;
    private Float pricePerUnit;
}
