package com.sophia.store.controller;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.vo.BasicVo;
import com.sophia.store.entity.vo.MiddleVo;
import com.sophia.store.entity.vo.PageResponse;
import com.sophia.store.entity.vo.Response;
import com.sophia.store.log.Log;
import com.sophia.store.service.BasicService;
import com.sophia.store.service.MiddleService;
import com.sophia.store.utils.Constant;
import com.sophia.store.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/store/middle")
@Slf4j
@Api(value = "高级产品管理接口", tags = {"高级产品管理"})
public class MiddleController {
    @Resource
    private MiddleService middleService;


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value = "查找所有的分类信息")
    public Response findAll() {
        Response response = new Response();
        try {
            List<MiddleVo> middleVos = middleService.findAll();
            response.setMessage("查询成功");
            response.setData(middleVos);
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
            List<MiddleVo> middleVos = middleService.find(pageable, name);
            long count;
            if (StringsUtils.isEmpty(name)) {
                count = middleService.findAllCount();
            } else {
                count = middleService.findCountByName("%" + name + "%");
            }
            response.setMessage("查询成功");
            response.setData(middleVos);
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
    @ApiOperation(value = "添加中级产品", notes = "其中name和timestamp不能为空")
    @Log("添加中级产品")
    public Response create(@RequestBody MiddleVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            MiddleVo middleVo = middleService.add(vo);
            if (middleVo != null) {
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
    @ApiOperation(value = "更新中级产品", notes = "仅能更新分类名称和是否填写过期信息")
    @Log("更新中级产品")
    public Response update(@RequestBody MiddleVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            MiddleVo middleVo = middleService.update(vo);
            if (middleVo != null) {
                response.setData(Collections.singletonList(vo));
                response.setMessage("更新成功");
            } else {
                response.setCode(Constant.NOK);
                response.setMessage(name + "在数据库中未查询到，请检查");
            }
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("更新失败, 错误原因【" + e.getMessage() + "】");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除中级产品", notes = "删除分类，该分类下不能允许有耗材存在")
    @Log("删除中级产品")
    public Response delete(@RequestBody MiddleVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            MiddleVo middleVo = middleService.delete(vo);
            if (middleVo != null) {
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
