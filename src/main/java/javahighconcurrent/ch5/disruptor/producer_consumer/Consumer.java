package javahighconcurrent.ch5.disruptor.producer_consumer;

import com.lmax.disruptor.WorkHandler;

/**
 * 消费者，由Disruptor封装成了响应式的形式
 */
public class Consumer implements WorkHandler<PCData> {
    @Override
    public void onEvent(PCData event) throws Exception {
        System.out.println(Thread.currentThread().getId() + ":Event: --"
                           + event.get() * event.get() + "--");     //求平方打印
    }
}
