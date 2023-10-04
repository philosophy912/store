package com.sophia.store.service;

import com.philosophy.base.util.StringsUtils;
import com.sophia.store.entity.po.Category;
import com.sophia.store.entity.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl extends BaseService implements CategoryService {


    @Override
    public List<CategoryVo> findAllCategories() {
        List<CategoryVo> categoryVos = new ArrayList<>();
        List<Category> categories = categoryDao.findAll();
        categories.forEach(category -> categoryVos.add(convertCategoryWithoutFood(category)));
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
        categories.forEach(category -> categoryVos.add(convertCategory(category)));
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
        if (categories.isEmpty()) {
            Category category = new Category();
            category.setName(vo.getName());
            category.setNeedExpire(vo.getNeedExpire());
            Category dpt = categoryDao.saveAndFlush(category);
            return convertCategory(dpt);
        }
        return null;
    }

    @Override
    public CategoryVo updateCategory(CategoryVo vo) {
        String name = vo.getName();
        log.debug("update category vo is {}", vo);
        List<Category> categories = categoryDao.findByName(name);
        if (!categories.isEmpty()) {
            // 根据vo查询category
            Optional<Category> optionalCategory = categoryDao.findById(vo.getId());
            Category category = optionalCategory.orElseGet(optionalCategory::get);
            log.debug("vo expire[{}] and category expire[{}]", vo.getNeedExpire(), category.getNeedExpire());
            if (category.getNeedExpire() != vo.getNeedExpire()) {
                // 当前分类下的食品分类不为空且有一个食品的过期时间不为空
                category.getFoods().forEach(food -> {
                    if (null != food.getExpireDate()) {
                        String error = "当前分类下有耗材，无法修改过期时间";
                        throw new RuntimeException(error);
                    }
                });
            }
            category.setName(name);
            category.setNeedExpire(vo.getNeedExpire());
            log.debug("update category and category is {}", category);
            log.debug("update category and category food is {}", category.getFoods());
            categoryDao.saveAndFlush(category);
            return vo;
        }
        String error = "当前已存在分类名【" + name + "】";
        throw new RuntimeException(error);
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
        categories.forEach(category -> categoryVos.add(convertCategory(category)));
        return categoryVos;
    }
}
