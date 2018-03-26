package com.arms.common.util;

import org.junit.Test;

public class TestGetRandomIntByLength {

    @Test
    public void testGetRandomIntByLength() {
        for (int i = 1; i <= 100; i++) {
            int number = NumberUtil.getRandomIntByLength(2);
            System.out.println(number);
        }
        // 测试1
    }
}
