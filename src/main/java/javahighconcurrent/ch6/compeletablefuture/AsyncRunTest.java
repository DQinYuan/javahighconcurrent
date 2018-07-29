package javahighconcurrent.ch6.compeletablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture.supplyAsync函数中的lambda会在一个新的线程中执行
 * 类似的工厂方法如下
 *     supplyAsync(supplier)
 *     supplyAsync(supplier, executor)
 *     runAsync(runnable)
 *     runAsync(runnable, executor)
 *
 * 这些方法如果传入了executor参数，则在指定的线程池中执行，
 * 否则则在默认的系统公共的ForkJoinPool.common线程池中执行
 *
 * 在Java8中，新增了ForkJoinPool.commonPool()方法。它可以获得一个公共
 * 的ForkJoin线程池。这个池中的所有线程都是Daemon线程。这意味着如果主线程退出
 * ，这些线程无论是否执行完毕，都会退出系统
 */
public class AsyncRunTest {
    public static int calc(Integer pare){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pare * pare;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final CompletableFuture<Integer> future
                = CompletableFuture.supplyAsync(() -> calc(50));
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() ->
                System.out.println(Thread.currentThread().getName()));
        System.out.println(future.get());
    }
}
