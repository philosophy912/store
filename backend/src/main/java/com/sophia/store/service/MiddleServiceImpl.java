package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.po.BasicFormula;
import com.sophia.store.entity.po.MaterialFormula;
import com.sophia.store.entity.po.Middle;
import com.sophia.store.entity.vo.FormulaVo;
import com.sophia.store.entity.vo.MiddleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;

@Service
@Slf4j
public class MiddleServiceImpl extends BaseService implements MiddleService {

    public MiddleVo convert(Middle middle) {
        MiddleVo vo = new MiddleVo();
        vo.setId(middle.getId());
        vo.setCapacity(middle.getCapacity());
        String name = middle.getName();
        log.debug("middle name is {}", name);
        vo.setName(name);
        vo.setUnit(middle.getUnit());
        Set<FormulaVo> formulaVos = new LinkedHashSet<>();
        Set<FormulaVo> materialFormulaVos = convertMaterialFormulaVo(middle.getMaterialFormulaSet());
        Set<FormulaVo> basicFormulaVos = convertBasicFormulaVo(middle.getBasicFormulaSet());
        formulaVos.addAll(materialFormulaVos);
        formulaVos.addAll(basicFormulaVos);
        vo.setFormulaVos(formulaVos);
        log.debug("formula vos is {}", formulaVos);
        float price = 0;
        for (FormulaVo formulaVo : formulaVos) {
            price += formulaVo.getPrice() * formulaVo.getCount();
        }
        vo.setPrice(price);
        vo.setPricePerUnit(vo.getPrice() / vo.getCapacity());
        return vo;
    }


    @Override
    public List<MiddleVo> findAll() {
        List<MiddleVo> middleVos = new LinkedList<>();
        List<Middle> middles = middleDao.findAll();
        middles.forEach(middle -> middleVos.add(convert(middle)));
        return middleVos;
    }

    @Override
    public List<MiddleVo> find(Pageable pageable, String name) {
        List<MiddleVo> middleVos = new LinkedList<>();
        Page<Middle> middles = middleDao.findAll((Specification<Middle>) (root, query, criteriaBuilder) -> {
            // 1. 创建集合 存储查询条件
            List<Predicate> queryList = new LinkedList<>();
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
        log.debug("name = {}", vo.getName());
        List<Middle> middles = middleDao.findByName(vo.getName());
        if (middles.isEmpty()) {
            Middle middle = new Middle();
            middle.setName(vo.getName());
            middle.setCapacity(vo.getCapacity());
            middle.setUnit(vo.getUnit());
            Set<MaterialFormula> materialFormulas = convertMaterialFormula(vo.getFormulaVos());
            Set<BasicFormula> basicFormulas = convertBasicFormula(vo.getFormulaVos());
            materialFormulaDao.saveAll(materialFormulas);
            basicFormulaDao.saveAll(basicFormulas);
            if (materialFormulas.isEmpty() && basicFormulas.isEmpty()) {
                String error = "原材料和基础产品必须有一个";
                throw new RuntimeException(error);
            }
            middle.setMaterialFormulaSet(materialFormulas);
            middle.setBasicFormulaSet(basicFormulas);
            Middle dpt = middleDao.saveAndFlush(middle);
            return convert(dpt);
        }
        return null;
    }

    @Override
    public MiddleVo update(MiddleVo vo) {
        Optional<Middle> optionalMiddle = middleDao.findById(vo.getId());
        Middle middle = optionalMiddle.orElseGet(optionalMiddle::get);
        middle.setName(vo.getName());
        middle.setCapacity(vo.getCapacity());
        middle.setUnit(vo.getUnit());
        Set<MaterialFormula> materialFormulas = convertMaterialFormula(vo.getFormulaVos());
        Set<BasicFormula> basicFormulas = convertBasicFormula(vo.getFormulaVos());
        if (materialFormulas.isEmpty() && basicFormulas.isEmpty()) {
            String error = "必须包含至少一个原材料或者基础材料配方";
            throw new RuntimeException(error);
        }
        materialFormulaDao.saveAll(materialFormulas);
        basicFormulaDao.saveAll(basicFormulas);
        middle.setMaterialFormulaSet(materialFormulas);
        middle.setBasicFormulaSet(basicFormulas);
        Middle dpt = middleDao.saveAndFlush(middle);
        log.debug("dpt is {}", dpt);
        return convert(dpt);
    }

    @Override
    public MiddleVo delete(MiddleVo vo) {
        Optional<Middle> optionalMiddle = middleDao.findById(vo.getId());
        Middle middle = optionalMiddle.orElseGet(optionalMiddle::get);
        Set<BasicFormula> basicFormulaSet = middle.getBasicFormulaSet();
        basicFormulaDao.deleteAll(basicFormulaSet);
        Set<MaterialFormula> materialFormulaSet = middle.getMaterialFormulaSet();
        materialFormulaDao.deleteAll(materialFormulaSet);
        middleDao.delete(middle);
        return vo;
    }
}
