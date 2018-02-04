package com.walle.hello;

import static org.junit.Assert.*;
import org.junit.*;

public class HelloTest{
    @Test
    public void testTest() {
        assertEquals(Hello.main(), "Hello Wall-E");
    }
}
