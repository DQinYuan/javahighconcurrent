package javahighconcurrent.ch7_akka.AkkaDemo4;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyWorker extends UntypedActor{
	private final LoggingAdapter log=Logging.getLogger(getContext().system(),this);
	public static enum Msg{
		WORKING,DONE,CLOSE;
	}
	@Override
	public void onReceive(Object msg) throws Exception {
		//15行到24行是后面AkkaDemo6实验所需的方法
		if(msg instanceof Integer){
			int i=(Integer) msg;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			getSender().tell(i*i, getSelf());
		}
		if(msg== Msg.WORKING){
			log.info("i am working");
		}
		if(msg== Msg.DONE){
			log.info("Stop working");
		}
		if(msg== Msg.CLOSE){
			log.info("i will shutdown");
			getSender().tell(Msg.CLOSE,getSelf());
			getContext().stop(getSelf());
		}else{
			unhandled(msg);
		}
		
	}

}
