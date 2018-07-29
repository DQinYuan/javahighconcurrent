package javahighconcurrent.ch6.introduction;

import java.util.Arrays;
import java.util.function.IntConsumer;

/**
 * 通过addThen组装IntConsumer
 */
public class IntConsumerTest {

    static int[] arr = {1,3,4,5};

    public static void main(String[] args) {
        IntConsumer outprintln = System.out::println;
        IntConsumer errprintln = System.err::println;
        Arrays.stream(arr).forEach(outprintln.andThen(errprintln));
    }

}
