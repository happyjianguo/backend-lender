package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统参数
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysParam" )
public class SysParam extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//系统key
    private String sysKey;
    public static final String sysKey_field="sysKey";
//数值
    private String sysValue;
    public static final String sysValue_field="sysValue";
//描述
    private String description;
    public static final String description_field="description";
//语言 中文zh_CN 印尼语ID
    private String language;
    public static final String language_field="language";
}
