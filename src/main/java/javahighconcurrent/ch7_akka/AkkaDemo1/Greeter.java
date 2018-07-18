package javahighconcurrent.ch7_akka.AkkaDemo1;

import akka.actor.UntypedActor;

public class Greeter extends UntypedActor{
	public static enum Msg {
        GREET, DONE
      }
      //6.收到了消息后执行onReceive方法，
      // 很明显 这个消息是从HelloWorld收到的 所以14行的getSender就是HelloWorld这个Actor,现在又发回去
      @Override
      public void onReceive(Object msg) {
        if (msg == Msg.GREET) {
          System.out.println("Hello World!");
          getSender().tell(Msg.DONE, getSelf());
        } 
        else unhandled(msg);
      }
}
