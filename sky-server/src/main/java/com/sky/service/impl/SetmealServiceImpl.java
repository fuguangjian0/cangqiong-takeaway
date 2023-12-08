package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.sky.constant.MessageConstant.SETMEAL_ON_SALE;
import static com.sky.constant.StatusConstant.ENABLE;

/**
 * @author 付广建 2023/12/7 23:50
 */
@Service
public class SetmealServiceImpl implements SetmealService {
    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void save(SetmealDTO setmealDTO) {
        // 新增套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        // 查到套餐ID
        Long setmealId = setmealMapper.getSetmealIdByImage(setmeal.getImage());
        // 新增套餐菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO pageQueryDTO) {
        PageHelper.startPage(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(pageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void removeBatch(List<Long> ids) {
        // 套餐起售则不能删
        ids.forEach(id->{
            if (setmealMapper.getById(id).getStatus().equals(ENABLE)) throw new DeletionNotAllowedException(SETMEAL_ON_SALE);
        });

        // 删除套餐
        setmealMapper.removeBatch(ids);

        // 删除套餐菜品关系
        ids.forEach(id->{
            setmealDishMapper.removeBySetmealId(id);
        });

    }



    /**
     * 修改套餐和套餐菜品关系
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        // 修改套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // 修改套餐菜品关系 -> 先删后增
        // 删除关系
        setmealDishMapper.removeBySetmealId(setmeal.getId());
        // 新增关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealDishMapper.insertBatch(setmealDTO.getSetmealDishes());

    }

    /**
     * 起售停售套餐
     * @param status
     * @param setmealId
     */
    @Override
    public void startOrStop(Integer status, Long setmealId) {
        setmealMapper.update(Setmeal.builder().status(status).id(setmealId).build());
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        // 查询套餐
        Setmeal setmeal = setmealMapper.getById(id);
        // 查询菜品套餐关系
        List<SetmealDish> list = setmealDishMapper.getBySetmealId(id);
        // 封装返回
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(list);
        return setmealVO;
    }

}
