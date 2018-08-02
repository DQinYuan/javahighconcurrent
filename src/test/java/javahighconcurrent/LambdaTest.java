package javahighconcurrent;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LambdaTest {

    @Override
    public String toString() {
        return "apple";
    }

    /**
     * lambda表达式中this代表外部类对象
     */
    Runnable r1 = () -> System.out.println(this);

    /**
     * 匿名内部类中this代表内部类对象
     */
    Runnable r2 = new Runnable() {
        @Override
        public void run() {
            System.out.println(this);
        }
    };

    @Test
    public void test() throws InterruptedException {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(r1);
        executor.execute(r2);
        Thread.sleep(1000);
    }
}
