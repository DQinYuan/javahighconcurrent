package javahighconcurrent.ch7_akka.AkkaDemo7;

import akka.actor.*;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.dispatch.OnComplete;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by liubenlong on 2017/1/12.
 */
public class AgentDemo extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object o) {
        if(o instanceof Integer){
            for(int i = 0 ; i < 10000 ; i ++){
                Future<Integer> future = countAgent.alter(new Mapper<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer parameter) {
                        return parameter + 1;
                    }
                });
                queue.add(future);
            }

            getContext().stop(getSelf());//完成任务，关闭自己
        }else{
            unhandled(o);
        }
    }


    public static Agent<Integer> countAgent = Agent.create(0, ExecutionContexts.global());
    public static ConcurrentLinkedQueue<Future<Integer>> queue = new ConcurrentLinkedQueue<>();

    public static void main(String [] args) throws InterruptedException, TimeoutException {
        final ActorSystem system = ActorSystem.create("inbox",
                ConfigFactory.load("samplehello.conf"));

        ActorRef[] actorRefs = new ActorRef[10];
        for(int i = 0 ; i < 10 ; i ++){
           actorRefs[i] = system.actorOf(Props.create(AgentDemo.class), "AgentTest" + i);
        }

        Inbox inbox = Inbox.create(system);
        for (ActorRef ref : actorRefs) {
            inbox.send(ref, 1);
            inbox.watch(ref);
        }

        System.out.println("countAgent 1:" + countAgent.get());


        /*
         * 这里本来想用JDK里的AtomicInteger, 但是不行，不适用于这里的actor
         */


        //等待所有actor执行完毕
        int closeCount = 0;
        while(true){
            Object o = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            if(o instanceof Terminated){
                closeCount ++;
                if(closeCount == actorRefs.length){
                    break;
                }
            }else{
                System.out.println("o:" + o);
            }
        }

        System.out.println("countAgent 2:" + countAgent.get());

        //等待所有累加线程完成
        Futures.sequence(queue, system.dispatcher()).onComplete(new OnComplete<Iterable<Integer>>() {
            @Override
            public void onComplete(Throwable throwable, Iterable<Integer> integers) throws Throwable {
                System.out.println("countAgent 3:" + countAgent.get());
                system.shutdown();
            }
        }, system.dispatcher());

    }
}