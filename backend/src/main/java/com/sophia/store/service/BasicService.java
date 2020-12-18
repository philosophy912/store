package com.sophia.store.service;

import com.sophia.store.entity.vo.BasicVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BasicService {

    List<BasicVo> findAll();

    List<BasicVo> find(Pageable pageable, String name);

    long findAllCount();

    long findCountByName(String s);

    BasicVo add(BasicVo vo);

    BasicVo update(BasicVo vo);

    BasicVo delete(BasicVo vo);
}
