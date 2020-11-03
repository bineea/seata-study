package com.example.servicea.service;

import com.example.servicea.dao.SampleTestARepo;
import com.example.servicea.dao.entity.SampleTestA;
import com.tryimpl.globaltransaction.annotation.GlobalTransactional;
import com.tryimpl.globaltransaction.http.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceAService {

    @Autowired
    private SampleTestARepo sampleTestARepo;

    @GlobalTransactional(isStart = true)
    @Transactional
    public void handerByServiceB() {

        SampleTestA sampleTestA = new SampleTestA();
        sampleTestA.setValue("test to handle A by B");
        sampleTestARepo.save(sampleTestA);
        //ResponseEntity<String> serviceBHandleResult = restTemplate.getForEntity("http://localhost:8280/handle/dosomething", String.class);
        //System.out.println(serviceBHandleResult.getBody());
        HttpClient.get("http://localhost:8280/handle/dosomething");
        System.out.println(1/0);
    }
}
