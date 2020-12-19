package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.po.Basic;
import com.sophia.store.entity.po.Material;
import com.sophia.store.entity.po.MaterialFormula;
import com.sophia.store.entity.vo.BasicVo;
import com.sophia.store.entity.vo.MaterialFormulaVo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BasicServiceImpl extends BaseService implements BasicService {

    private BasicVo convert(Basic basic) {
        BasicVo vo = new BasicVo();
        log.debug("basic id = {}", basic.getId());
        vo.setId(basic.getId());
        vo.setCapacity(basic.getCapacity());
        vo.setName(basic.getName());
        vo.setUnit(basic.getUnit());
        Set<MaterialFormulaVo> materialFormulaVos = convertMaterialFormulaVo(basic.getMaterialFormulaSet());
        vo.setMaterialFormulaVos(materialFormulaVos);
        float price = 0;
        for (MaterialFormulaVo formulaVo : materialFormulaVos) {
            Optional<Material> optionalMaterial = materialDao.findById(formulaVo.getMaterialId());
            Material material = optionalMaterial.orElseGet(optionalMaterial::get);
            float formulaPrice = formulaVo.getCount() * material.getPricePerUnit();
            log.debug("material id = {}, price = {}, pricePerUnit = {}", material.getId(), material.getPrice(), material.getPricePerUnit());
            log.debug("formula count = {}", formulaVo.getCount());
            log.debug("formula price = {}", formulaPrice);
            price += formulaPrice;

        }
        vo.setPrice(price);
        vo.setPricePerUnit(price / vo.getCapacity());
        return vo;
    }


    @Override
    public List<BasicVo> findAll() {
        List<BasicVo> basicVos = new ArrayList<>();
        List<Basic> basics = basicDao.findAll();
        basics.forEach(basic -> basicVos.add(convert(basic)));
        return basicVos;
    }

    @Override
    public List<BasicVo> find(Pageable pageable, String name) {
        List<BasicVo> basicVos = new ArrayList<>();
        Page<Basic> basics = basicDao.findAll((Specification<Basic>) (root, query, criteriaBuilder) -> {
            // 1. 创建集合 存储查询条件
            List<Predicate> queryList = new ArrayList<>();
            // 2. 添加查询条件
            if (StringsUtils.isNotEmpty(name)) {
                queryList.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            query.where(queryList.toArray(new Predicate[0]));
            return null;
        }, pageable);
        basics.forEach(basic -> basicVos.add(convert(basic)));
        return basicVos;
    }

    @Override
    public long findAllCount() {
        return basicDao.count();
    }

    @Override
    public long findCountByName(String name) {
        return basicDao.findByNameLike(name).size();
    }

    @Override
    public BasicVo add(BasicVo vo) {
        List<Basic> basics = basicDao.findByName(vo.getName());
        if (basics.size() == 0) {
            Basic basic = new Basic();
            basic.setName(vo.getName());
            basic.setCapacity(vo.getCapacity());
            basic.setUnit(vo.getUnit());
            Set<MaterialFormula> materialFormulas = convertMaterialFormula(vo.getMaterialFormulaVos());
            materialFormulas.forEach(formula -> materialFormulaDao.save(formula));
            basic.setMaterialFormulaSet(materialFormulas);
            Basic dpt = basicDao.saveAndFlush(basic);
            return convert(dpt);
        }
        return null;
    }

    @Override
    public BasicVo update(BasicVo vo) {
        Optional<Basic> optionalBasic = basicDao.findById(vo.getId());
        Basic basic = optionalBasic.orElseGet(optionalBasic::get);
        basic.setName(vo.getName());
        basic.setCapacity(vo.getCapacity());
        basic.setUnit(vo.getUnit());
        Set<MaterialFormula> materialFormulas = convertMaterialFormula(vo.getMaterialFormulaVos());
        if (materialFormulas.size() < 1) {
            String error = "必须包含至少一个原材料配方";
            throw new RuntimeException(error);
        }
        materialFormulas.forEach(formula -> materialFormulaDao.save(formula));
        basic.setMaterialFormulaSet(materialFormulas);
        Basic dpt = basicDao.saveAndFlush(basic);
        return convert(dpt);
    }

    @Override
    public BasicVo delete(BasicVo vo) {
        Optional<Basic> optionalBasic = basicDao.findById(vo.getId());
        Basic basic = optionalBasic.orElseGet(optionalBasic::get);
        Set<MaterialFormula> materialFormulaSet = basic.getMaterialFormulaSet();
        for (MaterialFormula formula : materialFormulaSet) {
            materialFormulaDao.delete(formula);
        }
        basicDao.delete(basic);
        return vo;
    }
}
