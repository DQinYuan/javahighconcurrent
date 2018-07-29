package javahighconcurrent.ch6.parallelstream;

import java.util.Arrays;
import java.util.Random;

public class ParallelArrayTest {
    public static void main(String[] args) {
        int[] arr = new int[1000000];
        Random r = new Random();
        Arrays.setAll(arr, i -> r.nextInt());

        //setAll的并行版本
        Arrays.parallelSetAll(arr, i -> r.nextInt());

        //并行排序
        Arrays.parallelSort(arr);

    }
}
