package com.sophia.store.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasicFormulaVo {

    private Integer id;
    private Float count;
    private Integer basicId;
    private String basicName;
}
