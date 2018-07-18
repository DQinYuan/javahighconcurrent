package javahighconcurrent.ch7_akka.AkkaDemo3;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

//学会对子Actor的执行监督策略和流程
public class SupervisorMain {
	public static void customStrategy(ActorSystem system){
		ActorRef a=system.actorOf(Props.create(Supervisor.class),"Supervisor");
		//1.重点是这一行 向a这个父Actor发送一个Props对象，这样父Actor在OnReceive()方法中接收并生成一个Actor并对这个Actor进行监督.
		a.tell(Props.create(RestartActor.class), ActorRef.noSender());
		ActorSelection sel=system.actorSelection("akka://lifecycle/user/Supervisor/restartActor");
		/*5.这里的for循环会向RestartActor发送100个Restart消息过去，然后RestartActor的Receive()方法中匹配到这个消息类型
		 * 会执行一个发生空指针异常的动作
		 */
		for(int i=0;i<100;i++){
			sel.tell(RestartActor.Msg.RESTART,ActorRef.noSender());
		}
	}
	public static void main(String[] args) {
		ActorSystem system=ActorSystem.create("lifecycle",ConfigFactory.load("lifecycle.conf"));
		customStrategy(system);		
	}
}
