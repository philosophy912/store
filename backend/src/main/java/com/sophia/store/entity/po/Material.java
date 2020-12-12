package com.sophia.store.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author lizhe
 * @date 2020/3/11 9:20
 **/
@Setter
@Getter
@Entity
@Table(name = "MATERIAL")
@ToString
@JsonIgnoreProperties(value = {"handler"})
@ApiModel
public class Material implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "编号")
    private Integer id;
    @Column(name = "name", nullable = false)
    @ApiModelProperty(value = "名字")
    private String name;
    @Column(name = "capacity", nullable = false)
    @ApiModelProperty(value = "容量")
    private Integer capacity;
    @Column(name = "unit", nullable = false)
    @ApiModelProperty(value = "单位")
    private String unit;
    @Column(name = "price", nullable = false)
    @ApiModelProperty(value = "单价")
    private Float price;
    @Column(name = "price_per_unit", nullable = false)
    @ApiModelProperty(value = "单位价格")
    private Float pricePerUnit;

}
