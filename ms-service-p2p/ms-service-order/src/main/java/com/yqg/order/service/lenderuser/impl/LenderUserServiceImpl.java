package com.yqg.order.service.lenderuser.impl;

import com.alibaba.fastjson.JSONObject;
import com.yqg.api.order.lenderuser.ro.LenderUserInfoRo;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.httpclient.RestTemplateUtil;
import com.yqg.common.utils.BeanCoypUtil;
import com.yqg.order.dao.LenderUserDao;
import com.yqg.order.entity.Creditorinfo;
import com.yqg.order.entity.LenderUser;
import com.yqg.order.service.lenderuser.LenderUserService;
import com.yqg.pay.service.PayCommonServiceImpl;
import com.yqg.pay.util.ToolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 借款人信息表
 *
 * @author wu
 * @email wu@yishufu.com
 * @date 2018-09-03 11:15:41
 */
@Service("lenderUserService")
public class LenderUserServiceImpl extends PayCommonServiceImpl implements LenderUserService {
    @Autowired
    protected LenderUserDao lenderuserDao;
    @Autowired
    RestTemplateUtil restTemplateUtil;


    @Override
    public LenderUser findById(String id) throws BusinessException {
        return this.lenderuserDao.findOneById(id, new LenderUser());
    }

    @Override
    public <E> E findById(String id, Class<E> boClass) throws BusinessException {
        LenderUser lenderuser = findById(id);
        E bo = BeanCoypUtil.copyToNewObject(lenderuser, boClass);
        return bo;
    }

    @Override
    public LenderUser findOne(LenderUser entity) throws BusinessException {
        return this.lenderuserDao.findOne(entity);
    }

    @Override
    public <E> E findOne(LenderUser entity, Class<E> boClass) throws BusinessException {
        LenderUser lenderuser = findOne(entity);
        E bo = BeanCoypUtil.copyToNewObject(lenderuser, boClass);
        return bo;
    }


    @Override
    public List<LenderUser> findList(LenderUser entity) throws BusinessException {
        return this.lenderuserDao.findForList(entity);
    }

    @Override
    public <E> List<E> findList(LenderUser entity, Class<E> boClass) throws BusinessException {
        List<LenderUser> lenderuserList = findList(entity);
        List<E> boList = new ArrayList<>();
        for (LenderUser lenderuser : lenderuserList) {
            E bo = BeanCoypUtil.copyToNewObject(lenderuser, boClass);
            boList.add(bo);
        }
        return boList;
    }

    @Override
    @Transactional
    public String addOne(LenderUser entity) throws BusinessException {
        return lenderuserDao.addOne(entity);
    }

    @Override
    @Transactional
    public void deleteById(String id) throws BusinessException {
        lenderuserDao.deleteOne(findById(id));
    }

    @Override
    @Transactional
    public void deleteOne(LenderUser entity) throws BusinessException {
        this.lenderuserDao.deleteOne(entity);
    }

    @Override
    @Transactional
    public void updateOne(LenderUser entity) throws BusinessException {
        this.lenderuserDao.updateOne(entity);
    }

    /**
     * 查询借款人信息，如果表里没有就去doit查询借款人信息
     * */
    @Override
    @Transactional
    public LenderUser selectLenderUserInfoFromDoit(LenderUserInfoRo ro) throws BusinessException {

        Creditorinfo  creditorinfoEntity= new Creditorinfo();
        creditorinfoEntity.setDisabled(0);
        creditorinfoEntity.setCreditorNo(ro.getCreditorNo());
        Creditorinfo creditorinfoResult = creditorinfoService.findOne(creditorinfoEntity);

        LenderUser lenderUserQuery = new LenderUser();
        lenderUserQuery.setLenderId(creditorinfoResult.getLenderId());
        lenderUserQuery.setDisabled(0);

        LenderUser lenderUserResult = this.findOne(lenderUserQuery);

        if(lenderUserResult!=null){

            return lenderUserResult;

        }else {

            JSONObject jsonObject = orderOrderService.queryLenderInfo(creditorinfoResult.getLenderId());

            if(jsonObject.getInteger("code")==0){

                JSONObject data = new JSONObject(jsonObject.getJSONObject("data"));
                LenderUser entity = new LenderUser();
                entity.setCreateTime(new Date());
                entity.setUpdateTime(new Date());
                entity.setAcdemic(data.getString("academic"));
                entity.setAddress(data.getString("liveAddress"));
                entity.setAge(data.getInteger("age"));
                entity.setBirty(data.getString("birthday"));
                entity.setEmail(ToolUtils.getYanMa(data.getString("email")));
                entity.setIdCardNo(ToolUtils.getYanMa(data.getString("idCardNumber")));
                entity.setIdentidy(data.getString("userRole"));
                entity.setIsBankCardAuth(data.getString("hasBank"));
                entity.setIsFamilyCardAuth(data.getString("hasFamilyCard"));
                entity.setIsIdentidyAuth(data.getString("hasIdentity"));
                entity.setIsInsuranceCardAuth(data.getString("hasInsuranceCard"));
                entity.setIsLindManAuth(data.getString("hasContact"));
                entity.setIsMarried(data.getString("maritalStatus"));
                entity.setLenderId(data.getString("userUuid"));
                entity.setMobile(ToolUtils.getYanMa(data.getString("teleNumber")));
                entity.setName(data.getString("realName"));
                entity.setReligion(data.getString("religion"));
                entity.setSex(data.getString("sex"));

                this.addOne(entity);

                return this.findOne(lenderUserQuery);
            }

        }


        return null;
    }
}