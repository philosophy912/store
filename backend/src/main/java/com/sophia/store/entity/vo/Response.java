package com.sophia.store.entity.vo;

import com.sophia.store.utils.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lizhe
 * @date 2020/11/20 14:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "返回消息实体")
public class Response {
    @ApiModelProperty(value = "响应代码", example = "20000")
    private Integer code = Constant.OK;
    @ApiModelProperty(value = "响应消息", example = "添加成功")
    private String message;
    @ApiModelProperty(value = "响应内容", example = "返回结果的数组")
    private Object data;
    @ApiModelProperty(value = "错误信息", example = "返回错误的信息")
    private String errorInfo;


}
