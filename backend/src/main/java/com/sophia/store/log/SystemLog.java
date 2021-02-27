package com.sophia.store.log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "SYSTEMLOG")
@JsonIgnoreProperties(value = {"handler"})
public class SystemLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "编号")
    private Long id;
    @Column(name = "username", nullable = false)
    @ApiModelProperty(value = "操作者名字")
    private String username;
    @Column(name = "operation", nullable = false)
    @ApiModelProperty(value = "操作类型")
    private String operation;
    @Column(name = "method", nullable = false)
    @ApiModelProperty(value = "方法")
    private String method;
    @Column(name = "params", nullable = false)
    @ApiModelProperty(value = "参数")
    private String params;
    @Column(name = "ipAddress", nullable = false)
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;
    @Column(name = "time", nullable = false)
    @ApiModelProperty(value = "操作耗时")
    private Long time;
    @Column(name = "createDate", nullable = false)
    @ApiModelProperty(value = "创建时间")
    private Date createDate;
}
