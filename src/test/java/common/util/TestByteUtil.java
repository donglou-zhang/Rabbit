package common.util;

import common.test.CaseCounter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link ByteUtil}
 */
public class TestByteUtil {

    @Test
    public void testIntToBytes() {
        int i = 517;
        Assert.assertEquals(i, ByteUtil.bytesToInt(ByteUtil.intToBytes(i)));
        i = -2134;
        Assert.assertEquals(i, ByteUtil.bytesToInt(ByteUtil.intToBytes(i)));
        System.out.println(String.format("[Rabbit] (^_^) <%s> Test Case Success -> int to bytes. ", CaseCounter.incr(2)));
    }
}
