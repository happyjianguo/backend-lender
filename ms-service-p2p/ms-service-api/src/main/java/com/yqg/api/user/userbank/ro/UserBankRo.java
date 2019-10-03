package com.yqg.api.user.userbank.ro;

import com.yqg.common.core.request.BasePageRo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 用户银行卡信息 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-08-31 10:20:30
 */
@Data
public class UserBankRo {
//用户uuid
@ApiModelProperty(value = "用户uuid", required = true)
private String userUuid;
//银行id
@ApiModelProperty(value = "银行id", required = true)
private String bankId;
//银行名称
@ApiModelProperty(value = "银行名称", required = true)
private String bankName;
//开户行简称
@ApiModelProperty(value = "开户行简称", required = true)
private String bankCode;
//银行卡号
@ApiModelProperty(value = "银行卡号", required = true)
private String bankNumberNo;
//账户持有人姓名
@ApiModelProperty(value = "账户持有人姓名", required = true)
private String bankCardName;
//绑卡状态(0=未验证，1=待验证,2=成功,3=失败)
@ApiModelProperty(value = "绑卡状态(0=未验证，1=待验证,2=成功,3=失败)", required = true)
private Integer status;
//顺序
@ApiModelProperty(value = "顺序", required = true)
private Integer bankorder;
//是否是最新的标识
@ApiModelProperty(value = "是否是最新的标识", required = true)
private Integer isRecent;
//默认0超级投资人1
@ApiModelProperty(value = "默认0超级投资人1", required = true)
private Integer type;
//排序自增字段
@ApiModelProperty(value = "排序自增字段", required = true)
private Integer sort;
}

