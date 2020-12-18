package com.sophia.store.service;

import com.sophia.store.entity.vo.MiddleVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MiddleService {
    List<MiddleVo> findAll();

    List<MiddleVo> find(Pageable pageable, String name);

    long findAllCount();

    long findCountByName(String s);

    MiddleVo add(MiddleVo vo);

    MiddleVo update(MiddleVo vo);

    MiddleVo delete(MiddleVo vo);
}
