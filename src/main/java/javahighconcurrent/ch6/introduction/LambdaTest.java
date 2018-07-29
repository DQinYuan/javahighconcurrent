package javahighconcurrent.ch6.introduction;

import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;

public class LambdaTest {

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1,2,3,4,5,6);
        numbers.forEach(System.out::println);

        System.out.println("-----");

        /**
         * 虽然num没有被声明为final
         * 但是实际上已经是不可变的了
         */
        int num = 2;
        Function<Integer, Integer> converter = (from) -> from * num;
        System.out.println(converter.apply(3));
        //num++;   //这行代码会导致编译不通过

        System.out.println("------");

    }

}
