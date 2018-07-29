package javahighconcurrent.ch6.longadder;

import java.util.Random;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * LongAccumulator是LongAdder的一个扩展
 * 允许自定义二元Long操作，构造函数的第一个参数就必须传入一个自定义的二元Long操作，返回一个Long
 */
public class LongAccumulatorTest {
    public static void main(String[] args) throws InterruptedException {
        LongAccumulator accumulator = new LongAccumulator(Long::max, Long.MIN_VALUE);
        Thread[] ts = new Thread[1000];

        for ( int i = 0; i < 1000; i++ ){
            ts[i] = new Thread(() -> {
                Random random = new Random();
                long value = random.nextLong();
                //会将value与内部的值进行Long::max操作
                accumulator.accumulate(value);
            });
            ts[i].start();
        }

        for ( int i = 0; i < 1000; i++ ){
            ts[i].join();
        }

        //longValue会将所有cell的值进行Long::max操作，得出最后结果
        System.out.println(accumulator.longValue());
    }
}
