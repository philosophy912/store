package com.sophia.store.service;

import com.philosophy.base.util.NumericUtils;
import com.sophia.store.entity.po.Material;
import com.sophia.store.entity.vo.MaterialVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class MaterialServiceTest {
    @Autowired
    private MaterialService service;

    private static final String[] UNITS = new String[]{"克", "盒", "打", "升", "斤"};

    @Test
    void findAll() {
        List<MaterialVo> materialVos = service.findAll();
        materialVos.forEach(System.out::println);
    }

    @Test
    void find() {
    }

    @Test
    void findAllCount() {
    }

    @Test
    void findCountByName() {
    }

    @Test
    void add() {
        for (int i = 0; i < 100; i++) {
            MaterialVo vo = new MaterialVo();
            vo.setName("原材料" + (i + 1));
            vo.setPrice(NumericUtils.randomFloat(1, 20));
            vo.setCapacity(NumericUtils.randomInteger(100, 200));
            vo.setUnit(UNITS[NumericUtils.randomInteger(0, UNITS.length - 1)]);
            service.add(vo);
        }
    }

    @Test
    void update() {
        MaterialVo vo = new MaterialVo();
        vo.setId(1);
        vo.setCapacity(100);
        vo.setPrice(120f);
        vo.setName("原材料1");
        vo.setUnit("克");
        service.update(vo);
    }

    @Test
    void delete() {
        MaterialVo vo =new MaterialVo();
        vo.setId(1);
        service.delete(vo);
    }
}