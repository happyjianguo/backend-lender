package com.yqg.common.dao;

import com.yqg.common.context.ApplicationContextProvider;
import com.yqg.common.dao.entity.BaseEntity;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.utils.MD5Util;
import com.yqg.common.utils.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 基础Dao实现类
 * Created by gao on 2017/7/28.
 *
 * @param <T>
 * @param <ID>
 */
public class BaseDaoImpl<T extends BaseEntity, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID> {

    private Logger logger = LoggerFactory.getLogger(getClass());


    private final EntityManager entityManager;

    public BaseDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    private RedisUtil redisUtil = null;

    /**
     * 获取redistUtil
     *
     * @return
     */
    private RedisUtil getRedisUtil() throws BusinessException {
        if (null == redisUtil) {
            redisUtil = ApplicationContextProvider.getBean(RedisUtil.class);
        }
        return redisUtil;
    }

    @Override
    public T findOneById(String id, T result) throws BusinessException {
        result.setId(id);
        result = getOneResult(result);
        return result;
    }


    @Override
    public T findOne(T entity) throws BusinessException {
        return getOneResult(entity);
    }

    /**
     * 取出单条数据
     *
     * @param result
     * @return
     * @throws BusinessException
     */
    private T getOneResult(T result) throws BusinessException {
        result.setDisabled(0);
        List<T> resultList = findForList(result);
        if (CollectionUtils.isEmpty(resultList)) {
            result = null;
        } else if (resultList.size() > 1) {
            logger.error("查询单条数据,返回{}条数据。查询结果{}", resultList.size(), result);
            throw new BusinessException(BaseExceptionEnums.RESULT_MORE_ERROR);
        } else {
            result = resultList.get(0);
        }
        return result;
    }

    @Override
    public Page<T> findForPage(T entity, Pageable pageable) throws BusinessException {
        String queryCachKey = this.getQueryKey(entity, pageable);
        //使用缓存
        Page<T> pageResult =null;
        if (entity.getUsedCache()) {
            pageResult = getRedisUtil().getPage(BaseRedisKeyEnums.QUERY_CACHE_KEY.appendToDefaultKey(queryCachKey),
                    (Class<T>) entity.getClass(), pageable);
        }
        if (pageResult == null) {
            pageResult = findAll(byAuto(entityManager, entity), pageable);
            if (entity.getUsedCache()) {
                //查询结果缓存
                getRedisUtil().set(BaseRedisKeyEnums.QUERY_CACHE_KEY.appendToDefaultKey(queryCachKey), pageResult);
                logger.info("缓存page查询结果key:{}", queryCachKey);
            }
        } else {

            logger.info("缓存命中,直接使用缓存page数据");
        }
        return pageResult;
    }

    @Override
    public List<T> findForList(T entity, Sort sort) throws BusinessException {
        List<T> result = null;
        String queryCachKey = this.getQueryKey(entity, sort);
        //使用缓存
        if (entity.getUsedCache()) {
            result = getRedisUtil().getList(BaseRedisKeyEnums.QUERY_CACHE_KEY.appendToDefaultKey(queryCachKey), (Class<T>) entity.getClass());
        }
        if (result == null) {
            result = findAll(byAuto(entityManager, entity), sort);
            if (entity.getUsedCache()) {
                //查询结果缓存
                getRedisUtil().set(BaseRedisKeyEnums.QUERY_CACHE_KEY.appendToDefaultKey(queryCachKey), result);
                logger.info("缓存list查询结果key:{}", queryCachKey);
            }
        } else {
            logger.info("缓存命中,直接使用缓存list数据");
        }
        return result;
    }

    /**
     * 生产缓存key
     *
     * @param entity
     * @param objects
     * @return
     */
    private String getQueryKey(T entity, Object... objects) throws BusinessException {
        //查询表的md5,查询条件md5,（查询条件+查询表)md5
        String queryCachKey = MD5Util.md5LowerCase(getQueryTableKey(entity), entity, objects);
        logger.info("生成查询结果缓存key:{}", queryCachKey);

        return queryCachKey;
    }

    /**
     * 查询表级key
     *
     * @param entity
     * @return
     */
    private String getQueryTableKey(T entity) throws BusinessException {
        Table table = entity.getClass().getAnnotation(Table.class);
        String queryTableKey = getRedisUtil().get(BaseRedisKeyEnums.QUERY_TABLE_KEY.appendToDefaultKey(table.name()));
        logger.info("获取表{}缓存key:{}", table.name(), queryTableKey);
        if (StringUtils.isEmpty(queryTableKey)) {
            queryTableKey = refreshQueryTableKey(entity);
        }

        return queryTableKey;
    }

    /**
     * 更新表级缓存
     *
     * @param entity
     */
    private String refreshQueryTableKey(T entity) throws BusinessException {
        Table table = entity.getClass().getAnnotation(Table.class);
        String queryTableKey = UuidUtil.create();
        getRedisUtil().set(BaseRedisKeyEnums.QUERY_TABLE_KEY.appendToDefaultKey(table.name()), queryTableKey);
        logger.info("更新表{}缓存key:{}", table.name(), queryTableKey);
        return queryTableKey;
    }

    @Override
    public List<T> findForList(T entity) throws BusinessException {
        return findForList(entity, new Sort(Sort.Direction.ASC, BaseEntity.sort_Field));
    }

    @Override
    public String addOne(T entity) throws BusinessException {
        //更新表级缓存
        this.refreshQueryTableKey(entity);
        String id = UuidUtil.create();
        entity.setId(id);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        this.saveAndFlush(entity);
        return id;
    }

    @Override
    public void deleteOne(T entity) throws BusinessException {
        if (entity != null) {
            //更新表级缓存
            this.refreshQueryTableKey(entity);
            entity.setDisabled(1);//逻辑删除
            this.updateOne(entity);
        } else {
            logger.info("删除的数据不存在");
        }
    }

    @Override
    public void updateOne(T entity) throws BusinessException {
        if (StringUtils.isEmpty(entity.getId())) {
            throw new BusinessException(BaseExceptionEnums.PARAM_ERROR.setCustomMessage("id不能为空"));
        }
        this.saveAndFlush(entity);
        //更新表级缓存
        this.refreshQueryTableKey(entity);
    }

    /**
     * 字段值自动解析构造查询条件
     */
    private <T extends BaseEntity> Specification<T> byAuto(final EntityManager entityManager, final T example) {

        final Class<T> type = (Class<T>) example.getClass();
        /**
         * 构造查询条件构造器
         */
        return new Specification<T>() {

            ExtendQueryCondition extendQueryCondition = example.getExtendQueryCondition();

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                EntityType<T> entity = entityManager.getMetamodel().entity(type);

//                Set<? extends Attribute<? super T, ?>> declaredAttributes = entity.getSupertype().getDeclaredAttributes();
//                for (Attribute<? super T, ?> superAttr : declaredAttributes) {
//                    Object attrValue = getValue(example, superAttr);
//                    if (attrValue != null) {
//                        predicates.add(cb.equal(root.get(superAttribute(entity, superAttr.getName(), attrValue.getClass())),
//                                attrValue));
//                    }
//                }

//                Set<Attribute<T, ?>> attributes = entity.getDeclaredAttributes();
                Set<Attribute<? super T, ?>> attributes = entity.getAttributes();
                for (Attribute<? super T, ?> attr : attributes) {
                    Object attrValue = getValue(example, attr);
                    String attrName = attr.getName();

                    List<Object> inValues = extendQueryCondition.getInQueryMap().get(attrName);
                    List<Object> notInValues = extendQueryCondition.getNotInQueryMap().get(attrName);
                    Map<ExtendQueryCondition.CompareType, Object> compareInfo = extendQueryCondition.getCompareQueryMap().get(attrName);
                    if (!CollectionUtils.isEmpty(inValues)) {//是否in查询
//                        predicates.add(cb.in(root.in(inValues)));
//                        predicates.add(cb.in(root.get(attribute(entity, attrName, attr.getJavaType()))).in(inValues));
                        predicates.add(root.get(attrName).in(inValues));

//                        predicates.add(cb.in(attribute(entity, attrName,attr.getJavaType()),root.in(inValues)));
                    }else if (!CollectionUtils.isEmpty(notInValues)) {//是否not in查询
                        predicates.add(cb.not(root.get(attrName).in(notInValues)));
                    }
                    else if (!CollectionUtils.isEmpty(compareInfo)) {//是否比较查询
                        for (ExtendQueryCondition.CompareType compareType : compareInfo.keySet()) {
                            switch (compareType) {
                                case GT:
                                    predicates.add(cb.gt(root.get(attrName), (Number) compareInfo.get(compareType)));
                                    break;
                                case GTE:
                                    predicates.add(cb.ge(root.get(attrName), (Number) compareInfo.get(compareType)));
                                    break;
                                case LT:
                                    predicates.add(cb.lt(root.get(attrName), (Number) compareInfo.get(compareType)));
                                    break;
                                case LTE:
                                    predicates.add(cb.le(root.get(attrName), (Number) compareInfo.get(compareType)));
                                    break;
                                case NE:
//                                    predicates.add(cb.notEqual(root.get(attribute(entity, attrName, attr.getJavaType())),
//                                            compareInfo.get(compareType)));
                                    predicates.add(cb.notEqual(root.get(attrName),compareInfo.get(compareType)));
                                    break;
                                case GTE_TIME:
                                    //大于或等于传入时间
//                                    predicates.add(cb.greaterThanOrEqualTo(root.get("commitTime").as(String.class), stime));
//                                    predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("sendDate"), stime));
                                    predicates.add(cb.greaterThanOrEqualTo(root.<Date>get(attrName), (Date) compareInfo.get(compareType)));
                                    break;
                                case LTE_TIME:
                                    //小于或等于传入时间
//                                    predicates.add(cb.lessThanOrEqualTo(root.get("commitTime").as(String.class), etime));
                                    predicates.add(cb.lessThanOrEqualTo(root.<Date>get(attrName), (Date) compareInfo.get(compareType)));
                                    break;
                                case GT_TIME:
                                    //大于或等于传入时间
//                                    predicates.add(cb.greaterThanOrEqualTo(root.get("commitTime").as(String.class), stime));
//                                    predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("sendDate"), stime));
                                    predicates.add(cb.greaterThan(root.<Date>get(attrName), (Date) compareInfo.get(compareType)));
                                    break;
                                case LT_TIME:
                                    //小于或等于传入时间
//                                    predicates.add(cb.lessThanOrEqualTo(root.get("commitTime").as(String.class), etime));
                                    predicates.add(cb.lessThan(root.<Date>get(attrName), (Date) compareInfo.get(compareType)));
                                    break;
                            }
                        }

                    } else if (attrValue != null) {//普通查询
                        if (attr.getJavaType() == String.class) {
                            if (!StringUtils.isEmpty(attrValue)) {
                                if (isLikeQueyFields(attrName)) {//模糊查询
                                    predicates.add(cb.like(root.get(attrName),
                                            "%" + attrValue + "%"));
                                } else {//精确查询
                                    predicates.add(cb.equal(root.get(attrName), attrValue));
                                }
                            }else {//精确查询
                                predicates.add(cb.equal(root.get(attrName), attrValue));
                            }
                        } else {
                            predicates.add(cb.equal(root.get(attrName), (Number) attrValue));
                        }
                    }
                }

                Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
                return predicates.isEmpty() ? cb.conjunction() : cb.and(predicatesArray);//11
            }

            /**
             * 是否精确查询字符串
             * @param attrName
             * @return
             */
            private boolean isLikeQueyFields(String attrName) {
                List<String> preciseQueyFields = extendQueryCondition.getLikeQueyFields();
                for (String fieldName : preciseQueyFields) {
                    if (fieldName.equals(attrName)) {
                        return true;
                    }
                }
                return false;
            }


            /**
             * 获取属性值
             */
            private <T> Object getValue(T example, Attribute<T, ?> attr) {
                return ReflectionUtils.getField((Field) attr.getJavaMember(), example);
            }

            /**
             * 本类属性字段
             */
            private <E, T> SingularAttribute<T, E> attribute(EntityType<T> entity, String fieldName,
                                                             Class<E> fieldClass) {
                return entity.getDeclaredSingularAttribute(fieldName, fieldClass);
            }

            /**
             * 父类属性字段
             */
            private <E, T> SingularAttribute<? super T, ?> superAttribute(EntityType<T> entity, String fieldName,
                                                                          Class<E> fieldClass) {
                return entity.getSupertype().getDeclaredSingularAttribute(fieldName);
            }

        };

    }
}
