package com.yqg.system.dao;

import com.yqg.common.dao.BaseDao;
import com.yqg.system.entity.SysSmsMessageInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 短信信息表
 * Created by Lixiangjun on 2019/6/5.
 */
@Repository
public interface SysSmsMessageDao extends BaseDao<SysSmsMessageInfo, String> {

    @Query(value = "SELECT * FROM sysSmsMessageInfo  where mobile= :mobile and smsType in (2,3) and disabled=0 order by sort desc",nativeQuery = true)
    List<SysSmsMessageInfo> getUserSmsCode(@Param("mobile")String mobile);
}
