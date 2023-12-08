package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 付广建 2023/12/7 20:08
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id获得套餐id
     * @param idList
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> idList);

    /**
     * 新增套餐菜品关系
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id找到关系数据
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getBySetmealId(Long id);


    /**
     * 根据套餐id删除
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void removeBySetmealId(Long id);


}
