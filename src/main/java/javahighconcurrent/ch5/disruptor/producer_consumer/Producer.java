package javahighconcurrent.ch5.disruptor.producer_consumer;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class Producer {
    private final RingBuffer<PCData> ringBuffer;

    public Producer(RingBuffer<PCData> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void pushData(ByteBuffer bb){
        long sequence = ringBuffer.next();   //获得下一个序号
        PCData event = ringBuffer.get(sequence);    //获得相应序号处的项
        event.set(bb.getLong(0));
        ringBuffer.publish(sequence);    //只有发布后的数据才能被消费者看到
    }
}
