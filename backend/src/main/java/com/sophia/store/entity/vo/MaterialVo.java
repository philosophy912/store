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
@ApiModel(value = "原材料显示实体")
public class MaterialVo {

    @ApiModelProperty(value = "编号")
    private Integer id;
    @ApiModelProperty(value = "名字")
    private String name;
    @ApiModelProperty(value = "容量")
    private Integer capacity;
    @ApiModelProperty(value = "单位")
    private String unit;
    @ApiModelProperty(value = "单价")
    private Float price;
    @ApiModelProperty(value = "单位价格")
    private Float pricePerUnit;
}
