package com.ryaltech.util.archive;

import java.io.InputStream;

public class DummyClass {
    static{
        String x = new String("1234");
        System.out.println(x.toLowerCase());
    }
    public void xyz()throws Exception{
        String x = new String("1234");
        System.out.println(x.toUpperCase());
        InputStream i=null;
        i.close();
        new Runnable(){
            @Override
            public void run() {
                String x = new String("1234");
                System.out.println(x.toCharArray());
                
            }
        };
    }

}
