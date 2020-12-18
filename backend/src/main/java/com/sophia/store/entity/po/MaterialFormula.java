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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author lizhe
 * @date 2020/3/11 9:20
 **/
@Setter
@Getter
@Entity
@Table(name = "MATERIAL_FORMULA")
@ToString
@JsonIgnoreProperties(value = {"handler"})
public class MaterialFormula implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "编号")
    private Integer id;
    @Column(name = "count", nullable = false)
    @ApiModelProperty(value = "数量")
    private Float count;
    @OneToOne(fetch = FetchType.EAGER, cascade={CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @ApiModelProperty(value = "原材料")
    private Material material;

}
