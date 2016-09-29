package com.prcsteel.ec.service;

import com.prcsteel.ec.model.dto.Page;
import com.prcsteel.ec.model.query.ComplexCond;

import java.util.List;

/**
 * Created by Rolyer on 2016/4/27.
 */
public interface GenericDaoService {
    int save(Object entity);
    int insert(Object entity);
    <T> int batchInsert(List<T> entitys);
    int updateByKey(Object entity);
    int deleteByKey(Object entityInfo);
    <T> T findOne(T entity);
    <T> List<T> findAll(T entity);
    <T> List<T> findAll(T entity,String... orderBys);
    <T> List<T> findAllTop(T entity, int top, String... orderBys);
    <T> Page<T> findAll(T entity, int pageNum, int pageSize);
    <T> Page<T> findAll(T entity,int pageNum,int pageSize, String... orderBys);
    <T> T findOneComplex(Class<T> clazz, ComplexCond condition);
    <T> List<T> findAllComplex(Class<T> clazz,   ComplexCond condition);
    <T> Page<T> findAllComplexTop(Class<T> clazz, ComplexCond condition, int top,String orderBys);
    <T> Page<T> findAllComplex(Class<T> clazz,   ComplexCond condition, int pageNum, int pageSize);
    <T> Page<T> findAllComplex(Class<T> clazz,   ComplexCond condition, int pageNum, int pageSize,String... orderBys);
}
