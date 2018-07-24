
package javahighconcurrent.ch5.network.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AIOEchoServer {
    public final static int PORT = 8000;
    private AsynchronousServerSocketChannel server;
    public AIOEchoServer() throws IOException {
        server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(PORT));
    }

    public void start() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("Server listen on " + PORT);
        //注册事件和事件完成后的处理器
        //方法参数:附件(这里不需要,为null), 处理器
        server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);

            //连接成功时回调
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                System.out.println(Thread.currentThread().getName());
                Future<Integer> writeResult=null;
                try {
                    buffer.clear();
                    //这里的read也是异步的,返回一个Feature,所以这样调用future.get将其改为同步方法
                    result.read(buffer).get(100, TimeUnit.SECONDS);
                    buffer.flip();
                    //write也是异步的,返回一个Feature
                    writeResult=result.write(buffer);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //准备处理下一个连接
                        server.accept(null, this);
                        //在关闭连接之前,要确保write操作依据完成,因此,先使用get进行等待
                        writeResult.get();
                        //关闭连接
                        result.close();
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                }
            }

            //连接失败时回调
            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("failed: " + exc);
            }
        });
    }

    public static void main(String args[]) throws Exception {
        new AIOEchoServer().start();
        // 主线程可以继续自己的行为
        while (true) {
            Thread.sleep(1000);
        }
    }
}
