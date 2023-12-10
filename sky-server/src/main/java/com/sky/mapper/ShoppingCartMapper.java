package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 付广建 2023/12/8 22:04
 */
@Mapper
public interface ShoppingCartMapper {


    // 根据shoppingCart看购物车有没有这条数据
    List<ShoppingCart> list(ShoppingCart shoppingCart);


    // 根据shoppingCart插入购物车数据
    void insert(ShoppingCart shoppingCart);

    // 根据id修改份数
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);


    // 清空购物车
    @Delete("delete from shopping_cart where user_id=#{currentId}")
    void deleteByUserId(Long currentId);

    /**
     * 批量插入购物车数据
     *
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);

}
