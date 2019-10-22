package com.yqg.common.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PayParamContants {


    public String PAY_BUY_MAX = "pay:buy:max";
    public String TIMER_6HOURS_DEAL = "timer:6hours:deal";
    public String TIMER_30MIN_DEAL = "timer:30min:deal";
    public String LOCK_CREDITORNO = "lock:creditorno";
    //打款锁
    public String LOCK_LOAN_CREDITORNO = "lock:loan:creditorno";
    //放款清分锁
    public String LOCK_LOAN_DISTRIBUTION_CREDITORNO = "lock:distribution:creditorno";

    public String SCATTERSTANDARD_YEAR_RATE = "scatterstandard:year:rate";

    //用户操作码
    public String USER_REFUND_PASSWORD_KEY = "user:refund:password";
    //用户默认头像
    public String USER_HEADIMAGE = "user:headImage";

    @Value("${third.pay.X-AUTH-TOKEN}")
    public String payToken;
    @Value("${third.pay.income.depositUrl}")
    public String depositUrl;
    @Value("${third.pay.income.depositConfirmUrl}")
    public String depositConfirmUrl;
    @Value("${third.pay.income.currency}")
    public String currency;

}
