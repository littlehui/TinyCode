package org.tinycode.web.mock.coutdown;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/10/27 20:49
 */
public class VisualThread {

    public static void main(String[] args) throws InterruptedException {
        int size = 10000;
        //ThreadFactory factory = Thread.ofVirtual().factory();
        //ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //StopWatch stopWatch = new StopWatch();
        //stopWatch.start();
        Long startMills = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
        System.out.println(System.currentTimeMillis() - startMills);
    }
}
