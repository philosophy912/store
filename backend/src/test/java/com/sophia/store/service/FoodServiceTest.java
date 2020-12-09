package com.sophia.store.service;

import com.philosophy.base.util.NumericUtils;
import com.sophia.store.entity.vo.FoodVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class FoodServiceTest {
    @Autowired
    private FoodService service;

    @Test
    void addFood() {
        for (int i = 0; i < 50; i++) {
            FoodVo foodVo = new FoodVo();
            foodVo.setName("耗材" + (i + 1));
            foodVo.setCount(NumericUtils.randomFloat(1, 10));
            foodVo.setRestCount(0f);
            foodVo.setInDate(System.currentTimeMillis());
            if (i % 2 == 0) {
                foodVo.setCategoryId(1);
                foodVo.setInDate(System.currentTimeMillis() + 100000000);
            } else if (i % 3 == 0) {
                foodVo.setCategoryId(3);
                foodVo.setInDate(System.currentTimeMillis() + 100000000);
            } else if (i % 5 == 0) {
                foodVo.setCategoryId(5);
                foodVo.setInDate(System.currentTimeMillis() + 100000000);
            } else {
                foodVo.setCategoryId(2);
            }
            service.addFood(foodVo);
        }
    }
}