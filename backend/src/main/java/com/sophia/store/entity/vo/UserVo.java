package com.sophia.store.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lizhe
 * @date 2020/11/20 14:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "用户显示对象")
public class UserVo {
    @ApiModelProperty(value = "序号")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
}
