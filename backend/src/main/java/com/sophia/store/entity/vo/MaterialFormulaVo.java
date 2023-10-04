package com.sophia.store.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MaterialFormulaVo {

    private Integer id;
    private Float count;
    private Integer materialId;
    private String materialName;
}
