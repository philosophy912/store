package com.sophia.store.dao;

import com.sophia.store.entity.po.Basic;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BasicDao extends JpaRepository<Basic, Integer>, JpaSpecificationExecutor<Basic> {
    @NotNull
    Page<Basic> findAll(@NotNull Pageable pageable);

    List<Basic> findByName(String name);

    List<Basic> findByNameLike(String name);
}
