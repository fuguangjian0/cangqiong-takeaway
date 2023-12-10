package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 付广建 2023/12/8 17:40
 */
@Mapper
public interface UserMapper {

    /**
     * 根据openId获得用户
     * @param openId
     * @return
     */
    @Select("select * from user where openid=#{openId};")
    User getByOpenId(String openId);

    /**
     * 新增登录数据
     * @param user
     */
    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time) values (#{openid}, #{name}, #{phone}, #{sex}, #{idNumber}, #{avatar}, #{createTime})")
    void insert(User user);

    /**
     * 根据id获取用户信息
     * @param userId
     * @return
     */
    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
