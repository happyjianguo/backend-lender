package com.yqg.common.core.request.check;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.check.IRoCheckRule;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.core.annocation.ReqIntGreaterThan0;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.utils.BeanCoypUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 基础检查规则
 * Created by gao on 2018/6/18.
 */
public class RoCheckRule implements IRoCheckRule {

   private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doCheck(Object request) throws Exception {
        List<Field> fields = BeanCoypUtil.getFields(request.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(request);
            if (field.getAnnotation(ReqStringNotEmpty.class) != null) {
                if (fieldValue == null || StringUtils.isEmpty(String.valueOf(fieldValue))) {
                    logger.error("Non-nullable parameter: {}, request data is empty or parameter does not exist. Request data{}",
                            field.getName(), JSON.toJSONString(request, SerializerFeature.WRITE_MAP_NULL_FEATURES));
                    throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
                }
            }
            if (field.getAnnotation(ReqIntGreaterThan0.class) != null) {
                try {
                    if (fieldValue == null || Integer.parseInt(String.valueOf(fieldValue)) <= 0) {
                        logger.error("应大于0整数参数: {} 不合法,请求数据 {}。", field.getName(),
                                JSON.toJSONString(request, SerializerFeature.WRITE_MAP_NULL_FEATURES));
                        throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
                    }
                } catch (NumberFormatException ex) {
                    logger.error("应大于0整数参数: {} 不合法,请求值非整数,请求数据 {}",
                            field.getName(),JSON.toJSONString(request, SerializerFeature.WRITE_MAP_NULL_FEATURES));
                    throw new BusinessException(BaseExceptionEnums.PARAM_ERROR);
                }
            }

        }
    }


}
