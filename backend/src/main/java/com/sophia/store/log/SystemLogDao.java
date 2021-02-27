package com.sophia.store.log;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemLogDao extends JpaRepository<SystemLog, Integer> {
}
