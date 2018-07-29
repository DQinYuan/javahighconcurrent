package javahighconcurrent.ch6.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock通过引入乐观读改进了读写锁
 */
public class Point {

    private double x, y;
    private final StampedLock s1 = new StampedLock();

    void move(double deltaX, double deltaY){
        long stamp = s1.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            s1.unlockWrite(stamp);
        }
    }

    double distanceFromOrigin(){
        //乐观读
        long stamp = s1.tryOptimisticRead();
        double currentX = x, currentY = y;
        if ( !s1.validate(stamp) ){
            //锁升级，如果此时有别的线程正在临界区，则可能会造成线程挂起
            //这里也可以选择不升级，而是类似于CAS那样循环申请乐观读锁
            stamp = s1.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                s1.unlockRead(stamp);
            }
        }

        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

}
