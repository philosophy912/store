package com.sophia.store.controller;


import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.vo.FoodVo;
import com.sophia.store.entity.vo.PageResponse;
import com.sophia.store.entity.vo.Response;
import com.sophia.store.service.FoodService;
import com.sophia.store.utils.Constant;
import com.sophia.store.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/store/food")
@Slf4j
public class FoodController {
    @Resource
    private FoodService foodService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResponse fetchList(@RequestParam int page,
                                  @RequestParam int limit,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String sort,
                                  @RequestParam(required = false) Integer categoryId) {
        log.debug("categoryId = {}", categoryId);
        PageResponse response = new PageResponse();
        Pageable pageable;
        if (StringsUtils.isNotEmpty(sort)) {
            if (sort.equalsIgnoreCase(Constant.DESC)) {
                pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "id");
            } else {
                pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            }
        } else {
            pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
        }
        try {
            List<FoodVo> categoryVos = foodService.findFood(pageable, name, categoryId);
            long count;
            if (StringsUtils.isEmpty(name) && categoryId == null) {
                // 名字和分类都不存在
                count = foodService.findAllFoodCount();
            } else if (StringsUtils.isNotEmpty(name) && categoryId != null) {
                // 名字和分类都存在
                count = foodService.findFoodCountByNameAndCategoryId("%" + name + "%", categoryId);
            } else if (StringsUtils.isEmpty(name)) {
                // 仅分类存在
                count = foodService.findFoodCountByCategoryId(categoryId);
            } else {
                // 仅名字存在
                count = foodService.findFoodCountByName("%" + name + "%");
            }
            response.setMessage("查询成功");
            response.setData(categoryVos);
            response.setPageSize(pageable.getPageSize());
            response.setTotalRows((int) count);
            response.setTotalPages(PageUtils.get(count, pageable.getPageSize()));
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("查询失败");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Response create(@RequestBody FoodVo vo) {
        Response response = new Response();
        try {
            FoodVo foodVo = foodService.addFood(vo);
            response.setData(Collections.singletonList(foodVo));
            response.setMessage("创建成功");
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("创建失败");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Response update(@RequestBody FoodVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            FoodVo foodVo = foodService.updateFood(vo);
            if (foodVo != null) {
                response.setData(Collections.singletonList(vo));
                response.setMessage("更新成功");
            } else {
                response.setCode(Constant.NOK);
                response.setMessage(name + "在数据库中未查询到，请检查");
            }
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("更新失败");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response delete(@RequestBody FoodVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            FoodVo foodVo = foodService.deleteFood(vo);
            if (foodVo != null) {
                response.setData(Collections.singletonList(vo));
                response.setMessage("删除成功");
            } else {
                response.setCode(Constant.NOK);
                response.setMessage(name + "不在数据库中，无法删除");
            }
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("删除失败");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

}
