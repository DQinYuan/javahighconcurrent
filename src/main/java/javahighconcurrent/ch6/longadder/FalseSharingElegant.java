package javahighconcurrent.ch6.longadder;

import sun.misc.Contended;

public class FalseSharingElegant implements Runnable {

    public final static int NUM_THREADS = 4;
    public final static long ITERATIONS = 500L * 1000L * 1000L;   //5亿次
    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];
    static {
        for ( int i = 0; i < longs.length; i++ ){
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharingElegant(int arrayIndex) {
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
            threads[i] = new Thread(new FalseSharingElegant(i));
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

    /**
     * java8中对于 伪共享的官方解决方案
     * 必须要加额外的JVM参数： -XX:-RestrictContended
     * 该注解才能生效
     * 否则该注解会被忽略
     * @sun.misc.Contented
     */
    @Contended
    public final static class VolatileLong {
        //public long p11, p22, p33, p44, p55, p66, p77;
        public volatile long value = 0L;
        //public long p1, p2, p3, p4, p5, p6, p7;
    }
}
