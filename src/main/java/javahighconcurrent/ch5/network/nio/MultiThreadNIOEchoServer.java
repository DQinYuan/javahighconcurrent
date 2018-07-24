
package javahighconcurrent.ch5.network.nio;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadNIOEchoServer {
    //用于统计服务器线程在一个客户端上花费的时间
	public static Map<Socket,Long> geym_time_stat=new HashMap<Socket,Long>(10240);
    class EchoClient {
        private LinkedList<ByteBuffer> outq;

        EchoClient() {
            outq = new LinkedList<ByteBuffer>();
        }

        // Return the output queue.
        public LinkedList<ByteBuffer> getOutputQueue() {
            return outq;
        }

        // Enqueue a ByteBuffer on the output queue.
        public void enqueue(ByteBuffer bb) {
            outq.addFirst(bb);
        }
    }

    class HandleMsg implements Runnable{
        SelectionKey sk;
        ByteBuffer bb;
        public HandleMsg(SelectionKey sk,ByteBuffer bb){
            this.sk=sk;
            this.bb=bb;
        }
        @Override
        public void run() {
            EchoClient echoClient = (EchoClient) sk.attachment();
            echoClient.enqueue(bb);

            // 再注册一个写操作
            sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            //强迫selector立即返回
            selector.wakeup();
        }
    }
    
    private Selector selector;
    private ExecutorService  tp=Executors.newCachedThreadPool();
    /**
     * Accept a new client and set it up for reading.
     */
    private void doAccept(SelectionKey sk) {

        //sk.channel()方法用于取出这个Key对应的channel
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        SocketChannel clientChannel;
        try {
            //当有新的客户端接入时,就会有一个新的Channel产生代表这个连接
            clientChannel = server.accept();
            clientChannel.configureBlocking(false);

            // Register this channel for reading.
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            // Allocate an EchoClient instance and attach it to this selection key.
            EchoClient echoClient = new EchoClient();
            //将echoClient作为附件附加到key上,这样在整个连接处理过程中,就都可以共享这个echoClient实例
            clientKey.attach(echoClient);

            InetAddress clientAddress = clientChannel.socket().getInetAddress();
            System.out.println("Accepted connection from " + clientAddress.getHostAddress() + ".");
        } catch (Exception e) {
            System.out.println("Failed to accept new client.");
            e.printStackTrace();
        }
    }

    /**
     * Read from a client. Enqueue the data on the clients output
     * queue and set the selector to notify on OP_WRITE.
     */
    private void doRead(SelectionKey sk) {
        //sk.channel()方法用于取出这个Key对应的channel
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;

        try {
            len = channel.read(bb);
            if (len < 0) {
                disconnect(sk);
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed to read from client.");
            e.printStackTrace();
            disconnect(sk);
            return;
        }

        // Flip the buffer.
        bb.flip();
        tp.execute(new HandleMsg(sk,bb));
    }

    /**
     * Called when a SelectionKey is ready for writing.
     */
    private void doWrite(SelectionKey sk) {
        //sk.channel()方法用于取出这个Key对应的channel
        SocketChannel channel = (SocketChannel) sk.channel();
        //获得之前用attach方法放进去的附件
        EchoClient echoClient = (EchoClient) sk.attachment();
        LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();

        ByteBuffer bb = outq.getLast();
        try {
            int len = channel.write(bb);
            if (len == -1) {
                disconnect(sk);
                return;
            }

            if (bb.remaining() == 0) {
                // The buffer was completely written, remove it.
                outq.removeLast();
            }
        } catch (Exception e) {
            System.out.println("Failed to write to client.");
            e.printStackTrace();
            disconnect(sk);
        }

        // 数据全部写完后,一定要将写事件移除
        if (outq.size() == 0) {
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private void disconnect(SelectionKey sk) {
        //sk.channel()方法用于取出这个Key对应的channel
        SocketChannel channel = (SocketChannel) sk.channel();

        InetAddress clientAddress = channel.socket().getInetAddress();
        System.out.println(clientAddress.getHostAddress() + " disconnected.");

        try {
            channel.close();
        } catch (Exception e) {
            System.out.println("Failed to close client socket channel.");
            e.printStackTrace();
        }
    }

    private void startServer() throws Exception {
        selector = SelectorProvider.provider().openSelector();

        // Create non-blocking server socket.
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //设置为非阻塞模式,其实SocketChannel也可以像传统Socket那样工作
        ssc.configureBlocking(false);

        // Bind the server socket to localhost.
//        InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), 8000);
        InetSocketAddress isa = new InetSocketAddress(8000);
        ssc.socket().bind(isa);

        // Register the socket for select events.
        //如果是NIO客户端,则应该注册OP_CONNECT事件
        SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
        
        // Loop forever.
        for (;;) {
            //select是一个阻塞方法,返回值是已经就绪的SelectionKey的数量
             selector.select();
//            if(selector.selectNow()==0){
//                continue;
//            }
            //获取已经准备好的Key
            Set readyKeys = selector.selectedKeys();
            Iterator i = readyKeys.iterator();
            long e=0;
            while (i.hasNext()) {
                SelectionKey sk = (SelectionKey) i.next();
                //处理完一个Key后,必须将其移除,不然会重复处理
                i.remove();
                
                if (sk.isAcceptable()) {
                    doAccept(sk);
                }
                else if (sk.isValid() && sk.isReadable()) {
                	if(!geym_time_stat.containsKey(((SocketChannel)sk.channel()).socket()))
                		geym_time_stat.put(((SocketChannel)sk.channel()).socket(), 
                			System.currentTimeMillis());
                    doRead(sk);
                }
                else if (sk.isValid() && sk.isWritable()) {
                    doWrite(sk);
                    e=System.currentTimeMillis();
                    long b=geym_time_stat.remove(((SocketChannel)sk.channel()).socket());
                    System.out.println("spend:"+(e-b)+"ms");
                }
            }
        }
    }

    // Main entry point.
    public static void main(String[] args) {
        MultiThreadNIOEchoServer echoServer = new MultiThreadNIOEchoServer();
        try {
            echoServer.startServer();
        } catch (Exception e) {
            System.out.println("Exception caught, program exiting...");
            e.printStackTrace();
        }
    }
}
