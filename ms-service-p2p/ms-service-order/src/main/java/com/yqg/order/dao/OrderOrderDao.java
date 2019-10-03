package com.yqg.order.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.order.entity.OrderOrder;
import com.yqg.order.entity.Scatterstandard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 债权人的基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 10:40:02
 */
@Repository
public interface OrderOrderDao extends BaseDao<OrderOrder, String> {

    //查询用户订单
    @Query(value = "select * from orderOrder where disabled = 0 and userUuid = :userUuid and createTime > :startTime and createTime < :endTime ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from orderOrder where disabled = 0 and userUuid = :userUuid and createTime > :startTime and createTime < :endTime",
            nativeQuery = true)
    Page<OrderOrder> findByUserUuid(@Param("userUuid")String userUuid, @Param("startTime")Date startTime,@Param("endTime")Date endTime,Pageable pageable);

    //查询用户订单
    @Query(value = "select * from orderOrder where disabled = 0 and userUuid = :userUuid and status = :status and createTime > :startTime and createTime < :endTime ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from orderOrder where disabled = 0 and userUuid = :userUuid and status = :status and createTime > :startTime and createTime < :endTime",
            nativeQuery = true)
    Page<OrderOrder> findByStatus(@Param("userUuid")String userUuid,@Param("status")int status, @Param("startTime")Date startTime,@Param("endTime")Date endTime,Pageable pageable);

    //查询用户订单
    @Query(value = "select * from orderOrder where disabled = 0 and userUuid = :userUuid and productType = :productType and createTime > :startTime and createTime < :endTime ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from orderOrder where disabled = 0 and userUuid = :userUuid and productType = :productType and createTime > :startTime and createTime < :endTime",
            nativeQuery = true)
    Page<OrderOrder> findByProductType(@Param("userUuid")String userUuid,@Param("productType")int productType, @Param("startTime")Date startTime,@Param("endTime")Date endTime,Pageable pageable);


    //查询用户订单
    @Query(value = "select * from orderOrder where disabled = 0 and userUuid = :userUuid and productType = :productType and status = :status and createTime > :startTime and createTime < :endTime ORDER BY ?#{#pageable}",
            countQuery = "select count(*) from orderOrder where disabled = 0 and userUuid = :userUuid and productType = :productType and status = :status and createTime > :startTime and createTime < :endTime",
            nativeQuery = true)
    Page<OrderOrder> findByUserUuid(@Param("userUuid")String userUuid,@Param("productType")int productType, @Param("status")int status,@Param("startTime")Date startTime,@Param("endTime")Date endTime,Pageable pageable);



    @Query("SELECT o FROM OrderOrder o where status in(1,2) and disabled=0 and createTime<:date")
    List<OrderOrder> deal30MinLaterOrder(@Param("date") Date date);
}
