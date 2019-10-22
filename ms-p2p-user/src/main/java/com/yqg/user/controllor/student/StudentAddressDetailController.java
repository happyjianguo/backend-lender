package com.yqg.user.controllor.student;

import com.yqg.common.core.BaseControllor;
import com.yqg.user.service.student.studentaddressdetail.StudentAddressDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * 学生借款地址信息表
 *
 * @author alan
 * @email alan@yishufu.com
 * @date 2018-10-08 16:22:40
 */
@RestController
public class StudentAddressDetailController extends BaseControllor {
    @Autowired
    StudentAddressDetailService studentaddressdetailService;

}