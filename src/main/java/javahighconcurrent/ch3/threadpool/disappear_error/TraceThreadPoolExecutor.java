package javahighconcurrent.ch3.threadpool.disappear_error;

import java.util.concurrent.*;

/**
 * 使用这个改进的线程池能够获得报错的线程的提交位置的信息
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void execute(Runnable task) {
        super.execute(wrap(task, clientTrace(), Thread.currentThread().getName()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task, clientTrace(), Thread.currentThread().getName()));
    }

    private Exception clientTrace(){
        return new Exception("Client stack trace");
    }

    private Runnable wrap(final Runnable task, final Exception clientStack,
                          String clientThreadName){   //利用clientThreadName可以打印出父线程名
        return new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e){
                    clientStack.printStackTrace();
                    throw e;
                }
            }
        };
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pools = new TraceThreadPoolExecutor(0, Integer.MAX_VALUE,
                0L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

        for ( int i = 0; i < 5; i++ ){
            pools.execute(new DivTask(100 ,i));
        }
    }
}
