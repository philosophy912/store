package com.sophia.store.dao;

import com.sophia.store.entity.po.Material;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MaterialDao extends JpaRepository<Material, Integer>, JpaSpecificationExecutor<Material> {
    @NotNull
    Page<Material> findAll(@NotNull Pageable pageable);

    List<Material> findByName(String name);

    List<Material> findByNameLike(String name);
}
