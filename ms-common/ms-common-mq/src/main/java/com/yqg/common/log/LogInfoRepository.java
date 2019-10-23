package com.yqg.common.log;

import com.yqg.common.log.formatter.LogMessgeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * Created by gao on 2017/9/10.
 */
@Component
public interface LogInfoRepository extends MongoRepository<LogMessgeInfo, String> {

}
