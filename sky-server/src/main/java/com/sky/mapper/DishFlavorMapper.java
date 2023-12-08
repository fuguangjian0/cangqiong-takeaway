package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 付广建 2023/12/7 16:24
 */
@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品id删除数据
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteByDishId(Long id);

    /**
     * 根据菜品id查询口味
     * @param dishId
     */
    @Select("select * from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor>  getByDishId(Long dishId);

}
