package com.yqg.order.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.order.entity.Scatterstandard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.keyvalue.repository.config.QueryCreatorType;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 散标表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Repository
public interface ScatterstandardDao extends BaseDao<Scatterstandard, String> {

    @Query("SELECT s FROM Scatterstandard s where status in(1) and disabled=0 and createTime<:date and packageNo='0'")
    List<Scatterstandard> query6Hours(@Param("date") Date date);


}
