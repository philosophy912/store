/**
 * @author lizhe
 * @date 2020/11/20 14:56
 **/
package com.sophia.store.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVo {
    private Integer id;
    private String username;
    private String password;
}
