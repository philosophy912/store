package com.sophia.store.service;

import com.sophia.store.dao.BasicDao;
import com.sophia.store.dao.BasicFormulaDao;
import com.sophia.store.dao.CategoryDao;
import com.sophia.store.dao.MaterialDao;
import com.sophia.store.dao.MaterialFormulaDao;
import com.sophia.store.dao.MiddleDao;
import com.sophia.store.entity.po.Basic;
import com.sophia.store.entity.po.BasicFormula;
import com.sophia.store.entity.po.Category;
import com.sophia.store.entity.po.Food;
import com.sophia.store.entity.po.Material;
import com.sophia.store.entity.po.MaterialFormula;
import com.sophia.store.entity.vo.CategoryVo;
import com.sophia.store.entity.vo.FoodVo;
import com.sophia.store.entity.vo.FormulaVo;
import com.sophia.store.utils.Constant;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseService {

    @Resource
    protected CategoryDao categoryDao;
    @Resource
    protected MaterialDao materialDao;
    @Resource
    protected MaterialFormulaDao materialFormulaDao;
    @Resource
    protected BasicFormulaDao basicFormulaDao;
    @Resource
    protected BasicDao basicDao;
    @Resource
    protected MiddleDao middleDao;

    protected FoodVo convertFood(Food food) {
        FoodVo vo = new FoodVo();
        vo.setId(food.getId());
        vo.setName(food.getName());
        vo.setCount(food.getCount());
        vo.setRestCount(food.getRestCount());
        vo.setInDate(food.getInDate());
        if (null != food.getExpireDate()) {
            vo.setExpireDate(food.getExpireDate());
        }
        if (null != food.getImageUrl()) {
            vo.setImageUrl(food.getImageUrl());
        }
        if (null != food.getDescription()) {
            vo.setDescription(food.getDescription());
        }
        Category category = food.getCategory();
        vo.setCategoryId(category.getId());
        vo.setCategoryName(category.getName());
        return vo;
    }

    protected Food convertFood(FoodVo vo, String type) {
        Food food = new Food();
        // 更新的时候需要ID
        if (type.equalsIgnoreCase(Constant.UPDATE)) {
            food.setId(vo.getId());
        }
        food.setName(vo.getName());
        // 创建的时候需要时间
        if (type.equalsIgnoreCase(Constant.CREATE)) {
            food.setInDate(vo.getInDate());
        }
        Optional<Category> optionalCategory = categoryDao.findById(vo.getCategoryId());
        Category category = optionalCategory.orElseGet(optionalCategory::get);
        food.setCategory(category);
        if (null != vo.getExpireDate()) {
            food.setExpireDate(vo.getExpireDate());
        }
        if (null != vo.getDescription()) {
            food.setDescription(vo.getDescription());
        }
        if (null != vo.getImageUrl()) {
            food.setImageUrl(vo.getImageUrl());
        }
        food.setCount(vo.getCount());
        food.setRestCount(vo.getRestCount());
        return food;
    }

    protected CategoryVo convertCategory(Category category) {
        CategoryVo vo = new CategoryVo();
        vo.setId(category.getId());
        vo.setName(category.getName());
        vo.setNeedExpire(category.getNeedExpire());
        Set<FoodVo> foodVoSet = new HashSet<>();
        category.getFoods().forEach(food -> foodVoSet.add(convertFood(food)));
        vo.setFoods(foodVoSet);
        return vo;
    }

    protected CategoryVo convertCategoryWithoutFood(Category category) {
        CategoryVo vo = new CategoryVo();
        vo.setId(category.getId());
        vo.setName(category.getName());
        vo.setNeedExpire(category.getNeedExpire());
        return vo;
    }

    protected Set<FormulaVo> convertMaterialFormulaVo(Set<MaterialFormula> materialFormulas) {
        Set<FormulaVo> formulaVos = new HashSet<>();
        materialFormulas.forEach(formula -> {
            log.debug("formula is {}", formula);
            FormulaVo vo = new FormulaVo();
            Material material = formula.getMaterial();
            vo.setId(material.getId());
            vo.setName(material.getName());
            vo.setCount(formula.getCount());
            vo.setType(Constant.MATERIAL);
            vo.setPrice(material.getPricePerUnit());
//            vo.setPrice(vo.getCount() * material.getPricePerUnit());
            log.debug("material is {}", material);
            log.debug("vo is {}", vo);
            formulaVos.add(vo);
        });
        log.debug("convertMaterialFormulaVo result is {}", formulaVos);
        return formulaVos;
    }

    protected Set<FormulaVo> convertBasicFormulaVo(Set<BasicFormula> basicFormulas) {
        Set<FormulaVo> formulaVos = new HashSet<>();
        basicFormulas.forEach(formula -> {
            FormulaVo vo = new FormulaVo();
            Basic basic = formula.getBasic();
            vo.setId(basic.getId());
            vo.setName(basic.getName());
            vo.setCount(formula.getCount());
            vo.setType(Constant.BASIC);
            double price = basic.getMaterialFormulaSet().stream()
                    .mapToDouble(materialFormula -> materialFormula.getMaterial().getPricePerUnit() * materialFormula.getCount())
                    .sum();
            vo.setPrice((float) price);
            formulaVos.add(vo);
        });
        log.debug("convertBasicFormulaVo result is {}", formulaVos);
        return formulaVos;
    }

    protected Set<MaterialFormula> convertMaterialFormula(Set<FormulaVo> formulaVos) {
        Set<MaterialFormula> materialFormulas = new HashSet<>();
        Set<FormulaVo> materialFormulaVos = formulaVos.stream().filter(formulaVo -> formulaVo.getType().equalsIgnoreCase(Constant.MATERIAL)).collect(Collectors.toSet());
        //有可能不需要原材料，所以需要判断是否为空
        if (!materialFormulaVos.isEmpty()) {
            for (FormulaVo formula : materialFormulaVos) {
                MaterialFormula materialFormula = new MaterialFormula();
                Optional<Material> optionalMaterial = materialDao.findById(formula.getId());
                Material material = optionalMaterial.orElseGet(optionalMaterial::get);
                materialFormula.setMaterial(material);
                materialFormula.setCount(formula.getCount());
                materialFormulas.add(materialFormula);
            }
        }
        return materialFormulas;
    }

    protected Set<BasicFormula> convertBasicFormula(Set<FormulaVo> formulaVos) {
        Set<BasicFormula> basicFormulas = new HashSet<>();
        Set<FormulaVo> basicFormulaVos = formulaVos.stream().filter(formulaVo -> formulaVo.getType().equalsIgnoreCase(Constant.BASIC)).collect(Collectors.toSet());
        //有可能不需要基础材料，所以需要判断是否为空
        if (!basicFormulaVos.isEmpty()) {
            for (FormulaVo formula : basicFormulaVos) {
                BasicFormula basicFormula = new BasicFormula();
                Optional<Basic> optionalBasic = basicDao.findById(formula.getId());
                Basic basic = optionalBasic.orElseGet(optionalBasic::get);
                basicFormula.setBasic(basic);
                basicFormula.setCount(formula.getCount());
                basicFormulas.add(basicFormula);
            }
        }
        return basicFormulas;
    }

    protected Set<MaterialFormula> convert2MaterialFormula(Set<FormulaVo> formulaVos) {
        Set<MaterialFormula> materialFormulas = new HashSet<>();
        formulaVos.forEach(formula -> {
            MaterialFormula vo = new MaterialFormula();
            vo.setCount(formula.getCount());
            Optional<Material> optionalMaterial = materialDao.findById(formula.getId());
            Material material = optionalMaterial.orElseGet(optionalMaterial::get);
            vo.setMaterial(material);
            materialFormulas.add(vo);
        });
        return materialFormulas;
    }

}
