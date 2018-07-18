package javahighconcurrent.ch7_akka.AkkaDemo3;

import akka.actor.UntypedActor;
import scala.Option;

public class RestartActor extends UntypedActor{
	public enum Msg{
		DONE,RESTART
	}
	@Override
	public void preStart() throws Exception {
		System.out.println("preStart hashCode:"+this.hashCode());
	}
	@Override
	public void postStop() throws Exception {
		System.out.println("postStop hashcode:"+this.hashCode());
	}
	@Override
	public void postRestart(Throwable reason) throws Exception {
		super.postRestart(reason);
		System.out.println("postRestart hashcode:"+this.hashCode());
	}
	@Override
	public void preRestart(Throwable reason, Option<Object> message) throws Exception {
		System.out.println("preRestart hashcode:"+this.hashCode());
	}
	
	@Override
	public void onReceive(Object msg) throws Exception {
		if(msg== Msg.DONE){
			getContext().stop(getSelf());
		}else if(msg== Msg.RESTART){
			System.out.println(((Object)null).toString());
			double a=0/0;
		}
		unhandled(msg);
	}

}
