package javahighconcurrent.ch7_akka.EchoServer;

import akka.actor.UntypedActor;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.io.TcpMessage;
import akka.util.ByteString;

public class Handler extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws Exception {
        System.out.println("Handler received:" + msg);
        
        if (msg instanceof Received) {
            final ByteString data = ((Received) msg).data();
            //将ByteString转换成可读String
            System.out.println(data.decodeString("utf-8"));
            //getSender().tell(TcpMessage.write(data), getSelf());       //原样返回
            getSender().tell(TcpMessage.write(ByteString.fromString("HTTP/1.1 200 OK\r\n")), getSelf());
        } else if (msg instanceof ConnectionClosed) {
            getContext().stop(getSelf());
        }
    }
    
}
