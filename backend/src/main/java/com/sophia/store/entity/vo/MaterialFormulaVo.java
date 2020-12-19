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
@ApiModel(value = "原材料配方显示实体")
public class MaterialFormulaVo {

    @ApiModelProperty(value = "编号")
    private Integer id;
    @ApiModelProperty(value = "数量")
    private Integer count;
    @ApiModelProperty(value = "编号")
    private Integer materialId;
    @ApiModelProperty(value = "名字")
    private String materialName;
}
