package com.sophia.store.controller;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.vo.MaterialVo;
import com.sophia.store.entity.vo.PageResponse;
import com.sophia.store.entity.vo.Response;
import com.sophia.store.service.MaterialService;
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
@RequestMapping("/store/material")
@Slf4j
public class MaterialController {
    @Resource
    private MaterialService materialService;


    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Response findAll() {
        Response response = new Response();
        try {
            List<MaterialVo> materialVos = materialService.findAll();
            response.setMessage("查询成功");
            response.setData(materialVos);
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("查询失败");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResponse fetchList(@RequestParam int page,
                                  @RequestParam int limit,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) String sort) {
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
            List<MaterialVo> materialVos = materialService.find(pageable, name);
            long count;
            if (StringsUtils.isEmpty(name)) {
                count = materialService.findAllCount();
            } else {
                count = materialService.findCountByName("%" + name + "%");
            }
            response.setMessage("查询成功");
            response.setData(materialVos);
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
    public Response create(@RequestBody MaterialVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            MaterialVo materialVo = materialService.add(vo);
            if (materialVo != null) {
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
    public Response update(@RequestBody MaterialVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            MaterialVo materialVo = materialService.update(vo);
            if (materialVo != null) {
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
    public Response delete(@RequestBody MaterialVo vo) {
        Response response = new Response();
        String name = vo.getName();
        try {
            MaterialVo materialVo = materialService.delete(vo);
            if (materialVo != null) {
                response.setData(Collections.singletonList(vo));
                response.setMessage("删除成功");
            } else {
                response.setCode(Constant.NOK);
                response.setMessage(name + "不在数据库中，无法删除");
            }
        } catch (Exception e) {
            response.setCode(Constant.NOK);
            response.setMessage("删除失败， 该原材料被其他产品使用");
            response.setErrorInfo(e.getMessage());
        }
        return response;
    }

}
