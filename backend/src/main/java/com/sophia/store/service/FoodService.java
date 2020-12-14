package com.sophia.store.service;

import com.sophia.store.entity.vo.FoodVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodService {

    List<FoodVo> findFood(Pageable pageable, String name, Integer categoryId);

    long findAllFoodCount();

    long findFoodCountByName(String name);

    FoodVo addFood(FoodVo vo);

    FoodVo updateFood(FoodVo vo);

    FoodVo deleteFood(FoodVo vo);

    long findFoodCountByNameAndCategoryId(String name, Integer categoryId);

    long findFoodCountByCategoryId(Integer categoryId);
}
