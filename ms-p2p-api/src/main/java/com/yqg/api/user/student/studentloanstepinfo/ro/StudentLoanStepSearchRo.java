package com.yqg.api.user.student.studentloanstepinfo.ro;

import com.yqg.common.core.annocation.ReqStringNotEmpty;
import com.yqg.common.core.request.BaseSessionIdRo;
import lombok.Data;

@Data
public class StudentLoanStepSearchRo extends BaseSessionIdRo {
    @ReqStringNotEmpty
    String uuid;
}
