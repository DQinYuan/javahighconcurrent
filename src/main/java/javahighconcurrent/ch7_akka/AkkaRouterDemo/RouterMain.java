package javahighconcurrent.ch7_akka.AkkaRouterDemo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import com.typesafe.config.ConfigFactory;

/**
 * Created by 燃烧杯 on 2018/5/18.
 */
public class RouterMain {
    public static Agent<Boolean> flag = Agent.create(true, ExecutionContexts.global());

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("route", ConfigFactory.load("samplehello.conf"));
        //w是顶级的父Actor，负责负载均衡
        ActorRef w = system.actorOf(Props.create(WatchActor.class), "watcher");
        int i = 1;
        while ( flag.get() ){
            w.tell(MyWorker.Msg.WORKING, ActorRef.noSender());
            if ( i % 10 == 0 ){
                w.tell(MyWorker.Msg.CLOSE, ActorRef.noSender());
            }
            i++;
            Thread.sleep(100);
        }
        System.out.println("END");
    }
}
