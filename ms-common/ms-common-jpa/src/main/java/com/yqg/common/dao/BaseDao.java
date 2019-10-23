package com.yqg.common.dao;

import com.yqg.common.dao.entity.BaseEntity;
import com.yqg.common.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * 基础Dao接口
 * Created by gao on 2017/7/28.
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseDao<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 根据主键查询
     *
     * @param id
     * @param result 结果对象
     * @return
     */
    T findOneById(String id, T result) throws BusinessException;


    /**
     * 根据条件查询
     * @param entity
     * @return
     * @throws BusinessException
     */
    T findOne(T entity)throws BusinessException;

    /**
     * 分页查询
     *
     * @param entity 查询条件
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<T> findForPage(T entity, Pageable pageable) throws BusinessException;

    /**
     * 列表查询
     *
     * @param entity 查询条件
     * @param sort 排序参数
     * @return 结果
     */
    List<T> findForList(T entity, Sort sort) throws BusinessException;

    /**
     * 列表查询
     *
     * @param entity 查询条件
     * @return 结果
     */
    List<T> findForList(T entity) throws BusinessException;

    /**
     * 新增对象
     *
     * @param entity 实体
     * @return 是否成功
     */
    String addOne(T entity) throws BusinessException;

    /**
     * 根据id删除
     *
     * @param entity 实体
     * @return 是否成功
     * @throws BusinessException
     */
    void deleteOne(T entity) throws BusinessException;

    /**
     * 根据id更新对象
     *
     * @param entity 实体
     * @return 是否成功
     * @throws BusinessException
     */
    void updateOne(T entity) throws BusinessException;


}
