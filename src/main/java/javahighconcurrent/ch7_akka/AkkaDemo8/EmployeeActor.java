package javahighconcurrent.ch7_akka.AkkaDemo8;

import akka.actor.UntypedActor;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

/**
 * Created by 燃烧杯 on 2018/5/18.
 */
public class EmployeeActor extends UntypedActor {
    //Ref是一个指向STM(软件事务内存)的引用
    private Ref.View<Integer> count = STM.newRef(50);

    @Override
    public void onReceive(Object message) throws Exception {
        if ( message instanceof Coordinated){
            final Coordinated c = (Coordinated) message;
            final int downCount = (Integer) c.getMessage();
            //定义事务处理块
            c.atomic(new Runnable() {
                @Override
                public void run() {
                    STM.increment(count, downCount);
                }
            });
        } else if ( "GetCount".equals(message) ){
            getSender().tell(count.get(), getSelf());
        } else {
            unhandled(message);
        }
    }
}
