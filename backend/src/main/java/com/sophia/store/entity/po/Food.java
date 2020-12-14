package com.sophia.store.entity.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author lizhe
 * @date 2020/12/9 15:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Food")
@ApiModel(value = "食品")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "序号")
    private Integer id;
    @Column(name = "name", nullable = false)
    @ApiModelProperty(value = "一级分类名")
    private String name;
    @Column(name = "count", nullable = false)
    @ApiModelProperty(value = "总数量")
    private Float count;
    @Column(name = "rest_count", nullable = false)
    @ApiModelProperty(value = "剩余数量")
    private Float restCount;
    @Column(name = "in_date", nullable = false)
    @ApiModelProperty(value = "购买日期")
    private Long inDate;
    @Column(name = "expire_date")
    @ApiModelProperty(value = "过期日期")
    private Long expireDate;
    @Column(name = "image_url")
    @ApiModelProperty(value = "图片地址")
    private String imageUrl;
    @Column(name = "description")
    @ApiModelProperty(value = "相关描述")
    private String description;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "category_id")
    @ApiModelProperty(value = "类别")
    private Category category;


}
