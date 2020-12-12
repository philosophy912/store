package com.sophia.store.entity.vo;

import com.sophia.store.entity.po.Food;
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
@ApiModel(value = "分类显示实体")
public class CategoryVo {
    @ApiModelProperty(value = "序号")
    private Integer id;
    @ApiModelProperty(value = "设备名称", required = true)
    private String name;
    @ApiModelProperty(value = "耗材列表")
    private Set<FoodVo> foods;
    @ApiModelProperty(value = "是否需要填写过期日期")
    private Boolean needExpire;
}
