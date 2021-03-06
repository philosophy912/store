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
@Table(name = "MIDDLE")
@ToString
@JsonIgnoreProperties(value = {"handler"})
public class Middle implements Serializable {

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
    private Integer capacity;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "middle_id")
    @ApiModelProperty(value = "原材料集合")
    private Set<MaterialFormula> materialFormulaSet = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "middle_id")
    @ApiModelProperty(value = "初级产品集合")
    private Set<BasicFormula> basicFormulaSet = new HashSet<>();

}
