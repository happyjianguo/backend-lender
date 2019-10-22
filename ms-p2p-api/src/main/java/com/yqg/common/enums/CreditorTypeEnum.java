package com.yqg.common.enums;

import lombok.Getter;

/**
 * Remark:
 * Created by huwei on 19.7.2.
 */
@Getter
public enum CreditorTypeEnum {
    NORMAL(1),//普通
    STAGING(2),//分期
    EXTENSION(3),//展期
    ;

    private int type;

    private CreditorTypeEnum(int type){
        this.type = type;

    }

}
