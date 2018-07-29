package javahighconcurrent.ch6.parallelstream;

import java.util.stream.IntStream;

/**
 * 并行求质数
 */
public class PrimeUtil {

    public static boolean isPrime(int number){
        int tmp = number;
        if ( tmp < 2 ){
            return false;
        }
        for ( int i = 2; i <= Math.sqrt(tmp); i++ ){
            if ( tmp % i == 0 ){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        //加上parallel即可将一个流并行，此时PrimeUtil::isPrime会被并行调用
        long count = IntStream.range(1, 1000000).parallel().filter(PrimeUtil::isPrime).count();
        System.out.println(count);
        System.out.println("time:" + (System.currentTimeMillis() - start));
    }

}
