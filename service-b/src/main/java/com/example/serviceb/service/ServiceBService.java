package com.example.serviceb.service;

import com.example.serviceb.dao.SampleTestBDao;
import com.tryimpl.globaltransaction.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceBService {

    @Autowired
    private SampleTestBDao sampleTestBDao;

    @GlobalTransactional
    @Transactional
    public void doSomething() {
        sampleTestBDao.insert("test to handle B");
    }
}
