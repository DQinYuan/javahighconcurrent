package javahighconcurrent.personstream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyTest {



    @Test
    public void test(){
        ArrayList<Person> persons = new ArrayList<>();

        persons.add(new Person(10, "Tom", Person.Country.BeiJing));
        persons.add(new Person(20, "Tom", Person.Country.ShangHai));
        persons.add(new Person(15, "Mary", Person.Country.HangZhou));
        persons.add(new Person(25, "Worker", Person.Country.HangZhou));

        //断言，用于filter或者分块
        Predicate<Person> isTom = p -> p.name.equals("Tom");
        //数据分块
        Map<Boolean, List<Person>> map = persons.stream().collect(Collectors.partitioningBy(isTom));//按照Tom分区
        map.get(true).stream().map(p -> p.name).forEach(System.out::println);

        //数据分组，分组与分块的不同是，分块只能分成两组
        //ordinal返回枚举值的序号
        Function<Person, Integer> country = p -> p.country.ordinal();
        persons.stream().collect(Collectors.groupingBy(country));

        //把流转换成字符串，增加分割符，前缀和后缀
        System.out.println(persons.stream().map(p -> p.name).collect(Collectors.joining("/", "[", "]")));
    }

}
