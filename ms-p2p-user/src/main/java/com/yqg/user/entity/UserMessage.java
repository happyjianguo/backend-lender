package com.yqg.user.entity;

import com.yqg.common.dao.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Lixiangjun on 2019/5/20.
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userMessage" )
public class UserMessage extends BaseEntity implements Serializable {
    private static final long serialVersionUID=1L;

    //用户uuid
    private String userUuid;
    public static final String userUuid_field="userUuid";
    //消息内容
    private String content;
    public static final String content_field="content";
    //标题
    private String title;
    public static final String title_field="title";
    //是否已读
    private Integer status;
    public static final String status_field="status";
}
