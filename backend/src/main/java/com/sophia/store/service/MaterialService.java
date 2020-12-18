package com.sophia.store.service;

import com.sophia.store.entity.vo.CategoryVo;
import com.sophia.store.entity.vo.MaterialVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaterialService {
    List<MaterialVo> findAll();

    List<MaterialVo> find(Pageable pageable, String name);

    long findAllCount();

    long findCountByName(String s);

    MaterialVo add(MaterialVo vo);

    MaterialVo update(MaterialVo vo);

    MaterialVo delete(MaterialVo vo);
}
