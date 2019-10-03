package com.yqg.user.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户银行卡信息
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userBank" )
public class UserBank extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

    //用户uuid
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //银行id
    private String bankId;
    public static final String bankId_field="bankId";
    //银行名称
    private String bankName;
    public static final String bankName_field="bankName";
    //开户行简称
    private String bankCode;
    public static final String bankCode_field="bankCode";
    //银行卡号
    private String bankNumberNo;
    public static final String bankNumberNo_field="bankNumberNo";
    //账户持有人姓名
    private String bankCardName;
    public static final String bankCardName_field="bankCardName";
    //绑卡状态(0=未验证，1=待验证,2=成功,3=失败)
    private Integer status;
    public static final String status_field="status";
    //顺序
    private Integer bankorder;
    public static final String bankorder_field="bankorder";
    //是否是最新的标识
    private Integer isRecent;
    public static final String isRecent_field="isRecent";
    //默认0超级投资人1
    private Integer type;
    public static final String type_field="type";
}
