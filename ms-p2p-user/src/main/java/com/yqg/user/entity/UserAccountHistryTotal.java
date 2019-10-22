package com.yqg.user.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Remark:
 * Created by huwei on 19.5.24.
 */
@Data
@Entity
public class UserAccountHistryTotal {
    @Id
    private int id;
    private String userUuid;
    private BigDecimal amount;

    public UserAccountHistryTotal(String userUuid, BigDecimal amount){
        this.userUuid = userUuid;
        this.amount = amount;
    }

    public UserAccountHistryTotal(){}

}
