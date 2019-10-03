package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 行政区划表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysDist" )
public class SysDist extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//多语言UUID可重复
    private String uuid;
    public static final String uuid_field="uuid";
//区划名称
    private String distName;
    public static final String distName_field="distName";
//区划编号
    private String distCode;
    public static final String distCode_field="distCode";
//区划级别
    private String distLevel;
    public static final String distLevel_field="distLevel";
//上级区划编号
    private String parentCode;
    public static final String parentCode_field="parentCode";
//语言 中文zh_CN 印尼语ID
    private String language;
    public static final String language_field="language";
}
