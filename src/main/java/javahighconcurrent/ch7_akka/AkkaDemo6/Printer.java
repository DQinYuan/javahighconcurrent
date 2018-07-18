package javahighconcurrent.ch7_akka.AkkaDemo6;


import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import javahighconcurrent.ch7_akka.AkkaDemo4.MyWorker.Msg;

public class Printer extends UntypedActor{
	private final LoggingAdapter log=Logging.getLogger(getContext().system(),this);
	
	@Override
	public void onReceive(Object msg) throws Exception {
		System.out.println("Print收到消息");
        System.out.println(msg);
        if(msg instanceof Integer){
			System.out.println("Printer:"+msg);
		}
		if(msg == Msg.DONE){
			log.info("Stop Working");
		}
		if(msg == Msg.CLOSE){
			log.info("I will shutdown");
			getSender().tell(Msg.CLOSE,getSelf());
			getContext().stop(getSelf());
		}else{
			unhandled(msg);
		}
	}

}
