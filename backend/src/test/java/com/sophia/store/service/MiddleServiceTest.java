package com.sophia.store.service;

import com.philosophy.base.util.NumericUtils;
import com.sophia.store.entity.vo.FormulaVo;
import com.sophia.store.entity.vo.MiddleVo;
import com.sophia.store.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
                Set<FormulaVo> formulaVos = new HashSet<>();
                Set<Integer> idSet = new HashSet<>();
                for (int j = 0; j < NumericUtils.randomInteger(5, 10); j++) {
                    FormulaVo formulaVo = new FormulaVo();
                    formulaVo.setCount(NumericUtils.randomInteger(1, 5));
                    Integer materialId = NumericUtils.randomInteger(1, 100);
                    int size = idSet.size();
                    idSet.add(materialId);
                    if (size != idSet.size()) {
                        formulaVo.setType(Constant.MATERIAL);
                        formulaVo.setId(materialId);
                        formulaVos.add(formulaVo);
                    }
                }
                vo.setFormulaVos(formulaVos);
            } else if (i % 3 == 0) {
                // 只添加basicFormula
                Set<FormulaVo> formulaVos = new HashSet<>();
                Set<Integer> idSet = new HashSet<>();
                for (int j = 0; j < NumericUtils.randomInteger(5, 10); j++) {
                    FormulaVo formulaVo = new FormulaVo();
                    formulaVo.setCount(NumericUtils.randomInteger(1, 5));
                    Integer basicId = NumericUtils.randomInteger(3, 100);
                    int size = idSet.size();
                    idSet.add(basicId);
                    if (size != idSet.size()) {
                        formulaVo.setType(Constant.BASIC);
                        formulaVo.setId(basicId);
                        formulaVos.add(formulaVo);
                    }
                }
                vo.setFormulaVos(formulaVos);
            } else {
                // 只添加materialFormula
                Set<FormulaVo> formulaVos = new HashSet<>();
                Set<Integer> materialIdSet = new HashSet<>();
                for (int j = 0; j < NumericUtils.randomInteger(5, 10); j++) {
                    FormulaVo formulaVo = new FormulaVo();
                    formulaVo.setCount(NumericUtils.randomInteger(1, 5));
                    Integer materialId = NumericUtils.randomInteger(1, 100);
                    int size = materialIdSet.size();
                    materialIdSet.add(materialId);
                    if (size != materialIdSet.size()) {
                        formulaVo.setType(Constant.MATERIAL);
                        formulaVo.setId(materialId);
                        formulaVos.add(formulaVo);
                    }
                }
                Set<Integer> basicIdSet = new HashSet<>();
                for (int j = 0; j < NumericUtils.randomInteger(5, 10); j++) {
                    FormulaVo formulaVo = new FormulaVo();
                    formulaVo.setCount(NumericUtils.randomInteger(1, 5));
                    Integer basicId = NumericUtils.randomInteger(1, 100);
                    int size = basicIdSet.size();
                    basicIdSet.add(basicId);
                    if (size != basicIdSet.size()) {
                        formulaVo.setType(Constant.BASIC);
                        formulaVo.setId(basicId);
                        formulaVos.add(formulaVo);
                    }
                }
                vo.setFormulaVos(formulaVos);
            }
            log.info("vo = {}", vo);
            service.add(vo);
        }
    }

    @Test
    void update() {
        MiddleVo vo = new MiddleVo();
        vo.setId(2);
        vo.setName("中级材料1");
        vo.setUnit("克");
        vo.setCapacity(1000);
        Set<FormulaVo> formulaVos = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            FormulaVo formulaVo = new FormulaVo();
            formulaVo.setCount(5);
            formulaVo.setId(i + 1);
            if (i % 2 == 0) {
                formulaVo.setType(Constant.MATERIAL);
            } else {
                formulaVo.setType(Constant.BASIC);
            }
            formulaVos.add(formulaVo);
        }
        vo.setFormulaVos(formulaVos);
        service.update(vo);
    }

    @Test
    void delete() {
        MiddleVo vo = new MiddleVo();
        vo.setId(2);
        service.delete(vo);
    }
}