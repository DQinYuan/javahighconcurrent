package javahighconcurrent.ch5.disruptor.falseshare.mockdisruptot;

class LhsPadding{   //左Padding
    protected long p1, p2, p3, p4, p5, p6, p7;
}

class Value extends LhsPadding{
    protected volatile long value;
}

class RhsPadding extends Value{   //右Padding

}

public class Sequence extends RhsPadding {
    //省略具体实现
}

//RingBuffer的内部数组是通过以下方式构建的
//这样就可以保证整个数组不会被伪共享（即不会受到其他变量的影响而失效）
//    this.entries = new Object[sequencer.getBufferSize() + 2 * BUFFER_PAD]

