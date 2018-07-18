package javahighconcurrent.ch5.disruptor.future_model.jdkfuture;

import java.util.concurrent.*;

/**
 * 使用Callable实例构造一个FutureTask实例，然后交给线程池
 */
public class FutureMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //构造FutureTask
        FutureTask<String> future = new FutureTask<>(new RealData("a"));
        ExecutorService executor = Executors.newFixedThreadPool(1);

        executor.submit(future);

        System.out.println("请求完毕");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //可以直接从futuretask中得到结果
        System.out.println("数据 = " + future.get());
    }
}
