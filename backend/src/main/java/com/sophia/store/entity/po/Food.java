/**
 * @author lizhe
 * @date 2020/12/9 15:09
 **/
package com.sophia.store.entity.po;

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


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "count", nullable = false)
    private Float count;
    @Column(name = "rest_count", nullable = false)
    private Float restCount;
    @Column(name = "in_date", nullable = false)
    private Long inDate;
    @Column(name = "expire_date")
    private Long expireDate;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "description")
    private String description;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "category_id")
    private Category category;


}
