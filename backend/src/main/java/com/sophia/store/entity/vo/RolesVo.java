package com.sophia.store.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author lizhe
 * @date 2020/11/20 15:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "角色显示对象")
public class RolesVo {
    @ApiModelProperty(value = "角色对象集合", example = "[admin]")
    private List<String> roles;
    @ApiModelProperty(value = "角色介绍", example = "管理员")
    private String introduction;
    @ApiModelProperty(value = "角色头像")
    private String avatar;
    @ApiModelProperty(value = "角色名称", example = "管理员")
    private String name;
}
