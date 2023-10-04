/**
 * @author lizhe
 * @date 2020/3/11 9:20
 **/
package com.sophia.store.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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


@Setter
@Getter
@Entity
@Table(name = "MATERIAL")
@ToString
@JsonIgnoreProperties(value = {"handler"})
public class Material implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    @Column(name = "unit", nullable = false)
    private String unit;
    @Column(name = "price", nullable = false)
    private Float price;
    @Column(name = "price_per_unit", nullable = false)
    private Float pricePerUnit;

}
