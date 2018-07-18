package javahighconcurrent.ch7_akka.EchoServer;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp.Bound;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.Connected;
import akka.io.TcpMessage;

import java.net.InetSocketAddress;

public class Accepter extends UntypedActor {

    private final ActorRef tcpManager;
    
    public Accepter(ActorRef tcpManager) {
        this.tcpManager = tcpManager;
    }
    
    @Override
    public void onReceive(Object msg) throws Exception {
        System.out.println("Accepter received:" + msg);
        
        if (msg instanceof Integer) {
            final int port = (Integer) msg;
            final InetSocketAddress endpoint = new InetSocketAddress("localhost", port);
            final Object cmd = TcpMessage.bind(getSelf(), endpoint, 100);
            tcpManager.tell(cmd, getSelf());
        } else if (msg instanceof Bound) {
            tcpManager.tell(msg, getSelf());
        } else if (msg instanceof CommandFailed) {
            getContext().stop(getSelf());
        } else if (msg instanceof Connected) {
            final Connected conn = (Connected) msg;
            tcpManager.tell(conn, getSelf());
            final ActorRef handler = getContext().actorOf(Props.create(Handler.class));
            getSender().tell(TcpMessage.register(handler), getSelf());
        }
    }
    
}
