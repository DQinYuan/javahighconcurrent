package javahighconcurrent.ch7_akka.AkkaDemo5;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public class BabyActor extends UntypedActor{
	private final LoggingAdapter log=Logging.getLogger(getContext().system(),this);
	public static enum Msg{
		SLEEP,PLAY,CLOSE;
	}
	/*
	 * 
	 * Procedure对象就代表了一个Actor的多种状态。  感觉讲到这里杜总就应该秒懂了 
	 */
	Procedure<Object> angry=new Procedure<Object>() {
		public void apply(Object message){
			System.out.println("angryApply:"+message);
			if(message== Msg.SLEEP){
				getSender().tell("i am already angry",getSelf());
				System.out.println("I am already angry");
			}else if(message== Msg.PLAY){
				System.out.println("i like playing");
				getContext().become(happy);
			}
		}
	};
	Procedure<Object> happy=new Procedure<Object>() {

		@Override
		public void apply(Object message) throws Exception {
			System.out.println("happyApply:"+message);
			if(message== Msg.PLAY){
				getSender().tell("i am already happy:-)", getSelf());
				System.out.println("I am already happy:-)");
			}else if(message== Msg.SLEEP){
				System.out.println("i dont want to sleep");
				getContext().become(angry);
			}
		}
		
		
	};
	@Override
	public void onReceive(Object msg) throws Exception {
		System.out.println("OnReceive:"+msg);
		if(msg== Msg.SLEEP){
			getContext().become(angry);
		}else if(msg== Msg.PLAY){
			getContext().become(happy);
		}else{
			unhandled(msg);
		}
	}

}
