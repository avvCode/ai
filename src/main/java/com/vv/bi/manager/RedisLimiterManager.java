package com.vv.bi.manager;

import com.vv.bi.common.ErrorCode;
import com.vv.bi.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author vv
 * @Description Redisson限流器
 * @date 2023/7/11-20:04
 */
@Service
public class RedisLimiterManager {
    @Resource
    private RedissonClient redissonClient;

    public void doRateLimiter(String key){
        //创建一个名称为 key 的限流器
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        //每秒2次
        rateLimiter.trySetRate(RateType.OVERALL,2,1, RateIntervalUnit.SECONDS);
        //操作需要占用的令牌
        boolean canOp = rateLimiter.tryAcquire(1);
        if(!canOp){
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }
}
