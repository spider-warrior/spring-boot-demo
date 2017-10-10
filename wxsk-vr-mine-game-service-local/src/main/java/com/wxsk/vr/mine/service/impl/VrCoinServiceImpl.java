package com.wxsk.vr.mine.service.impl;

import com.wxsk.vr.mine.common.util.AlgorithmUtil;
import com.wxsk.vr.mine.properties.ApplicationProperties;
import com.wxsk.vr.mine.properties.GameProperties;
import com.wxsk.vr.mine.service.VrCoinService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 无限币服务实现
 */
@Service
public class VrCoinServiceImpl implements VrCoinService {

    private static final Logger logger = LogManager.getLogger(VrCoinServiceImpl.class);
    private static final long ONE_DAY_IN_MILLS = TimeUnit.DAYS.toMillis(1);

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public VrCoinServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();
    }

    @Autowired
    private GameProperties gameProperties;
    @Autowired
    private ApplicationProperties applicationProperties;

    @Override
    public int getMaxVrCoin() {
        return gameProperties.getVrCoinConfig().getMax();
    }

    @Override
    public int calculateAwardVrCoin(int level, long now) {
        List<List<Integer>> subtypeRandomConfig = gameProperties.getVrCoinConfig().getSubtypeRandomConfig();
        //随机可选项
        List<Integer> options = subtypeRandomConfig.get(level);
        if (options == null || options.size() == 0) {
            logger.warn("receive a unknown [VR COIN] level: {}", level);
            return 0;
        }
        //for test
        if (!redisTemplate.hasKey(applicationProperties.getVrCoinRedisKey())) {
            valueOperations.set(applicationProperties.getVrCoinRedisKey(), String.valueOf(gameProperties.getVrCoinConfig().getMax()));
        }
        //掉率比
        double rate = calculateVRCoinRate(now);
        int index = AlgorithmUtil.randomInt(0, options.size());
        int awardAmount = rate <= 0 ? 0 : (int) (options.get(index) * rate);
        long remain = valueOperations.increment(applicationProperties.getVrCoinRedisKey(), -awardAmount);
        if (remain < 0) {
            awardAmount = 0;
        }
        logger.info("无限币随机量: {}, 掉率比: {}, 基数: {}", awardAmount, rate, options.get(index));
        return awardAmount;
    }

    @Override
    public long calculateOnlineDayCount(long now) {
        long range = now - gameProperties.getVrCoinConfig().getStartTime();
        return (range + ONE_DAY_IN_MILLS - 1) / ONE_DAY_IN_MILLS;
    }

    @Override
    public double calculateVRCoinRate(long now) {
        //开服天数
        long dayCount = calculateOnlineDayCount(now);
        //实际总产出
        long actualProduction = gameProperties.getVrCoinConfig().getMax() - valueOperations.increment(applicationProperties.getVrCoinRedisKey(), 0);
        //预期产出量 = 平均日产出 * 开服天数
        long predictSum = gameProperties.getVrCoinConfig().getAverageByDay() * dayCount;
        //掉率比 = 今日应产出 / 平均产出
        return ((double) (predictSum - actualProduction)) / gameProperties.getVrCoinConfig().getAverageByDay();
    }
}
