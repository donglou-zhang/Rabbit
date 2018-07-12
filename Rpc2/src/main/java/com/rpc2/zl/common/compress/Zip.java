package com.rpc2.zl.common.compress;

/**
 * Created by Vincent on 2017/11/12.
 */
public interface Zip {

    byte[] zip(byte[] data);

    byte[] unzip(byte[] data);
}
