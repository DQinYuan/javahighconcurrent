package javahighconcurrent.ch7_akka.PSO;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MasterBird extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private PsoValue gBest = null;

    @Override
    public void onReceive(Object message) throws Exception {
        if ( message instanceof PBestMsg ){
            PsoValue pBest = ((PBestMsg) message).getValue();
            if ( gBest == null || gBest.value < pBest.value ){
                //更新全局最优，通知所有粒子
                System.out.println(message + "\n");
                gBest = pBest;
                //批量通知
                ActorSelection selection = getContext().system().actorSelection("/user/bird_*");
                selection.tell(new GBestMsg(gBest), getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}
