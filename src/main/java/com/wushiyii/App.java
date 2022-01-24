package com.wushiyii;

import com.wushiyii.builder.MVCBuilder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        MVCBuilder.of().port(8080).contextPath("").start(App.class);
    }
}
