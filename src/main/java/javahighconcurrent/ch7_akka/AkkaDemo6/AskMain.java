package javahighconcurrent.ch7_akka.AkkaDemo6;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.util.Timeout;
import com.typesafe.config.ConfigFactory;
import javahighconcurrent.ch7_akka.AkkaDemo2.WatchActor;
import javahighconcurrent.ch7_akka.AkkaDemo4.MyWorker;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;


public class AskMain {
	public static void main(String[] args) throws Exception {
		ActorSystem system=ActorSystem.create("askdemo",ConfigFactory.load("samplehello.conf"));
		ActorRef worker=system.actorOf(Props.create(MyWorker.class),"worker");
		ActorRef printer=system.actorOf(Props.create(Printer.class),"printer");
		system.actorOf(Props.create(WatchActor.class,worker),"watcher");

		Future<Object> f = ask(worker, 5, 1500);
        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
		int re=(int) Await.result(f, timeout.duration());
		System.out.println("return:"+re);

        f=ask(worker, 6, 1500);
		pipe(f, system.dispatcher()).to(printer);
		worker.tell(PoisonPill.getInstance(), ActorRef.noSender());
	}
}
