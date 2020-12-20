package com.sophia.store.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "配方显示实体")
public class FormulaVo {

    @ApiModelProperty(value = "材料编号")
    private Integer id;
    @ApiModelProperty(value = "材料数量")
    private Integer count;
    @ApiModelProperty(value = "材料类型")
    private String type;
    @ApiModelProperty(value = "材料名字")
    private String name;
    @ApiModelProperty(value = "材料价格")
    private Float price;
    @ApiModelProperty(value = "MaterialFormula编号")
    private Integer materialFormulaId;
    @ApiModelProperty(value = "BasicFormula编号")
    private Integer basicFormulaId;

}
