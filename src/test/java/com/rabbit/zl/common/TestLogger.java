package com.rabbit.zl.common;

import com.rabbit.zl.common.test.CaseCounter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Vincent on 2017/12/8.
 */
public class TestLogger {
    private static Logger LOGGER = LoggerFactory.getLogger(TestLogger.class);

    @Test
    public void testLogger() {
        LOGGER.debug("This is debug message");

        LOGGER.info("This is info message");

        LOGGER.error("This is error message");

        System.out.println(String.format("[Rabbit] (^_^) <%s> Test Case Success -> Logger. ", CaseCounter.incr(3)));
    }
}
