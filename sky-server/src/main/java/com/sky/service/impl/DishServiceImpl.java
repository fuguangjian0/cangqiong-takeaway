package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.sky.constant.MessageConstant.DISH_BE_RELATED_BY_SETMEAL;
import static com.sky.constant.MessageConstant.DISH_ON_SALE;
import static com.sky.constant.StatusConstant.DISABLE;
import static com.sky.constant.StatusConstant.ENABLE;

/**
 * @author 付广建 2023/12/7 16:21
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Resource
    private DishMapper dishMapper;

    @Resource
    private DishFlavorMapper dishFlavorMapper;

    @Resource
    private SetmealDishMapper setmealDishMapper;

    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        // 1.新增菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);

        // dishId 为null, 只能到数据库查了
        Long dishId = dishMapper.getDishId(dish.getImage());

        // 2.新增口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()>0) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
        }
        dishFlavorMapper.insertBatch(flavors);

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        // 如果正在启售, 不能删除
        ids.forEach(id->{
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus().equals(ENABLE)){
                throw new DeletionNotAllowedException(DISH_ON_SALE);
            }
        });

        // 菜品被套餐关联,不能删除
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds!=null && setmealIds.size()>0) {
            throw new DeletionNotAllowedException(DISH_BE_RELATED_BY_SETMEAL);
        }

        ids.forEach(id->{
            // 可以删除, 删除菜品
            dishMapper.deleteById(id);
            // 删除菜品口味关系
            dishFlavorMapper.deleteByDishId(id);
        });

    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 修改菜品 不能先删后增,因为id规则主键自增的
        dishMapper.update(dish);

        // 修改菜品口味关系 先删后增
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.forEach(flavor->flavor.setDishId(dishDTO.getId()));
        dishFlavorMapper.insertBatch(flavors);

    }

    /**
     * 根据菜品id回显菜品和菜品口味关系
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        // 查菜品
        Dish dish = dishMapper.getById(id);
        // 查口味关系
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 封装返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 菜品起售停售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder().status(status).id(id).build();
        dishMapper.update(dish);

        // 如果是停售, 要把套餐也停售
        if (status.equals(DISABLE)){
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            setmealIds.forEach(setmealId->{
                Setmeal setmeal = Setmeal.builder().status(status).id(setmealId).build();
                setmealMapper.update(setmeal);
            });

        }
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getDishesByCategoryId(Long categoryId) {
        return dishMapper.getDishesByCategoryId(categoryId);
    }
}



