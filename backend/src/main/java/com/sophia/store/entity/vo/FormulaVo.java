package com.sophia.store.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FormulaVo {

    private Integer id;
    private Float count;
    private String type;
    private String name;
    private Float price;

}
