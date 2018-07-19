package javahighconcurrent.ch5.concurrent_sort;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 奇偶交换排序，冒泡排序的一种并行化改进
 * 将排序分成两个阶段，奇交换和偶交换。
 * 奇交换：只比较奇数索引及其相邻后继元素
 * 偶交换：只比较偶数索引及其后继元素
 * 奇偶交换总是成对出现，这样才能保证比较涉及到数组中每一个元素
 *
 * 通过这种方式解开了冒泡排序的数据相关性
 */
public class OddEvenSort {

    static int exchFlag = 1;

    static ExecutorService pool = Executors.newCachedThreadPool();

    static synchronized void setExchFlag(int v){
        exchFlag = v;
    }

    public static int getExchFlag() {
        return exchFlag;
    }

    public static class OddEvenSortTask implements Runnable{
        int i;
        CountDownLatch latch;
        int[] arr;

        public OddEvenSortTask(int i, CountDownLatch latch, int[] arr){
            this.i = i;
            this.latch = latch;
            this.arr = arr;
        }

        @Override
        public void run() {
            if ( arr[i] > arr[i + 1] ){
                int temp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = temp;
                setExchFlag(1);
            }
            latch.countDown();
        }
    }

    public static void pOddEventSort(int[] arr) throws InterruptedException {
        int start = 0;   //0代表偶交换阶段， 1代表奇交换阶段
        while ( getExchFlag() == 1 || start == 1 ){  //确保奇偶交换成对出现
            setExchFlag(0);
            //奇交换（start = 1），且数组长度为偶数时，只需要len/2 - 1次比较
            CountDownLatch latch = new CountDownLatch(arr.length / 2 - (arr.length%2==0?start:0));
            for ( int i = start; i < arr.length - 1; i+= 2 ){
                pool.submit(new OddEvenSortTask(i, latch, arr));
            }
            latch.await();   //等待所有线程结束再开始下一阶段

            start = start == 0?1:0;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] arr = new int[]{10, 9, 7, 8, 1, 2, 11};
        pOddEventSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
