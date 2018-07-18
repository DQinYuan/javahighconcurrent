package javahighconcurrent.ch5.disruptor.pipeline;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Multiply implements Runnable {
    public static BlockingQueue<Msg> bq = new LinkedBlockingDeque<>();


    @Override
    public void run() {
        while (true){
            try {
                Msg msg = bq.take();
                msg.i = msg.i * msg.j;
                Div.bq.add(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
