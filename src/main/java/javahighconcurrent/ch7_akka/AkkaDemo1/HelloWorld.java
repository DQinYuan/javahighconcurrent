package javahighconcurrent.ch7_akka.AkkaDemo1;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class HelloWorld extends UntypedActor{
	ActorRef greeter;
	//4.prestart是Akka的回调方法，在Actor启动前就会被Akka框架调用，完成初始化工作
	 @Override
     public void preStart() {
       // create the greeter actor,因为是在HelloWorld的上下文中调用，所以它是属于HelloWorld的子Actor
       greeter=getContext().actorOf(Props.create(Greeter.class),"greeter");
       System.out.println("Greeter Actor Path:"+greeter.path());
       //发送Greet消息
       greeter.tell(Greeter.Msg.GREET,getSelf());
     }
	 //7.又给Greeter tell了一下发了个消息过去， 然后又stop了自己，也就是停止了HelloWorld这个Actor 他就再也不能收消息了 
     @Override
     public void onReceive(Object msg) {
       if (msg == Greeter.Msg.DONE) {
    	 greeter.tell(Greeter.Msg.GREET,getSelf());
    	 //停止自身
         getContext().stop(getSelf());
       } 
       else unhandled(msg);
     }
}
