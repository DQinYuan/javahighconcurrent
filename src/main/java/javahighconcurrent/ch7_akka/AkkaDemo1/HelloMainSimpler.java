package javahighconcurrent.ch7_akka.AkkaDemo1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

//1.首先建立一个思想， Akka应用是由消息驱动的，
// 它是相比于线程更细粒度的一种模型，书上把Actor比喻成一个人，人和人之间通信就如同微信对话一样。
public class HelloMainSimpler {
	public static void main(String[] args) {
		//2.ActorSystem指代维护和管理Actor的系统,一个应用只需要一个ActorSystem发，第一个参数为系统名称，第二个为配置文件
		ActorSystem system=ActorSystem.create("Hello",ConfigFactory.load("samplehello.conf"));
		//3.创建一个顶级Actor(Hello World) , 在这个顶级Actor的prestart方法中会创建一个Greeter子Actor
		ActorRef a=system.actorOf(Props.create(HelloWorld.class),"helloWorld");
		System.out.println("HelloWolrd Actor Path:"+a.path());
	}
	/*main函数跑完后会打印出5行代码，其中Greetor收到了2次消息，  HelloWorld收到了1次消息， 对照2个Actor的Syso和控制台对比即可
	*/
}
