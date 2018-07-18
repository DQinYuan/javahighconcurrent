package javahighconcurrent.ch7_akka.EchoServer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.io.Tcp;

public class Main {
    
    public static void main(String[] args) {
        ActorSystem mySystem = ActorSystem.create("mySystem");
        //Akka将整个tcp层抽象为一个actor
        ActorRef tcpManager = Tcp.get(mySystem).getManager();
        Props accepterProps = Props.create(Accepter.class, tcpManager);
        ActorRef accepter = mySystem.actorOf(accepterProps, "accepter");
        accepter.tell(12345, ActorRef.noSender());
    }
    
}
