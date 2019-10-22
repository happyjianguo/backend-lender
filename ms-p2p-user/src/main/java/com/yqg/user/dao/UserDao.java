package com.yqg.user.dao;

import com.yqg.api.user.useraccount.bo.OrganOrSuperUserBo;
import com.yqg.common.dao.BaseDao;
import com.yqg.user.entity.UserUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 用户基本信息表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Repository
public interface UserDao extends BaseDao<UserUser, String> {

//    @Query(value = "SELECT uu.* FROM userUser uu left join userBank ub on uu.id = ub.userUuid where " +
//            "uu.disabled=0 and uu.userType = :userType AND ub.bankCode = :bankCode and ub.disabled = 0",nativeQuery = true)
//    List<UserUser> getUserByTypeAndBankCode(@Param("userType") Integer userType,@Param("bankCode") String bankCode);
//
//    @Query(value = "SELECT uu.id,uu.userName,uu.mobileNumber,ub.bankNumberNo FROM userUser uu left join userBank ub on uu.id = ub.userUuid where " +
//            "uu.disabled=0 and uu.userType = :userType and ub.disabled = 0",nativeQuery = true)
//    List<OrganOrSuperUserBo> getOrganOrSuperAccount(@Param("userType") Integer userType);

}
