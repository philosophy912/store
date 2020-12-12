package com.sophia.store.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lizhe
 * @date 2020/3/11 9:21
 **/
@Setter
@Getter
@Entity
@Table(name = "BASIC")
@ToString
@JsonIgnoreProperties(value = {"handler"})
public class Basic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "编号")
    private Integer id;
    @Column(name = "name", nullable = false)
    @ApiModelProperty(value = "名字")
    private String name;
    @Column(name = "unit")
    @ApiModelProperty(value = "单位")
    private String unit;
    @Column(name = "capacity")
    @ApiModelProperty(value = "总量")
    private Float capacity;
    @Column(name = "price", nullable = false)
    @ApiModelProperty(value = "总价")
    private Float price;
    @OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "basic_id")
    @ApiModelProperty(value = "包含的原材料")
    private Set<MaterialFormula> materialFormulaSet = new HashSet<>();

}
