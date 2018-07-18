package javahighconcurrent.ch7_akka.AkkaRouterDemo;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 燃烧杯 on 2018/5/18.
 */
public class WatchActor extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public Router router;

    //新建数个worker子Actor,以此初始化router
    {
        List<Routee> routees = new ArrayList<>();
        for ( int i = 0; i < 5; i++ ){
            ActorRef worker = getContext().actorOf(Props.create(MyWorker.class), "worker_" + i);
            getContext().watch(worker);
            routees.add(new ActorRefRoutee(worker));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }


    @Override
    public void onReceive(Object message) throws Exception {
        if ( message instanceof MyWorker.Msg ){
            router.route(message, getSender());
        } else if ( message instanceof Terminated ){
            System.out.println("一个Worker停止了");
            //必须要重新给routee赋值,因为removeRoutee方法似乎不改变原本的router
            router = router.removeRoutee(((Terminated) message).actor());
            System.out.println(((Terminated) message).actor().path() + "is closed, routees="
                    + router.routees().size());
            if ( router.routees().size() == 0 ){
                System.out.println("Close System");
                //修改Agent的值
                RouterMain.flag.send(false);
                getContext().system().shutdown();
            }
        } else {
            unhandled(message);
        }
    }
}
