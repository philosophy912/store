package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.dao.CategoryDao;
import com.sophia.store.dao.FoodDao;
import com.sophia.store.entity.po.Category;
import com.sophia.store.entity.po.Food;
import com.sophia.store.entity.vo.FoodVo;
import com.sophia.store.utils.Constant;
import com.sophia.store.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FoodServiceImpl implements FoodService {
    @Resource
    private FoodDao foodDao;
    @Resource
    private CategoryDao categoryDao;

    private FoodVo convert(Food food) {
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
        return vo;
    }

    private Food convert(FoodVo vo, String type) {
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


    @Override
    public List<FoodVo> findFood(Pageable pageable, String name) {
        List<FoodVo> foodVos = new ArrayList<>();
        Page<Food> foods = foodDao.findAll((Specification<Food>) (root, query, criteriaBuilder) -> {
            // 1. 创建集合 存储查询条件
            List<Predicate> queryList = new ArrayList<>();
            // 2. 添加查询条件
            if (StringsUtils.isNotEmpty(name)) {
                queryList.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            query.where(queryList.toArray(new Predicate[0]));
            return null;
        }, pageable);
        foods.forEach(food -> foodVos.add(convert(food)));
        return foodVos;
    }

    @Override
    public long findAllFoodCount() {
        return foodDao.count();
    }

    @Override
    public long findFoodCountByName(String name) {
        return foodDao.findByNameLike(name).size();
    }

    @Override
    public FoodVo addFood(FoodVo vo) {
        List<Food> foods = foodDao.findByName(vo.getName());
        if (foods.size() == 0) {
            Food food = convert(vo, Constant.CREATE);
            Food dpt = foodDao.saveAndFlush(food);
            return convert(dpt);
        }
        return null;
    }

    @Override
    public FoodVo updateFood(FoodVo vo) {
        Food originFood = convert(vo, Constant.UPDATE);
        Optional<Food> optionalFood = foodDao.findById(vo.getId());
        Food food = optionalFood.orElseGet(optionalFood::get);
        ObjectUtils.copyFiledValue(originFood, food);
        log.debug("update food and food is {}", food);
        foodDao.saveAndFlush(food);
        return vo;
    }

    @Override
    public FoodVo deleteFood(FoodVo vo) {
        int id = vo.getId();
        log.debug("department id = " + id);
        Optional<Food> optionalFood = foodDao.findById(id);
        Food food = optionalFood.orElseGet(optionalFood::get);
        foodDao.delete(food);
        return vo;
    }
}
