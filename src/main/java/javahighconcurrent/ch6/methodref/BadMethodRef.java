package javahighconcurrent.ch6.methodref;

import java.util.ArrayList;
import java.util.List;

public class BadMethodRef {

    public static void main(String[] args) {
        List<Double> numbers = new ArrayList<>();
        for ( int i = 0; i < 10; i++ ){
            numbers.add(Double.valueOf(i));
        }
        /**
         * 这里编译器无法识别流中的元素是作为方法参数还是调用目标，所以会报错
         */
        //numbers.stream().map(Double::toString).forEach(System.out::println);
    }

}
