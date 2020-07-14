package com.yqg.api.order.scatterstandard.bo;

import com.yqg.common.core.request.BaseSessionIdRo;
import lombok.Data;

import java.util.List;

/**
 * 散标表 请求对象
 *
 * @author huanhuan
 * @email liuhuanhuan@yishufu.com
 * @date 2018-09-03 11:02:36
 */
@Data
public class SignBo{
    List<String> orderNo;
}

