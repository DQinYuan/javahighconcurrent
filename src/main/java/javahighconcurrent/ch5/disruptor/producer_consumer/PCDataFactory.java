package javahighconcurrent.ch5.disruptor.producer_consumer;

import com.lmax.disruptor.EventFactory;

/**
 * 工厂类，会在Disruptor系统初始化时，构造所有的缓冲区中的对象实例
 */
public class PCDataFactory implements EventFactory<PCData> {
    @Override
    public PCData newInstance() {
        return new PCData();
    }
}
