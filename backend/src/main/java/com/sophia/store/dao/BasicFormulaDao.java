package com.sophia.store.dao;

import com.sophia.store.entity.po.BasicFormula;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BasicFormulaDao extends JpaRepository<BasicFormula, Integer>, JpaSpecificationExecutor<BasicFormula> {
    @NotNull
    Page<BasicFormula> findAll(@NotNull Pageable pageable);

    List<BasicFormula> findByName(String name);

    List<BasicFormula> findByNameLike(String name);
}
