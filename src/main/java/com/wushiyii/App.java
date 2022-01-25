package com.wushiyii;

import com.wushiyii.builder.MyMVCBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        MyMVCBuilder.of().port(8080).contextPath("").start(App.class);
    }
}
