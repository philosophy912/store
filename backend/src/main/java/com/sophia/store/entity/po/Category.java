package com.sophia.store.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lizhe
 * @date 2020/12/9 14:57
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Categoty")
@ApiModel(value = "一级分类")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "序号")
    private Integer id;
    @Column(name = "name", nullable = false)
    @ApiModelProperty(value = "一级分类名")
    private String name;
    @Column(name = "need_expire", nullable = false)
    @ApiModelProperty(value = "是否需要填写过期日期")
    private Boolean needExpire;
    @OneToMany(mappedBy = "category", cascade={CascadeType.MERGE, CascadeType.REFRESH},fetch= FetchType.LAZY)
    @ApiModelProperty(value = "分类下所有的材料")
    private Set<Food> foods = new HashSet<>();
}
