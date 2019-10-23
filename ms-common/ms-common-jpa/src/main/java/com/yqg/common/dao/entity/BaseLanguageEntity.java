package com.yqg.common.dao.entity;

import com.yqg.common.dao.ExtendQueryCondition;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 基础对象,一般业务对象表需包含这些字段
 * Created by gao on 2017/7/28.
 */
@Entity
@Data
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class BaseLanguageEntity {

    @Id
    private String id;
    public static final String id_Field="id";

    private String uuid;
    public static final String uuid_Field="uuid";
    /**
     * 是否有效记录（0有效,其他无效）
     */
    private Integer disabled=0;
    public static final String disabled_Field="disabled";
    /**
     * 数据创建时间
     */
    private Date createTime;
    public static final String createTime_Field="createTime";
    /**
     * 数据创建者
     */
    private String createUser;
    public static final String createUser_Field="createUser";
    /**
     * 数据更新时间
     */
    private Date  updateTime;
    public static final String updateTime_Field="updateTime";
    /**
     * 数据更新者
     */
    private String  updateUser;
    public static final String updateUser_Field="updateUser";
    /**
     * 备注信息
     */
    private String remark;
    public static final String remark_Field="remark";
    /**
     * 排序字段
     */
    private Integer sort;
    public static final String sort_Field="sort";
    /**
     * 语言
     */
    private String language;
    public static final String language_Field="language";

    /**
     * 查询条件（非字段属性,特殊查询时使用）
     */
    @Transient
    private ExtendQueryCondition extendQueryCondition =new ExtendQueryCondition();

    /**
     * 是否无效记录
     * @return
     */
    public  boolean isDisabled(){
        return  this.getDisabled()!=0;//非0即无效数据（被逻辑删除）
    }

}
