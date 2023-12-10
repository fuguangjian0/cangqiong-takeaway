package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author 付广建 2023/12/8 22:01
 */
public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */

    List<ShoppingCart> showShoppingCart();


    /**
     * 清空购物车
     */
    void cleanShoppingCart();


}
