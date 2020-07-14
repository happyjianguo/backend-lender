package com.yqg.order.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.order.entity.OrderOrder;
import com.yqg.order.entity.OrderScatterStandardRel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 订单散标关系表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-11-27 15:04:24
 */
@Repository
public interface OrderScatterStandardRelDao extends BaseDao<OrderScatterStandardRel, String> {

}
