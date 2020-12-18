package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.po.BasicFormula;
import com.sophia.store.entity.po.Material;
import com.sophia.store.entity.po.MaterialFormula;
import com.sophia.store.entity.po.Middle;
import com.sophia.store.entity.vo.BasicFormulaVo;
import com.sophia.store.entity.vo.MaterialFormulaVo;
import com.sophia.store.entity.vo.MiddleVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MiddleServiceImpl extends BaseService implements MiddleService {

    public MiddleVo convert(Middle middle) {
        MiddleVo vo = new MiddleVo();
        vo.setCapacity(middle.getCapacity());
        vo.setName(middle.getName());
        vo.setPrice(middle.getPrice());
        vo.setMaterialFormulaVos(convertMaterialFormula(middle.getMaterialFormulaSet()));
        vo.setBasicFormulaVos(convertBasicFormula(middle.getBasicFormulaSet()));
        return vo;
    }


    @Override
    public List<MiddleVo> findAll() {
        List<MiddleVo> middleVos = new ArrayList<>();
        List<Middle> middles = middleDao.findAll();
        middles.forEach(middle -> middleVos.add(convert(middle)));
        return middleVos;
    }

    @Override
    public List<MiddleVo> find(Pageable pageable, String name) {
        List<MiddleVo> middleVos = new ArrayList<>();
        Page<Middle> middles = middleDao.findAll((Specification<Middle>) (root, query, criteriaBuilder) -> {
            // 1. 创建集合 存储查询条件
            List<Predicate> queryList = new ArrayList<>();
            // 2. 添加查询条件
            if (StringsUtils.isNotEmpty(name)) {
                queryList.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            query.where(queryList.toArray(new Predicate[0]));
            return null;
        }, pageable);
        middles.forEach(middle -> middleVos.add(convert(middle)));
        return middleVos;
    }

    @Override
    public long findAllCount() {
        return middleDao.count();
    }

    @Override
    public long findCountByName(String name) {
        return middleDao.findByNameLike(name).size();
    }

    @Override
    public MiddleVo add(MiddleVo vo) {
        return null;
    }

    @Override
    public MiddleVo update(MiddleVo vo) {
        return null;
    }

    @Override
    public MiddleVo delete(MiddleVo vo) {
        Optional<Middle> optionalMiddle = middleDao.findById(vo.getId());
        Middle middle = optionalMiddle.orElseGet(optionalMiddle::get);
        Set<BasicFormula> basicFormulaSet = middle.getBasicFormulaSet();
        basicFormulaSet.forEach(formula -> basicFormulaDao.delete(formula));
        Set<MaterialFormula> materialFormulaSet = middle.getMaterialFormulaSet();
        materialFormulaSet.forEach(formula -> materialFormulaDao.delete(formula));
        return null;
    }
}
