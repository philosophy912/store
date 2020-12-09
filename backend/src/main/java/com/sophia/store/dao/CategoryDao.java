package com.sophia.store.dao;

import com.sophia.store.entity.po.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    @NotNull
    Page<Category> findAll(@NotNull Pageable pageable);

    List<Category> findByName(String name);

    List<Category> findByNameLike(String name);
}
