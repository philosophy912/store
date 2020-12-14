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
            float count = NumericUtils.randomFloat(5, 10);
            foodVo.setCount((float) (Math.round(1 / count * 10000) / 100.0));
            float restCount = NumericUtils.randomFloat(1, 5);
            foodVo.setRestCount((float) (Math.round(1 / restCount * 10000) / 100.0));
            LocalDateTime now = LocalDateTime.now();
            foodVo.setInDate(convert(now));
            if (i % 2 == 0) {
                LocalDateTime localDateTime = LocalDateTime.of(2021, 7, 12, 12, 12);
                foodVo.setExpireDate(convert(localDateTime));
                foodVo.setCategoryId(1);
            } else if (i % 3 == 0) {
                LocalDateTime localDateTime = LocalDateTime.of(2021, 8, 1, 11, 5);
                foodVo.setExpireDate(convert(localDateTime));
                foodVo.setCategoryId(3);
            } else if (i % 5 == 0) {
                LocalDateTime localDateTime = LocalDateTime.of(2021, 9, 12, 18, 12);
                foodVo.setExpireDate(convert(localDateTime));
                foodVo.setCategoryId(5);
            } else {
                foodVo.setCategoryId(2);
            }
            service.addFood(foodVo);
        }
    }
    @Test
    void findFoodCountByNameAndCategoryId(){
        long count = service.findFoodCountByNameAndCategoryId("%1%", 1);
        System.out.println(count);
    }
}