package javahighconcurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {

    public static void main(String[] args) {
        List<String> test = new ArrayList<>();
        test.add("ABC");
        test.add("ABC");
        test.add("DEF");
        test.add("");
        List<String> result = test.stream()
                .distinct()
                .filter("ABC"::equals)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        System.out.println(result);

        test.stream();




    }

}
