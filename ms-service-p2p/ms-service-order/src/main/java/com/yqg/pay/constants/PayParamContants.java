package com.yqg.pay.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PayParamContants {


    public String PAY_BUY_MAX = "pay:buy:max";
    public String TIMER_6HOURS_DEAL = "timer:6hours:deal";
    public String TIMER_30MIN_DEAL = "timer:30min:deal";
    public String LOCK_CREDITORNO = "lock:creditorno";
    public String LOCK_SCATTERSTANDARD_CREDITORNO = "lock:scatterstandard:creditorno";
    public String SCATTERSTANDARD_YEAR_RATE = "scatterstandard:year:rate";
    //打款锁
    public String LOCK_LOAN_CREDITORNO = "lock:loan:creditorno";
    @Value("${third.pay.X-AUTH-TOKEN}")
    public String payToken;
    @Value("${third.pay.income.depositUrl}")
    public String depositUrl;
    @Value("${third.pay.income.depositConfirmUrl}")
    public String depositConfirmUrl;
    @Value("${third.pay.income.currency}")
    public String currency;

}
