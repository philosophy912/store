/**
 * @author lizhe
 * @date 2020/3/11 9:21
 **/
package com.sophia.store.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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


@Setter
@Getter
@Entity
@Table(name = "BASIC")
@ToString
@JsonIgnoreProperties(value = {"handler"})
public class Basic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "unit")
    private String unit;
    @Column(name = "capacity")
    private Integer capacity;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "basic_id")
    private Set<MaterialFormula> materialFormulaSet = new HashSet<>();

}
