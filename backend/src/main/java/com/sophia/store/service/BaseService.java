package com.sophia.store.service;

import com.sophia.store.dao.CategoryDao;
import com.sophia.store.entity.po.Category;
import com.sophia.store.entity.po.Food;
import com.sophia.store.entity.vo.CategoryVo;
import com.sophia.store.entity.vo.FoodVo;
import com.sophia.store.utils.Constant;
import com.sophia.store.utils.ObjectUtils;
import sun.util.resources.cldr.teo.CalendarData_teo_KE;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class BaseService {

    @Resource
    protected CategoryDao categoryDao;

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

}
