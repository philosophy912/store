package com.sophia.store.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "初级产品显示实体")
public class BasicVo {

    @ApiModelProperty(value = "编号")
    private Integer id;
    @ApiModelProperty(value = "名字")
    private String name;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "总量")
    private Integer capacity;
    @ApiModelProperty(value = "总价")
    private Float price;
    @ApiModelProperty(value = "包含的原材料")
    private Set<FormulaVo> formulaVos;
    @ApiModelProperty(value = "单位价格")
    private Float pricePerUnit;
}
