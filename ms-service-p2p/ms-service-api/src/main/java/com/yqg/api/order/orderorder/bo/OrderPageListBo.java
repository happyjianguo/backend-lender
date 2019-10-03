package com.yqg.api.order.orderorder.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderPageListBo {
    private String id;

    private String mobile;

    private BigDecimal amountBuy;   //购买金额
////(1.投资处理中 2.投资失败 3,投资成功  4.过期 5.回款成功 6.回款失败 7.回款处理中 )
    private Integer status;          //   待支付=投资处理中     待匹配=投资成功       待回款=匹配成功       回款完成=回款成功

    private Date createTime;        //下单时间

    private Date buyTime;           //购买时间

    private BigDecimal yearRateFin; //年化收益率

    private Date dueTime;           //到期日

    private Integer productType;    //购买产品类型

    private String realName;        //真实姓名

    private Date bondXatchingTime;  //债券匹配时间

    private Integer borrowingTerm;   //取productConf表的borrowingTerm    字段写错了    '借款期限(月)',

}
