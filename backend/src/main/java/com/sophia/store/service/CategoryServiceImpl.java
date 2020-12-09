package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.dao.CategoryDao;
import com.sophia.store.entity.po.Category;
import com.sophia.store.entity.vo.CategoryVo;
import com.sophia.store.utils.Constant;
import com.sophia.store.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryDao categoryDao;

    private CategoryVo convert(Category category) {
        CategoryVo vo = new CategoryVo();
        vo.setId(category.getId());
        vo.setName(category.getName());
        vo.setNeedExpire(category.getNeedExpire());
        vo.setFoods(category.getFoods());
        return vo;
    }

    private Category convert(CategoryVo vo, String type) {
        Category category = new Category();
        // 更新的时候需要ID
        if (type.equalsIgnoreCase(Constant.UPDATE)) {
            category.setId(vo.getId());
        }
        category.setName(vo.getName());
        category.setNeedExpire(vo.getNeedExpire());
        if (null != vo.getFoods()) {
            category.setFoods(vo.getFoods());
        }
        return category;
    }

    @Override
    public List<CategoryVo> findAllCategories() {
        List<CategoryVo> categoryVos = new ArrayList<>();
        List<Category> categories = categoryDao.findAll();
        categories.forEach(category -> categoryVos.add(convert(category)));
        return categoryVos;
    }

    @Override
    public List<CategoryVo> findCategory(Pageable pageable, String name) {
        List<CategoryVo> categoryVos = new ArrayList<>();
        Page<Category> categories = categoryDao.findAll((Specification<Category>) (root, query, criteriaBuilder) -> {
            // 1. 创建集合 存储查询条件
            List<Predicate> queryList = new ArrayList<>();
            // 2. 添加查询条件
            if (StringsUtils.isNotEmpty(name)) {
                queryList.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            query.where(queryList.toArray(new Predicate[0]));
            return null;
        }, pageable);
        categories.forEach(category -> categoryVos.add(convert(category)));
        return categoryVos;
    }

    @Override
    public Long findAllCategoryCount() {
        return categoryDao.count();
    }

    @Override
    public int findCategoryCountByName(String name) {
        return categoryDao.findByNameLike(name).size();
    }

    @Override
    public CategoryVo addCategory(CategoryVo vo) {
        List<Category> categories = categoryDao.findByName(vo.getName());
        if (categories.size() == 0) {
            Category category = convert(vo, Constant.CREATE);
            Category dpt = categoryDao.saveAndFlush(category);
            return convert(dpt);
        }
        return null;
    }

    @Override
    public CategoryVo updateCategory(CategoryVo vo) {
        Category originDepartment = convert(vo, Constant.UPDATE);
        Optional<Category> optionalCategory = categoryDao.findById(vo.getId());
        Category category = optionalCategory.orElseGet(optionalCategory::get);
        ObjectUtils.copyFiledValue(originDepartment, category);
        log.debug("update category and category is {}", category);
        categoryDao.saveAndFlush(category);
        return vo;
    }

    @Override
    public CategoryVo deleteCategory(CategoryVo vo) {
        int id = vo.getId();
        log.debug("department id = " + id);
        Optional<Category> optionalDepartment = categoryDao.findById(id);
        Category category = optionalDepartment.orElseGet(optionalDepartment::get);
        categoryDao.delete(category);
        return vo;
    }

    @Override
    public List<CategoryVo> findCategoryByName(CategoryVo vo) {
        String name = vo.getName();
        List<Category> categories = categoryDao.findByName(name);
        List<CategoryVo> categoryVos = new ArrayList<>();
        categories.forEach(category -> categoryVos.add(convert(category)));
        return categoryVos;
    }
}
