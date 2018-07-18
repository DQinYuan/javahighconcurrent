package javahighconcurrent.ch3.threadpool.disappear_error;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StackDisappear {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pools = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                0L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

        for ( int i = 0; i < 5; i++ ){
            //pools.submit(new DivTask(100 ,i));   //使用submit不会抛出任何异常，就像程序没有错误一样，很难排查

            /*Future re = pools.submit(new DivTask(100, i));
            re.get();*/                     //使用submit方法时在get时才会报错

            pools.execute(new DivTask(100, i));    //与以上方法一样，只能够报错到Runnable对象内的那一行，无法得到提交信息
        }
    }

}
