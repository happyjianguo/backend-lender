package com.yqg.common.test;

import com.alibaba.fastjson.JSON;
import com.yqg.common.core.request.BaseRo;
import com.yqg.common.core.request.BaseSessionIdRo;
import com.yqg.common.redis.BaseRedisKeyEnums;
import com.yqg.common.redis.RedisUtil;
import com.yqg.common.utils.UuidUtil;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;

/**
 * Controllor单元测试基础类
 * Created by gao on 2018/7/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BaseControllorTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * web项目上下文
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * 会话id
     */
    protected String sessionId;
    /**
     * 基本会话请求
     */
    protected BaseSessionIdRo baseSessionIdRo;


    /**
     * 所有测试方法执行之前执行该方法
     */
    @Before
    public void before() {
        //获取mockmvc对象实例
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RedisUtil redisUtil = webApplicationContext.getBean(RedisUtil.class);

        sessionId = UuidUtil.create();
        String userId = "ec78da03547f41a88064bc0b861e995c";
        redisUtil.set(BaseRedisKeyEnums.USER_SESSION_KEY.appendToDefaultKey(sessionId), userId);
        baseSessionIdRo = new BaseSessionIdRo();
        baseSessionIdRo.setSessionId(sessionId);
    }

    /**
     * 简单 post 请求
     *
     * @param apiPaht 接口路径
     * @param ro      请求对象
     * @return
     * @throws Exception
     */
    protected String postTest(String apiPaht,BaseRo ro) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.post(apiPaht).
                        contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONString(ro));

        MvcResult mvcResult = mockMvc.perform(mockHttpServletRequestBuilder).
                andExpect(MockMvcResultMatchers.status().isOk()).//成功
                andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8)).//返回类型
                andReturn();
        String resultString = mvcResult.getResponse().getContentAsString();
        System.out.println("--------------返回值:" + resultString);
        return resultString;
    }

}