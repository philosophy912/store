package com.sophia.store.service;

import com.philosophy.base.util.NumericUtils;
import com.sophia.store.entity.vo.BasicFormulaVo;
import com.sophia.store.entity.vo.BasicVo;
import com.sophia.store.entity.vo.MaterialFormulaVo;
import com.sophia.store.entity.vo.MaterialVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.NumberUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class BasicServiceTest {
    @Autowired
    private BasicService service;

    private static final String[] UNITS = new String[]{"克", "盒", "打", "升", "斤"};

    @Test
    void findAll() {
        List<BasicVo> basicVos = service.findAll();
        basicVos.forEach(System.out::println);
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
            BasicVo vo = new BasicVo();
            vo.setName("基础材料" + (i + 1));
            vo.setCapacity(NumericUtils.randomInteger(100, 200));
            vo.setUnit(UNITS[NumericUtils.randomInteger(0, UNITS.length - 1)]);
            Set<MaterialFormulaVo> materialFormulaVos = new HashSet<>();
            Set<Integer> idSet = new HashSet<>();
            for (int j = 0; j < NumericUtils.randomInteger(5, 10); j++) {
                MaterialFormulaVo formulaVo = new MaterialFormulaVo();
                formulaVo.setCount(NumericUtils.randomInteger(1, 5));
                Integer materialId = NumericUtils.randomInteger(1, 100);
                int size = idSet.size();
                idSet.add(materialId);
                if (size != idSet.size()) {
                    formulaVo.setMaterialId(materialId);
                    materialFormulaVos.add(formulaVo);
                }
            }
            vo.setMaterialFormulaVos(materialFormulaVos);
            service.add(vo);
        }
    }

    @Test
    void update() {
        BasicVo vo = new BasicVo();
        vo.setId(1);
        vo.setName("基础材料1");
        vo.setUnit("克");
        vo.setCapacity(1000);
        Set<MaterialFormulaVo> materialFormulaVos = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            MaterialFormulaVo formulaVo = new MaterialFormulaVo();
            formulaVo.setCount(5);
            formulaVo.setMaterialId(i + 1);
            materialFormulaVos.add(formulaVo);
        }
        vo.setMaterialFormulaVos(materialFormulaVos);
        service.update(vo);
    }

    @Test
    void delete() {
        BasicVo vo = new BasicVo();
        vo.setId(1);
        service.delete(vo);
    }
}