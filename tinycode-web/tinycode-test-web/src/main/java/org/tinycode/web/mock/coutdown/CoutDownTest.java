package org.tinycode.web.mock.coutdown;

import org.springframework.util.StopWatch;

import java.util.concurrent.*;

/**
 * TODO
 *
 * @author littlehui
 * @version 1.0
 * @date 2022/10/27 20:22
 */
public class CoutDownTest {

    public static void main(String[] args) throws InterruptedException {
        int size = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(size);
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
