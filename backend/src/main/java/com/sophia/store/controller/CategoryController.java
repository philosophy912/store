package com.sophia.store.controller;


import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.vo.CategoryVo;
import com.sophia.store.entity.vo.PageResponse;
import com.sophia.store.entity.vo.Response;
import com.sophia.store.service.CategoryService;
import com.sophia.store.utils.Constant;
import com.sophia.store.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/category")
@Slf4j
@Api(value = "分类管理接口", tags = {"分类管理"})
public class CategoryController {
    @Resource
    private CategoryService categoryService;


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value = "查找所有的分类信息")
    public Response findAll() {
        Response response = new Response();
        try {
            List<CategoryVo> categories = categoryService.findAllCategories();
            response.setMessage("查询成功");
            response.setData(categories);
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("查询失败");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "分页查找分类的列表")
    public PageResponse fetchList(@ApiParam(value = "页数", required = true, example = "1") @RequestParam int page,
                                  @ApiParam(value = "每页数量", required = true, example = "10") @RequestParam int limit,
                                  @ApiParam(value = "查询的名字", example = "部门1") @RequestParam(required = false) String name,
                                  @ApiParam(value = "排序方式", example = "+id/-id") @RequestParam(required = false) String sort) {
        PageResponse response = new PageResponse();
        Pageable pageable;
        if (Strings.isNotEmpty(sort)) {
            if (sort.equalsIgnoreCase(Constant.DESC)) {
                pageable = PageRequest.of(page - 1, limit, Sort.Direction.DESC, "id");
            } else {
                pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
            }
        } else {
            pageable = PageRequest.of(page - 1, limit, Sort.Direction.ASC, "id");
        }
        try {
            List<CategoryVo> categoryVos = categoryService.findCategory(pageable, name);
            long count;
            if (StringsUtils.isEmpty(name)) {
                count = categoryService.findAllCategoryCount();
            } else {
                count = categoryService.findCategoryCountByName("%" + name + "%");
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
    @ApiOperation(value = "添加分类", notes = "其中name和timestamp不能为空")
    public Response create(@RequestBody CategoryVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            CategoryVo categoryVo = categoryService.addCategory(vo);
            if (categoryVo != null) {
                response.setData(Collections.singletonList(vo));
                response.setMessage("创建成功");
            } else {
                response.setCode(Constant.NOK);
                response.setMessage(name + "已经在数据库中存在，无法添加");
            }
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("创建失败");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新分类", notes = "仅能更新分类名称和是否填写过期信息")
    public Response update(@RequestBody CategoryVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            CategoryVo categoryVo = categoryService.updateCategory(vo);
            if (categoryVo != null) {
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
    @ApiOperation(value = "删除分类", notes = "删除分类，该分类下不能允许有耗材存在")
    public Response delete(@RequestBody CategoryVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            CategoryVo categoryVo = categoryService.deleteCategory(vo);
            if (categoryVo != null) {
                response.setData(Collections.singletonList(vo));
                response.setMessage("删除成功");
            } else {
                response.setCode(Constant.NOK);
                response.setMessage(name + "不在数据库中，无法删除");
            }
        } catch (DataIntegrityViolationException e) {
            response.setCode(Constant.NOK);
            response.setMessage("删除失败，部门" + name + "中仍然存在员工");
            response.setErrorInfo(e.getMessage());
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("删除失败");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

}
