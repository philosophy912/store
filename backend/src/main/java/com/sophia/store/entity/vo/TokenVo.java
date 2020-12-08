package com.sophia.store.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lizhe
 * @date 2020/11/20 15:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "Token显示对象")
public class TokenVo {
    @ApiModelProperty(value = "token值")
    private String token;
}
