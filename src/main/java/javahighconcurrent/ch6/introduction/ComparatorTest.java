package javahighconcurrent.ch6.introduction;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ComparatorTest {
    public static void main(String[] args) {
        /**
         * 有限按照长度排序，
         * 长度一样时按照大小不敏感的字母序排序
         */
        Comparator<String> cmp = Comparator.comparingInt(String::length)
                .thenComparing(String.CASE_INSENSITIVE_ORDER);

        List<String> test = Arrays.asList("a", "aaa", "aab", "bbb", "c", "aba");

        Collections.sort(test, cmp);

        System.out.println(test);
    }
}
