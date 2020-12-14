package com.sophia.store.dao;

import com.philosophy.base.util.NumericUtils;
import com.sophia.store.entity.po.Category;
import com.sophia.store.entity.po.Food;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lizhe
 * @date 2020/12/14 14:57
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class CategoryDaoTest {
    @Autowired
    private CategoryDao categoryDao;

    private Long convert(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Test
    void findByName() {
        Map<Integer, Category> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("分类" + (i + 1));
            category.setNeedExpire(i % 2 == 0);
            map.put(i, category);
        }
        for (int i = 0; i < 50; i++) {
            int id = NumericUtils.randomInteger(0, 5);
            Food food = new Food();
            food.setName("耗材" + (i + 1));
            float count = NumericUtils.randomFloat(5, 10);
            food.setCount((float) (Math.round(1 / count * 10000) / 100.0));
            float restCount = NumericUtils.randomFloat(1, 5);
            food.setRestCount((float) (Math.round(1 / restCount * 10000) / 100.0));
            LocalDateTime now = LocalDateTime.now();
            food.setInDate(convert(now));
            if (i % 2 == 0) {
                LocalDateTime localDateTime = LocalDateTime.of(2021, 7, 12, 12, 12);
                food.setExpireDate(convert(localDateTime));
            } else if (i % 3 == 0) {
                LocalDateTime localDateTime = LocalDateTime.of(2021, 8, 1, 11, 5);
                food.setExpireDate(convert(localDateTime));
            } else if (i % 5 == 0) {
                LocalDateTime localDateTime = LocalDateTime.of(2021, 9, 12, 18, 12);
                food.setExpireDate(convert(localDateTime));
            } else {
                food.setCategory(null);
            }
        }

//        categoryDao.saveandl

    }
}