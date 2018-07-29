package javahighconcurrent.ch6.compeletablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class StreamAPI {

    public static int calc(int para){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return para * para;
    }

    //故意抛出除0异常
    public static int divZero(int para) {
        return para / 0;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> fu = CompletableFuture.supplyAsync(() -> calc(50))
                .thenApply(i -> Integer.toString(i))
                .thenApply((str) -> "\" "+ str + "\"")
                /**
                 * thenAccept会返回CompletableFuture<Void>
                 *  表明流要结束了
                 */
                .thenAccept(System.out::println);

        fu.get();//等待完成

        CompletableFuture<Void> f = CompletableFuture
                .supplyAsync(() -> divZero(50))
                //处理异常
                .exceptionally(ex -> {
                    System.out.println(ex.toString());
                    return 0;  //如果出现异常则返回0
                })
                .thenApply(i -> Integer.toString(i))
                .thenApply((str) -> "\" "+ str + "\"")
                .thenAccept(System.out::println);

        f.get();



    }

}
