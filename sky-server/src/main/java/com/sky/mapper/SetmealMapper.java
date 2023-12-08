package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import static com.sky.enumeration.OperationType.INSERT;
import static com.sky.enumeration.OperationType.UPDATE;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 修改套餐
     * @param setmeal
     */
    @AutoFill(UPDATE)
    void update(Setmeal setmeal);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(INSERT)
    @Insert("insert into setmeal(category_id, description, image, name, price, status, create_user, create_time, update_time, update_user) values (#{categoryId}, #{description}, #{image}, #{name}, #{price}, #{status}, #{createUser}, #{createTime}, #{updateTime}, #{updateUser})")
    void insert(Setmeal setmeal);

    /**
     * 根据图片获取套餐id
     * @param image
     * @return
     */
    @Select("select id from setmeal where image=#{image}")
    Long getSetmealIdByImage(String image);

    /**
     * 套餐分页查询
     * @param pageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO pageQueryDTO);

    /**
     * 根据id获得套餐数据
     * @param id
     * @return
     */
    @Select("select * from setmeal where id=#{id}")
    Setmeal getById(Long id);

    /**
     * 根据id批量删除套餐
     * @param ids
     */
    void removeBatch(List<Long> ids);

}
