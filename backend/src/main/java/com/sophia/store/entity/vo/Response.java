/**
 * @author lizhe
 * @date 2020/11/20 14:57
 **/
package com.sophia.store.entity.vo;

import com.sophia.store.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response {
    private Integer code = Constant.OK;
    private String message;
    private Object data;
    private String errorInfo;

}
