package common.util;

import com.rabbit.zl.common.test.CaseCounter;
import com.rabbit.zl.common.util.ByteUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link ByteUtil}
 *
 * @author Vincent
 * Created  on 2017/11/16.
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
