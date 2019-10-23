package com.yqg.common.core;

import com.yqg.common.exceptions.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基础服务接口
 * Created by gao on 2017/12/21.
 */
public interface BaseService<T> {

    /**
     * 新增对象
     *
     * @param entity entity
     * @return 是否成功
     * @throws BusinessException
     */
    String addOne(T entity) throws BusinessException;

    /**
     * 根据id查询对象
     *
     * @param id 主键
     * @return entity
     * @throws BusinessException
     */
    T findById(String id) throws BusinessException;

    /**
     * 根据id查询对象并转化
     *
     * @param id 主键
     * @param boClass 目标对象
     * @param <E> 目标对象类型
     * @return <E> 目标对象
     * @throws BusinessException
     */
    <E> E findById(String id, Class<E> boClass) throws BusinessException;

    /**
     * 根据对象参数查对象
     *
     * @param entity entity
     * @return entity
     * @throws BusinessException
     */
    T findOne(T entity) throws BusinessException;

    /**
     * 根根据对象参数查对象并转化成目标对象
     *
     * @param entity 对象参数
     * @param boClass     目标对象
     * @param <E>    目标对象类型
     * @return <E> 目标对象
     * @throws BusinessException
     */
    <E> E findOne(T entity, Class<E> boClass) throws BusinessException;

    /**
     * 根据条件对象查询对象列表
     *
     * @param entity entity
     * @return entityList
     * @throws BusinessException
     */
    List<T> findList(T entity) throws BusinessException;

    /**
     * 根根据对象参数查对象list并转化成目标对象
     *
     * @param entity 对象参数
     * @param boClass     目标对象
     * @param <E>    目标对象类型
     * @return List<E> 目标对象列表
     * @throws BusinessException
     */
    <E> List<E> findList(T entity, Class<E> boClass) throws BusinessException;

    /**
     * 更新对象
     *
     * @param entity 业务对象
     * @throws BusinessException
     */
    void updateOne(T entity) throws BusinessException;

    /**
     * 根据id删除
     *
     * @param id 主键
     * @throws BusinessException
     */
    void deleteById(String id) throws BusinessException;

    /**
     * 根据对象删除
     *
     * @param entity 实体类
     * @throws BusinessException
     */
    void deleteOne(T entity) throws BusinessException;
}
