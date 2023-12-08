package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 付广建 2023/12/8 15:44
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关操作")
@Slf4j
public class ShopController {
    @Resource
    private StringRedisTemplate srt;
    public static final String KEY = "SHOP_STATUS";

    /**
     * 获取营业状态
     * @return
     */
    @ApiOperation("获取营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        String status = srt.opsForValue().get(KEY);
        if (status != null) {
            log.info("店铺状态: {}", status.equals("1") ? "营业中" : "打烊中");
            return Result.success(Integer.valueOf(status));
        }
        return Result.success();
    }

    /**
     * 设置营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result<String> setStatus(@PathVariable Integer status){
        srt.opsForValue().set(KEY, status.toString());
        return Result.success();
    }



}
