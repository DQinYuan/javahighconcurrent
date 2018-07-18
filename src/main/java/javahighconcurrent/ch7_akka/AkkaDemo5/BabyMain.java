package javahighconcurrent.ch7_akka.AkkaDemo5;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import javahighconcurrent.ch7_akka.AkkaDemo2.WatchActor;

/*
 * 1.学习内置状态转换
 * 现在进入BabyActor看下一步注释
 */
public class BabyMain {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("become",ConfigFactory.load("samplehello.conf"));
		ActorRef child=system.actorOf(Props.create(BabyActor.class),"baby");
		system.actorOf(Props.create(WatchActor.class,child),"watcher");
		child.tell(BabyActor.Msg.PLAY, ActorRef.noSender());
		child.tell(BabyActor.Msg.SLEEP, ActorRef.noSender());
		child.tell(BabyActor.Msg.PLAY, ActorRef.noSender());
		child.tell(BabyActor.Msg.PLAY, ActorRef.noSender());
		child.tell(PoisonPill.getInstance(), ActorRef.noSender());
	}
}
