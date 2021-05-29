package com.example.util;

import java.util.concurrent.Callable;

public class CallableTest implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        ThreadUtil.count++;
        System.out.println("count++:" + ThreadUtil.count);
        return ThreadUtil.count;
    }

    class CallableTestAdd implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadUtil.count++;
            System.out.println("count++:" + ThreadUtil.count);
            return ThreadUtil.count;
        }
    }

    class CallableTestDel implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ThreadUtil.count--;
            System.out.println("count--:" + ThreadUtil.count);
            return ThreadUtil.count;
        }
    }

}
