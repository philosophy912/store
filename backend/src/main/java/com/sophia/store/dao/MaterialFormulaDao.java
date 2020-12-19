package com.sophia.store.dao;

import com.sophia.store.entity.po.MaterialFormula;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface MaterialFormulaDao extends JpaRepository<MaterialFormula, Integer>, JpaSpecificationExecutor<MaterialFormula> {
    @NotNull
    Page<MaterialFormula> findAll(@NotNull Pageable pageable);


}
