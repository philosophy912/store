/**
 * @author lizhe
 * @date 2020/12/9 14:57
 **/
package com.sophia.store.entity.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "need_expire", nullable = false)
    private Boolean needExpire;
    @OneToMany(mappedBy = "category",cascade = {CascadeType.MERGE, CascadeType.REFRESH},fetch = FetchType.EAGER)
    private Set<Food> foods = new HashSet<>();

}
