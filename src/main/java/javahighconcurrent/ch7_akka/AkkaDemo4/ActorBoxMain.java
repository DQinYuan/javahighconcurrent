package javahighconcurrent.ch7_akka.AkkaDemo4;


import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class ActorBoxMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("inboxdemo", ConfigFactory.load("samplehello.conf"));
        ActorRef worker = system.actorOf(Props.create(MyWorker.class), "worker");
        //创建消息收件箱
        final Inbox inbox = Inbox.create(system);
        //收件箱监视worker,目的是在worker停止时收到一条Terminated消息
        inbox.watch(worker);
        inbox.send(worker, MyWorker.Msg.WORKING);
        inbox.send(worker, MyWorker.Msg.DONE);
        //发送Close消息时，worker会调用stop()
        inbox.send(worker, MyWorker.Msg.CLOSE);
        while (true) {
            Object msg = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            if (msg == MyWorker.Msg.CLOSE) {
                System.out.println("My worker is Closing");
            } else if (msg instanceof Terminated) {
                System.out.println("My worker is dead");
                system.shutdown();
                break;
            } else {
                System.out.println(msg);
            }

        }
    }
}
