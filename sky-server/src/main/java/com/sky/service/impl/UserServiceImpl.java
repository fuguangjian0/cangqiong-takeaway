
package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.context.BaseContext;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.sky.constant.MessageConstant.LOGIN_FAILED;

/**
 * @author 付广建 2023/12/8 17:20
 */
@Service
public class UserServiceImpl implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openId = this.getOpenId(userLoginDTO.getCode());
        // 判空openId, 如果为空则登录失败, 抛出业务异常
        if (openId == null) {
            throw new LoginFailedException(LOGIN_FAILED);
        }

        // 判断用户是否为新用户
        User user = userMapper.getByOpenId(openId);

        // 如果是新用户, 注册
        if (user==null) {
            user = User.builder().openid(openId).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }

        return user;
    }

    /**
     * 根据code获取openid
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }


}
