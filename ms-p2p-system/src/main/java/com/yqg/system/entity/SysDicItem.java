package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 字典项表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysDicItem" )
public class SysDicItem extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//字典父级ID 对应sysDict表
    private String dicId;
    public static final String dicId_field="dicId";
//字典项值
    private String dicItemValue;
    public static final String dicItemValue_field="dicItemValue";
//字典项名
    private String dicItemName;
    public static final String dicItemName_field="dicItemName";
//语言 中文zh_CN 印尼语ID
    private String language;
    public static final String language_field="language";
}
