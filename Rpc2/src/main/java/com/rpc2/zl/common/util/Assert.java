package com.rpc2.zl.common.util;

/**
 * provides statement assertion
 *
 * @author Vincent
 * Created  on 2017/11/112.
 */
public abstract class Assert {

    /**
     * Assert that the object is not <code>null<code/>
     *
     * @param obj
     * @param message
     */
    public static void notNull(Object obj, String message) {
        if(obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object obj) {
        notNull(obj, "[Assertion failed] - this argument is required; it must not be null");
    }
}
