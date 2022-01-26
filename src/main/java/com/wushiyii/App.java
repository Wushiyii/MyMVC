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
        MyMVCBuilder.of().start(App.class);
    }
}
