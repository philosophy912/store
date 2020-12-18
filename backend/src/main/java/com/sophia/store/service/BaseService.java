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
import com.sophia.store.entity.vo.BasicFormulaVo;
import com.sophia.store.entity.vo.CategoryVo;
import com.sophia.store.entity.vo.FoodVo;
import com.sophia.store.entity.vo.MaterialFormulaVo;
import com.sophia.store.utils.Constant;
import com.sophia.store.utils.ObjectUtils;
import sun.util.resources.cldr.teo.CalendarData_teo_KE;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    protected Set<MaterialFormulaVo> convertMaterialFormula(Set<MaterialFormula> materialFormulas) {
        Set<MaterialFormulaVo> materialFormulaVos = new HashSet<>();
        materialFormulas.forEach(formula -> {
            MaterialFormulaVo materialFormulaVo = new MaterialFormulaVo();
            materialFormulaVo.setId(formula.getId());
            materialFormulaVo.setCount(formula.getCount());
            Material material = formula.getMaterial();
            materialFormulaVo.setMaterialId(material.getId());
            materialFormulaVo.setMaterialName(material.getName());
            materialFormulaVos.add(materialFormulaVo);
        });
        return materialFormulaVos;
    }

    protected Set<BasicFormulaVo> convertBasicFormula (Set<BasicFormula> basicFormulas){
        Set<BasicFormulaVo> basicFormulaVos = new HashSet<>();
        basicFormulas.forEach(formula -> {
            BasicFormulaVo basicFormulaVo = new BasicFormulaVo();
            basicFormulaVo.setId(formula.getId());
            basicFormulaVo.setCount(formula.getCount());
            Basic basic = formula.getBasic();;
            basicFormulaVo.setBasicId(basic.getId());
            basicFormulaVo.setBasicName(basic.getName());
            basicFormulaVos.add(basicFormulaVo);
        });
        return basicFormulaVos;
    }

}
