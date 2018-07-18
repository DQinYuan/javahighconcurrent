package javahighconcurrent.ch5.disruptor.falseshare.testfalseshare;

/**
 * CPU cache line: CPU读取CPU cache的单位，主流CPU的Cache line是64B
 * 本来不共享的数据出现在同一个CPU cache line中，会造成伪共享问题
 * 通过给数据填充padding是指达到CPU cache line大小能够避免这个问题
 *
 * 以下优化在win10 jdk8上测试有效
 *
 * 测试CPU cache line的方法见：
 * http://blog.jobbole.com/85185/
 */
public class FalseSharing implements Runnable {

    public final static int NUM_THREADS = 4;
    public final static long ITERATIONS = 500L * 1000L * 1000L;   //5亿次
    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];
    static {
        for ( int i = 0; i < longs.length; i++ ){
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("duration = " + (System.currentTimeMillis() - start));
    }

    private static void runTest(){
        Thread[] threads = new Thread[NUM_THREADS];
        for ( int i = 0; i < threads.length; i++ ){
            threads[i] = new Thread(new FalseSharing(i));
        }

        for ( Thread t : threads ){
            t.start();
        }

        for ( Thread t : threads ){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        long i = ITERATIONS + 1;
        while ( 0 != --i ){
            longs[arrayIndex].value = i;
        }
    }

    //左边放56字节，右边放56字节，能够确保value无冲突地位于缓存中
    //如果只在左边或者右边放56字节的话，经测试，会造成程序结果不稳定
    public final static class VolatileLong {   //64B，一个CPU cache line大小
        public long p11, p22, p33, p44, p55, p66, p77;
        public volatile long value = 0L;
        public long p1, p2, p3, p4, p5, p6, p7;  //padding 用于填充cache line
    }
}
