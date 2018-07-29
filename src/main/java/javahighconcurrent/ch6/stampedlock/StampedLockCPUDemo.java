package javahighconcurrent.ch6.stampedlock;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock的一个小陷阱
 * StampedLock挂起线程时，使用的是Unsafe.park()函数，而park()函数在遇到线程中断时会直接返回
 * 而在StampedLock中，没有处理有关中断的逻辑，会导致阻塞在park上的线程被中断后，再次进入循环。
 * 而当退出条件得不到满足时，就会发生疯狂占中CPU的情况
 *
 * 可以用jstack pid 查看情况
 */
public class StampedLockCPUDemo {

    static Thread[] holdCpuThreads = new Thread[3];
    static final StampedLock lock = new StampedLock();

    public static void main(String[] args) throws InterruptedException {
        new Thread(){
            @Override
            public void run() {
                long readLong = lock.writeLock();
                LockSupport.parkNanos(600000000000L);  //600s
                lock.unlockWrite(readLong);
            }
        }.start();
        Thread.sleep(100);
        for ( int i = 0; i < 3; ++i ){
            holdCpuThreads[i] = new Thread(new HoldCPUReadThread());
            holdCpuThreads[i].start();
        }
        Thread.sleep(10000);
        //线程中断后，会占用CPU
        for ( int i = 0; i < 3; ++i ){
            holdCpuThreads[i].interrupt();
        }
    }

    private static class HoldCPUReadThread implements Runnable{

        @Override
        public void run() {
            long lockr = lock.readLock();
            System.out.println(Thread.currentThread().getName() + " 获取读锁");
            lock.unlockRead(lockr);
        }
    }

}
