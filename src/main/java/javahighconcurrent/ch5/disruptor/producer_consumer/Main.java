package javahighconcurrent.ch5.disruptor.producer_consumer;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        PCDataFactory factory = new PCDataFactory();

        int bufferSize = 2;
        Disruptor<PCData> disruptor = new Disruptor<PCData>(factory,
                bufferSize,   //ringbuffer的大小，必须为2的整数次幂
                executor,
                ProducerType.MULTI,
                new BlockingWaitStrategy());   //BlockingWaitStrategy是消费者监控缓冲区的策略有以下的一些策略
        /**
         * BlockingWaitStrategy:默认策略，类似于BlockingQueue，使用锁和条件进行数据的监控，最节省CPU
         *
         * SleepingWaitingStrategy:也是一个CPU使用保守的策略，在监控不到数据的情况下，他会先使用yield方法让出CPU,
         * 并最终使用LockSupport.partNanos(1)进行休眠，确保不占用过多的CPU，比较适合对延迟不是特别高的场合，好处是
         * 对生产者线程影响最小。典型的应用场景是异步打印日志
         *
         * -------------个人认为上面的策略都是适合线程数大于CPU核数的场合，下面则是比较适合线程数小于或者等于CPU核数的场合
         *
         * YieldingWaitStrategy:适合低延迟场合，消费者变成一个内部执行yield方法的死循环，最好有多于消费者线程数量的逻辑
         * CPU数量（逻辑CPU即双核四线程中的“四”）
         *
         * BusySpinWaitStrategy:最疯狂的策略，它就是一个死循环。适合对延迟非常苛刻或者系统非常繁忙的场合。此时你的物理
         * CPU的数量必须大于消费者线程数，注意，这里是物理CPU，即使在物理核上使用超线程技术模拟出两个逻辑核，另外一个
         * 逻辑核也会收到这种超密集计算的影响
         */
        disruptor.handleEventsWithWorkerPool(
                new Consumer(),
                new Consumer(),
                new Consumer(),
                new Consumer()
        );    //设置了4个消费者实例，系统会将每个消费者实例映射到一个线程中，也就是这里提供了四个消费者线程
        disruptor.start();    //启动消费者时间处理并且生成RingBuffer

        RingBuffer<PCData> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);

        for ( long l = 0; true; l++ ){   //有主线程担任生产者
            if ( l == 10 ){
                break;
            }

            bb.putLong(0, l);
            producer.pushData(bb);
            //Thread.sleep(1000);
            System.out.println("add Data " + l);
        }

    }
}
