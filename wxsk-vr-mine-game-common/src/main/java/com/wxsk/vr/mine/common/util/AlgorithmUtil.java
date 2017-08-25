package com.wxsk.vr.mine.common.util;

import java.util.Random;

/**
 * 算法工具类
 */
public class AlgorithmUtil {

    private static final Random random = new Random();

    /**
     * 随机int
     * @param min 最小值 include
     * @param max　最大值 exclude
     * */
    public static int randomInt(int min, int max) {
        int value = random.nextInt(max);
        //不在范围内
        if (value < min) {
            value = min + (min - value)%(max - min);
        }
        else if (value >= max) {
            value = max - (value - max)%(max - min) - 1;
        }
        return value;
    }

    public static void main(String[] args) {
        int min = 1000;
        int max = 1002;
        for (int i=0; i<1000000; i++) {
            int v = randomInt(min, max);
            if (v < min || v >= max) {
                throw new RuntimeException("算法错误");
            }
        }
        System.out.println("OK");
    }
}
