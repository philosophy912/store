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
@ApiModel(value = "返回分页消息实体")
public class PageResponse extends Response {
    @ApiModelProperty(value = "分页大小", example = "20")
    private int pageSize = 20;
    @ApiModelProperty(value = "总消息数", example = "100")
    private int totalRows = 0;
    @ApiModelProperty(value = "总页面数", example = "200")
    private int totalPages = 0;
}
