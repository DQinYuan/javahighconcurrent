package javahighconcurrent.ch7_akka.AkkaDemo2;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

//定义了一个MyWorker的Actor 重写prestart和postStop方法。
public class MyWorker extends UntypedActor{
	private final LoggingAdapter log=Logging.getLogger(getContext().system(), this);
	public static enum Msg{
		WORKING,DONE,CLOSE;
	}

	@Override
	public void preStart() throws Exception {
		System.out.println("MyWorker is starting");
	
	}

	@Override
	public void postStop() throws Exception {
		System.out.println("MyWorker is stopping");
	}

	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg== Msg.WORKING){
			System.out.println("I am working");
		}
		if(msg== Msg.CLOSE){
			System.out.println("I will shutdown");
			getSender().tell(Msg.CLOSE, getSelf());
			getContext().stop(getSelf());
		}
		else{
			unhandled(msg);
		}
		
		
	}

}
