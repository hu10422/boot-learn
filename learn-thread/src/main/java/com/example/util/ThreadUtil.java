package com.example.util;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 线程问题
 */
public class ThreadUtil {


    public static Integer count = 0;

    /**
     * 多线程Callable使用
     */
    public static void useCallable() {
        for (int i = 0; i < 10; i++) {
            String name = "add" + i;
            CallableTest callableTest = new CallableTest();
            CallableTest.CallableTestAdd add = callableTest.new CallableTestAdd();
            FutureTask<Integer> futureTask = new FutureTask<>(add);
            Thread thread = new Thread(futureTask, name);
            thread.start();
            try {
                System.err.println(thread.getName() + ":" + futureTask.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


            String name2 = "del" + i;
            CallableTest.CallableTestDel del = callableTest.new CallableTestDel();
            FutureTask<Integer> futureTask2 = new FutureTask<>(del);
            Thread thread2 = new Thread(futureTask2, name2);
            thread2.start();
            try {
                System.err.println(thread2.getName() + ":" + futureTask.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTime() {
        return LocalDateTime.now().toString();
    }


    public static void main(String[] args) {
        ThreadUtil.useCallable();

        System.out.println("主线程结束");
    }

}
