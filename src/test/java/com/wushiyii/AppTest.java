package com.wushiyii;

import static org.junit.Assert.assertTrue;

import com.wushiyii.builder.MyMVCBuilder;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Test
    public void start() {
        MyMVCBuilder.builder().port(7400).contextPath("/mvc").start(AppTest.class);
    }
}
