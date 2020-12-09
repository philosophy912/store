package com.sophia.store.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "耗材显示实体")
public class FoodVo {
    @ApiModelProperty(value = "序号")
    private Integer id;
    @ApiModelProperty(value = "耗材名称", required = true)
    private String name;
    @ApiModelProperty(value = "总数量")
    private Float count;
    @ApiModelProperty(value = "剩余数量")
    private Float restCount;
    @ApiModelProperty(value = "购买日期")
    private Long inDate;
    @ApiModelProperty(value = "过期日期")
    private Long expireDate;
    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;
    @ApiModelProperty(value = "分类名称")
    private Integer categoryName;
}
