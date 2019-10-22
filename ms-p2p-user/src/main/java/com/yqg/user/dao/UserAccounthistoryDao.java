package com.yqg.user.dao;

import com.yqg.api.user.useraccounthistory.bo.UserAccountHistoryTotalBo;
import com.yqg.common.dao.BaseDao;
import com.yqg.user.entity.UserAccountHistryTotal;
import com.yqg.user.entity.UserAccounthistory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 用户账户明细表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-11 10:43:36
 */

@Repository
public interface UserAccounthistoryDao extends BaseDao<UserAccounthistory, String> {

    @Query(value = "SELECT userUuid,SUM(amount) FROM userAccountHistory" +
            " WHERE TYPE=:type AND disabled=0 AND DATEDIFF(createTime,:date)=0  " +
            "AND userUuid IN(SELECT id FROM userUser WHERE userType=3) GROUP BY userUuid",nativeQuery = true)
    List<Object[]> getUserAccountHistoryTotal(@Param("type")String type, @Param("date") Date date);

    //@Modifying
    @Query(value = "SELECT SUM(amount) AS amount FROM userAccountHistory WHERE disabled = 0 AND " +
            "createTime>?1 AND businessType = '购买债权' AND userUuid=?2 ",nativeQuery = true)
    Object[] getSuccessInvest(Date date,String userUuid);//(NOW() - interval 24 hour)
}
