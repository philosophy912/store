package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.dao.FoodDao;
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
public class FoodServiceImpl extends BaseService implements FoodService {
    @Resource
    private FoodDao foodDao;

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
            queryList.add(criteriaBuilder.gt(root.get("restCount"), 0));
            query.where(queryList.toArray(new Predicate[0]));
            return null;
        }, pageable);
        foods.forEach(food -> foodVos.add(convertFood(food)));
        return foodVos;
    }

    @Override
    public List<FoodVo> findFood(Pageable pageable, String name, Integer categoryId) {
        List<FoodVo> foodVos = new ArrayList<>();
        log.info("categoryId = [{}]", categoryId);
        Page<Food> foods = foodDao.findAll((Specification<Food>) (root, query, criteriaBuilder) -> {
            // 1. 创建集合 存储查询条件
            List<Predicate> queryList = new ArrayList<>();
            // 2. 添加查询条件
            if (StringsUtils.isNotEmpty(name)) {
                queryList.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (null != categoryId) {
                queryList.add(criteriaBuilder.equal(root.get("food_id"), categoryId));
            }
            queryList.add(criteriaBuilder.gt(root.get("restCount"), 0));
            query.where(queryList.toArray(new Predicate[0]));
            return null;
        }, pageable);
        foods.forEach(food -> foodVos.add(convertFood(food)));
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
            Food food = convertFood(vo, Constant.CREATE);
            Food dpt = foodDao.saveAndFlush(food);
            return convertFood(dpt);
        }
        return null;
    }

    @Override
    public FoodVo updateFood(FoodVo vo) {
        Food originFood = convertFood(vo, Constant.UPDATE);
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

    @Override
    public FoodVo useFood(FoodVo vo) {
        Optional<Food> optionalFood = foodDao.findById(vo.getId());
        Food food = optionalFood.orElseGet(optionalFood::get);
        if (vo.getRestCount() - vo.getCount() > 0) {

        }
        return null;
    }

}
