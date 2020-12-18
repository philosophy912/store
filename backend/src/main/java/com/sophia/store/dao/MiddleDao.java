package com.sophia.store.dao;

import com.sophia.store.entity.po.Middle;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MiddleDao extends JpaRepository<Middle, Integer>, JpaSpecificationExecutor<Middle> {
    @NotNull
    Page<Middle> findAll(@NotNull Pageable pageable);

    List<Middle> findByName(String name);

    List<Middle> findByNameLike(String name);
}
