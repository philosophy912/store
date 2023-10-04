package com.sophia.store.entity.vo;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageResponse extends Response {
    private int pageSize = 20;
    private int totalRows = 0;
    private int totalPages = 0;
}
