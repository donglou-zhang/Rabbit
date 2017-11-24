package exampleService;

/**
 * Created by Vincent on 2017/11/24.
 */
public class TestHelloImpl implements TestHello {
    @Override
    public String hello(String s) {
        return "Hello, " + s;
    }
}
