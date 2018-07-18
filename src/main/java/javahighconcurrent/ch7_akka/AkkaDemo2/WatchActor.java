package javahighconcurrent.ch7_akka.AkkaDemo2;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

//类似Web中的监听器。在WatchActor方法中指定监听对象。
public class WatchActor extends UntypedActor{
	private final LoggingAdapter log=Logging.getLogger(getContext().system(), this);
	
	
	public  WatchActor(ActorRef ref) {
		//监视指定的Actor
		getContext().watch(ref);
	}
	@Override
	public void onReceive(Object msg) throws Exception {
		//监视的actor停止时,会向监视者发送一条Terminated消息
		if(msg instanceof Terminated){
			System.out.println("workder停止");
/*			System.out.println(String.format("%s has terminated,shutting down system",((Terminated) msg).getActor().path()));
			getContext().system().shutdown();*/
		}
		else{
			unhandled(msg);
		}
	}
	
}
