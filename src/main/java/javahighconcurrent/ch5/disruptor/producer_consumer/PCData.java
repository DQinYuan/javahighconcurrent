package javahighconcurrent.ch5.disruptor.producer_consumer;

public class PCData {

    private long value;

    public void set(long value){
        this.value = value;
    }

    public long get(){
        return value;
    }
}
