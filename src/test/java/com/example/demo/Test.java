package com.example.demo;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by heiqie on 2017/11/9.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        String s = "123456";
        String ss = s.substring(0,5);
        System.out.println(ss);
        String test1 = "    ";
        String test2 = "123 ";
        System.out.println(isEmpty(test1));
        System.out.println(isEmpty(test2));
        System.out.println(isEmpty(null));
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
    }


    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        char chars[] = str.toCharArray();
        for (char c : chars) {
            if (c != ' ') {
                return false;
            }
        }
        return true;
    }


}
