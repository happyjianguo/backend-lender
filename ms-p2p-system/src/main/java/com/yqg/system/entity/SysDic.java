package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 字典表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysDic" )
public class SysDic extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//字典名
    private String dicName;
    public static final String dicName_field="dicName";
//字典编码
    private String dicCode;
    public static final String dicCode_field="dicCode";
//语言 中文zh_CN 印尼语ID
    private String language;
    public static final String language_field="language";
}
