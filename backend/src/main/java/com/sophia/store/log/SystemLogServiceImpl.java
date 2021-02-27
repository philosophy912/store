package com.sophia.store.log;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SystemLogServiceImpl implements SystemLogService {
    @Resource
    private SystemLogDao systemLogDao;

    @Override
    public void add(SystemLog systemLog) {
        systemLogDao.save(systemLog);
    }
}
