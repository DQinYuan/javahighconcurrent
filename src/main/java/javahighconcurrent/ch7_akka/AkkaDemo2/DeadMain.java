package javahighconcurrent.ch7_akka.AkkaDemo2;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

//验证Actor的生命周期
public class DeadMain {
	public static void main(String[] args) {
		ActorSystem system=ActorSystem.create("deadwatch",ConfigFactory.load("samplehello.conf"));
		//创建worker Actor
		ActorRef worker=system.actorOf(Props.create(MyWorker.class),"worker");
		//创建监视者, Props.create的第二个参数会传入WatchActor的构造方法
		system.actorOf(Props.create(WatchActor.class,worker),"watcher");
		//5.依次发送3个消息过去，第一个代表参数消息类型
		worker.tell(MyWorker.Msg.WORKING, ActorRef.noSender());
		//停止worker Actor
		worker.tell(PoisonPill.getInstance(),ActorRef.noSender());

		//批量发送消息测试
        List<ActorRef> workers = new ArrayList<>();
        for ( int i = 0; i < 10; i++ ){
            workers.add(system.actorOf(Props.create(MyWorker.class), "worker_" + i));
        }
        //使用选择通配符进行批量通信
        ActorSelection selection = system.actorSelection("/user/worker_*");
        selection.tell(MyWorker.Msg.WORKING, ActorRef.noSender());
        selection.tell(PoisonPill.getInstance(), ActorRef.noSender());
	}
}
