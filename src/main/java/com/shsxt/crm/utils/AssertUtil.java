package com.shsxt.crm.utils;


import com.shsxt.crm.exceptions.ParamsException;

public class AssertUtil {

    public static void isTrue(Boolean flag,String msg){
        if(flag){
            throw new ParamsException(msg);
        }
    }
}
