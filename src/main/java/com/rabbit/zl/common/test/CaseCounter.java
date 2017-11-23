package com.rabbit.zl.common.test;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Vincent
 * Created  on 2017/11/16.
 */
public class CaseCounter {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static String incr(int delta) {
        String format = "000000";
        DecimalFormat df = new DecimalFormat(format);
        long c = counter.addAndGet(delta);
        return df.format(c);
    }
}
