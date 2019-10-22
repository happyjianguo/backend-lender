package com.yqg.system.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * app H5 url集合表
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:51:54
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sysAppH5" )
public class SysAppH5 extends BaseEntity implements Serializable{
    private static final long serialVersionUID=1L;

//url Key
    private String urlKey;
    public static final String urlKey_field="urlKey";
//url 值
    private String urlValue;
    public static final String urlValue_field="urlValue";
//url描述
    private String urlDesc;
    public static final String urlDesc_field="urlDesc";
}
