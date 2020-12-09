package com.sophia.store.service;

import com.sophia.store.entity.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class CategoryServiceTest {
    @Autowired
    private CategoryService service;

    @Test
    void addCategory() {
        for (int i = 0; i < 5; i++) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setNeedExpire(i % 2 == 0);
            categoryVo.setName("分类" + (i + 1));
            service.addCategory(categoryVo);
        }
    }
}