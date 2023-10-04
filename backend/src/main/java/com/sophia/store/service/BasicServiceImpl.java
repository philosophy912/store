package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.po.Basic;
import com.sophia.store.entity.po.MaterialFormula;
import com.sophia.store.entity.vo.BasicVo;
import com.sophia.store.entity.vo.FormulaVo;
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
        Set<FormulaVo> formulaVos = convertMaterialFormulaVo(basic.getMaterialFormulaSet());
        log.debug("formulaVos is {}", formulaVos);
        vo.setFormulaVos(formulaVos);
        float price = 0f;
        for (FormulaVo formulaVo : formulaVos) {
            price += formulaVo.getCount() * formulaVo.getPrice();
        }
        log.debug("price is {}", price);
        vo.setPrice((float) price);
        vo.setPricePerUnit(vo.getPrice() / vo.getCapacity());
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
        log.debug("basicvos is {}", basicVos);
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
        if (basics.isEmpty()) {
            Basic basic = new Basic();
            basic.setName(vo.getName());
            basic.setCapacity(vo.getCapacity());
            basic.setUnit(vo.getUnit());
            Set<MaterialFormula> materialFormulas = convert2MaterialFormula(vo.getFormulaVos());
            materialFormulaDao.saveAll(materialFormulas);
            if (materialFormulas.isEmpty()) {
                String error = "必须存在一个原材料";
                throw new RuntimeException(error);
            }
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
        Set<MaterialFormula> materialFormulas = convert2MaterialFormula(vo.getFormulaVos());
        if (materialFormulas.isEmpty()) {
            String error = "必须包含至少一个原材料配方";
            throw new RuntimeException(error);
        }
        materialFormulaDao.saveAll(materialFormulas);
        basic.setMaterialFormulaSet(materialFormulas);
        Basic dpt = basicDao.saveAndFlush(basic);
        return convert(dpt);
    }


    @Override
    public BasicVo delete(BasicVo vo) {
        Optional<Basic> optionalBasic = basicDao.findById(vo.getId());
        Basic basic = optionalBasic.orElseGet(optionalBasic::get);
        Set<MaterialFormula> materialFormulaSet = basic.getMaterialFormulaSet();
        materialFormulaDao.deleteAll(materialFormulaSet);
        basicDao.delete(basic);
        return vo;
    }
}
