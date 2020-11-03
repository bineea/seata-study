package com.example.serviceb.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SampleTestBDao {

    @Insert("insert into sample_test_b(value) values(#{value})")
    void insert(@Param("value") String value);
}
