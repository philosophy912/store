package com.sophia.store.service;

import com.sophia.store.entity.vo.CategoryVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryVo> findAllCategories();

    List<CategoryVo> findCategory(Pageable pageable, String name);

    Long findAllCategoryCount();

    int findCategoryCountByName(String name);

    CategoryVo addCategory(CategoryVo vo);

    CategoryVo updateCategory(CategoryVo vo);

    CategoryVo deleteCategory(CategoryVo vo);

    List<CategoryVo> findCategoryByName(CategoryVo vo);
}
