package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.po.Basic;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MiddleServiceImpl extends BaseService implements MiddleService {

    public MiddleVo convert(Middle middle) {
        MiddleVo vo = new MiddleVo();
        vo.setId(middle.getId());
        vo.setCapacity(middle.getCapacity());
        vo.setName(middle.getName());
        vo.setUnit(middle.getUnit());
        Set<MaterialFormulaVo> materialFormulaVos = convertMaterialFormulaVo(middle.getMaterialFormulaSet());
        Set<BasicFormulaVo> basicFormulaVos = convertBasicFormulaVo(middle.getBasicFormulaSet());
        vo.setMaterialFormulaVos(materialFormulaVos);
        vo.setBasicFormulaVos(basicFormulaVos);
        float price = 0;
        for(MaterialFormulaVo formulaVo: materialFormulaVos){
            Optional<Material> optionalMaterial = materialDao.findById(formulaVo.getMaterialId());
            Material material = optionalMaterial.orElseGet(optionalMaterial::get);
            price += material.getPricePerUnit() * formulaVo.getCount();
        }
        for(BasicFormulaVo formulaVo: basicFormulaVos){
            float basicPrice = 0;
            Optional<Basic> optionalBasic = basicDao.findById(formulaVo.getBasicId());
            Basic basic = optionalBasic.orElseGet(optionalBasic::get);
            Set<MaterialFormula> materialFormulaSet = basic.getMaterialFormulaSet();
            for(MaterialFormula formula: materialFormulaSet){
                // 单个Basic的总价
                basicPrice += formula.getCount() * formula.getMaterial().getPricePerUnit();
            }
            // basic的每单位价格
            price += formulaVo.getCount() * (basicPrice / basic.getCapacity());
        }
        vo.setPrice(price);
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
        List<Middle> middles = middleDao.findByName(vo.getName());
        if(middles.size() == 0){
            Middle middle = new Middle();
            middle.setName(vo.getName());
            middle.setCapacity(vo.getCapacity());
            middle.setUnit(vo.getUnit());
            Set<MaterialFormula> materialFormulas = convertMaterialFormula(vo.getMaterialFormulaVos());
            Set<BasicFormula> basicFormulas = convertBasicFormula(vo.getBasicFormulaVos());
            materialFormulas.forEach(formula -> materialFormulaDao.save(formula));
            basicFormulas.forEach(formula -> basicFormulaDao.save(formula));
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
        Set<MaterialFormula> materialFormulas = convertMaterialFormula(vo.getMaterialFormulaVos());
        Set<BasicFormula> basicFormulas = convertBasicFormula(vo.getBasicFormulaVos());
        if (!(materialFormulas.size() > 0 || basicFormulas.size() > 0)) {
            String error = "必须包含至少一个原材料或者基础材料配方";
            throw new RuntimeException(error);
        }
        materialFormulas.forEach(formula -> materialFormulaDao.save(formula));
        basicFormulas.forEach(formula -> basicFormulaDao.save(formula));
        middle.setMaterialFormulaSet(materialFormulas);
        middle.setBasicFormulaSet(basicFormulas);
        Middle dpt = middleDao.saveAndFlush(middle);
        return convert(dpt);
    }

    @Override
    public MiddleVo delete(MiddleVo vo) {
        Optional<Middle> optionalMiddle = middleDao.findById(vo.getId());
        Middle middle = optionalMiddle.orElseGet(optionalMiddle::get);
        Set<BasicFormula> basicFormulaSet = middle.getBasicFormulaSet();
        basicFormulaSet.forEach(formula -> basicFormulaDao.delete(formula));
        Set<MaterialFormula> materialFormulaSet = middle.getMaterialFormulaSet();
        materialFormulaSet.forEach(formula -> materialFormulaDao.delete(formula));
        middleDao.delete(middle);
        return vo;
    }
}
