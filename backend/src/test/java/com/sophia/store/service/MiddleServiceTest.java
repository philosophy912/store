package com.sophia.store.service;

import com.philosophy.base.util.NumericUtils;
import com.sophia.store.entity.po.Middle;
import com.sophia.store.entity.vo.BasicFormulaVo;
import com.sophia.store.entity.vo.BasicVo;
import com.sophia.store.entity.vo.MaterialFormulaVo;
import com.sophia.store.entity.vo.MiddleVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class MiddleServiceTest {
    @Autowired
    private MiddleService service;

    private static final String[] UNITS = new String[]{"克", "盒", "打", "升", "斤"};

    @Test
    void findAll() {
        List<MiddleVo> middleVos = service.findAll();
        middleVos.forEach(System.out::println);
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
            MiddleVo vo = new MiddleVo();
            vo.setName("中级材料" + (i + 1));
            vo.setCapacity(NumericUtils.randomInteger(100, 200));
            vo.setUnit(UNITS[NumericUtils.randomInteger(0, UNITS.length - 1)]);
            if (i % 5 == 0) {
                // 只添加materialFormula
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
            } else if (i % 3 == 0) {
                // 只添加basicFormula
                Set<BasicFormulaVo> basicFormulaVos = new HashSet<>();
                Set<Integer> idSet = new HashSet<>();
                for (int j = 0; j < NumericUtils.randomInteger(5, 10); j++) {
                    BasicFormulaVo formulaVo = new BasicFormulaVo();
                    formulaVo.setCount(NumericUtils.randomInteger(1, 5));
                    Integer basicId = NumericUtils.randomInteger(3, 100);
                    int size = idSet.size();
                    idSet.add(basicId);
                    if (size != idSet.size()) {
                        formulaVo.setBasicId(basicId);
                        basicFormulaVos.add(formulaVo);
                    }
                }
                vo.setBasicFormulaVos(basicFormulaVos);
            } else {
                Set<MaterialFormulaVo> materialFormulaVos = new HashSet<>();
                Set<Integer> materialIdSet = new HashSet<>();
                for (int j = 0; j < NumericUtils.randomInteger(5, 10); j++) {
                    MaterialFormulaVo formulaVo = new MaterialFormulaVo();
                    formulaVo.setCount(NumericUtils.randomInteger(1, 5));
                    Integer materialId = NumericUtils.randomInteger(1, 100);
                    int size = materialIdSet.size();
                    materialIdSet.add(materialId);
                    if (size != materialIdSet.size()) {
                        formulaVo.setMaterialId(materialId);
                        materialFormulaVos.add(formulaVo);
                    }
                }
                vo.setMaterialFormulaVos(materialFormulaVos);
                // 只添加basicFormula
                Set<BasicFormulaVo> basicFormulaVos = new HashSet<>();
                Set<Integer> basicIdSet = new HashSet<>();
                for (int j = 0; j < NumericUtils.randomInteger(5, 10); j++) {
                    BasicFormulaVo formulaVo = new BasicFormulaVo();
                    formulaVo.setCount(NumericUtils.randomInteger(1, 5));
                    Integer basicId = NumericUtils.randomInteger(3, 100);
                    int size = basicIdSet.size();
                    basicIdSet.add(basicId);
                    if (size != basicIdSet.size()) {
                        formulaVo.setBasicId(basicId);
                        basicFormulaVos.add(formulaVo);
                    }
                }
                vo.setBasicFormulaVos(basicFormulaVos);
            }
            service.add(vo);
        }
    }

    @Test
    void update() {
        MiddleVo vo = new MiddleVo();
        vo.setId(1);
        vo.setName("中级材料1");
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
        Set<BasicFormulaVo> basicFormulaVos = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            BasicFormulaVo formulaVo = new BasicFormulaVo();
            formulaVo.setCount(5);
            formulaVo.setBasicId(i + 1);
            basicFormulaVos.add(formulaVo);
        }
        vo.setBasicFormulaVos(basicFormulaVos);
        service.update(vo);
    }

    @Test
    void delete() {
        MiddleVo vo = new MiddleVo();
        vo.setId(2);
        service.delete(vo);
    }
}