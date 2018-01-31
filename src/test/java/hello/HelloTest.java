package hello;

import static org.junit.Assert.*;
import org.junit.*;
import junit.framework.TestCase;

public class HelloTest{
    @Test
    public void testTest() {
        assertEquals(Hello.main(), "Hello Wall-E");
    }
}
