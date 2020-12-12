package com.sophia.store.service;

import com.philosophy.base.util.NumericUtils;
import com.sophia.store.entity.vo.FoodVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class FoodServiceTest {
    @Autowired
    private FoodService service;

    private Long convert(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Test
    void addFood() {
        for (int i = 0; i < 50; i++) {
            FoodVo foodVo = new FoodVo();
            foodVo.setName("耗材" + (i + 1));
            foodVo.setCount(NumericUtils.randomFloat(5, 10));
            foodVo.setRestCount(NumericUtils.randomFloat(1, 5));
            LocalDateTime now = LocalDateTime.now();
            foodVo.setInDate(convert(now));
            if (i % 2 == 0) {
                foodVo.setCategoryId(1);
                LocalDateTime localDateTime = LocalDateTime.of(2021, 7, 12, 12, 12);
                foodVo.setExpireDate(convert(localDateTime));
            } else if (i % 3 == 0) {
                foodVo.setCategoryId(3);
                LocalDateTime localDateTime = LocalDateTime.of(2021, 8, 1, 11, 5);
                foodVo.setExpireDate(convert(localDateTime));
            } else if (i % 5 == 0) {
                foodVo.setCategoryId(5);
                LocalDateTime localDateTime = LocalDateTime.of(2021, 9, 12, 18, 12);
                foodVo.setExpireDate(convert(localDateTime));
            } else {
                foodVo.setCategoryId(2);
            }
            service.addFood(foodVo);
        }
    }
}