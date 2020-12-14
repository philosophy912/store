package com.sophia.store.dao;

import com.sophia.store.entity.po.Category;
import com.sophia.store.entity.po.Food;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FoodDao extends JpaRepository<Food, Integer>, JpaSpecificationExecutor<Food> {
    @NotNull
    Page<Food> findAll(@NotNull Pageable pageable);

    List<Food> findByName(String name);

    List<Food> findByNameLike(String name);

    List<Food> findByNameLikeAndCategory(String name, Category category);

    List<Food> findByCategory(Category category);
}
