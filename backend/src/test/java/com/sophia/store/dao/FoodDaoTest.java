package com.sophia.store.dao;

import com.sophia.store.entity.po.Category;
import com.sophia.store.entity.po.Food;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class FoodDaoTest {
    @Autowired
    private FoodDao foodDao;
    @Autowired
    private CategoryDao categoryDao;

    @Test
    void findAll() {
    }

    @Test
    void findByName() {
    }

    @Test
    void findByNameLike() {
    }

    @Test
    void findByNameLikeAndCategory() {
        Optional<Category> optionalCategory = categoryDao.findById(1);
        Category category = optionalCategory.orElseGet(optionalCategory::get);
        List<Food> foods = foodDao.findByNameLikeAndCategory("%1%", category);
        System.out.println(foods.size());
        foods.forEach(System.out::println);

    }
}