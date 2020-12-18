package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.po.Basic;
import com.sophia.store.entity.po.Material;
import com.sophia.store.entity.po.MaterialFormula;
import com.sophia.store.entity.vo.BasicVo;
import com.sophia.store.entity.vo.MaterialFormulaVo;
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
public class BasicServiceImpl extends BaseService implements BasicService {

    private BasicVo convert(Basic basic) {
        BasicVo vo = new BasicVo();
        vo.setId(basic.getId());
        vo.setCapacity(basic.getCapacity());
        vo.setName(basic.getName());
        vo.setPrice(basic.getPrice());
        vo.setUnit(basic.getUnit());
        vo.setMaterialFormulaVos(convertMaterialFormula(basic.getMaterialFormulaSet()));
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
            Set<MaterialFormulaVo> materialFormulaVos = vo.getMaterialFormulaVos();
            float price = 0;
            Set<MaterialFormula> materialFormulaSet = new HashSet<>();
            for (MaterialFormulaVo materialFormulaVo : materialFormulaVos) {
                MaterialFormula materialFormula = new MaterialFormula();
                materialFormula.setCount(materialFormulaVo.getCount());
                Optional<Material> optionalMaterial = materialDao.findById(materialFormulaVo.getMaterialId());
                Material material = optionalMaterial.orElseGet(optionalMaterial::get);
                materialFormula.setMaterial(material);
                materialFormulaSet.add(materialFormula);
                price += materialFormula.getCount() * materialFormula.getMaterial().getPrice();
            }
            basic.setMaterialFormulaSet(materialFormulaSet);
            basic.setPrice(price);
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
        Set<MaterialFormulaVo> materialFormulaVos = vo.getMaterialFormulaVos();
        float price = 0;
        Set<MaterialFormula> materialFormulaSet = new HashSet<>();
        for (MaterialFormulaVo materialFormulaVo : materialFormulaVos) {
            Optional<Material> optionalMaterial = materialDao.findById(materialFormulaVo.getMaterialId());
            Material material = optionalMaterial.orElseGet(optionalMaterial::get);
            Optional<MaterialFormula> optionalMaterialFormula = materialFormulaDao.findById(materialFormulaVo.getId());
            MaterialFormula materialFormula = optionalMaterialFormula.orElseGet(optionalMaterialFormula::get);
            materialFormula.setCount(materialFormulaVo.getCount());
            materialFormula.setMaterial(material);
            price += materialFormula.getCount() * materialFormula.getMaterial().getPrice();
        }
        basic.setMaterialFormulaSet(materialFormulaSet);
        basic.setPrice(price);
        Basic dpt = basicDao.saveAndFlush(basic);
        return null;
    }

    @Override
    public BasicVo delete(BasicVo vo) {
        Optional<Basic> optionalBasic = basicDao.findById(vo.getId());
        Basic basic = optionalBasic.orElseGet(optionalBasic::get);
        Set<MaterialFormula> materialFormulaSet = basic.getMaterialFormulaSet();
        for(MaterialFormula formula: materialFormulaSet){
            materialFormulaDao.delete(formula);
        }
        basicDao.delete(basic);
        return vo;
    }
}
