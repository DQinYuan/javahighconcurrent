package javahighconcurrent.ch6.parallelstream;

import java.util.ArrayList;
import java.util.List;

/**
 * 并行求平均数
 */
public class Student {
    int score;

    public Student(int score) {
        this.score = score;
    }

    public static void main(String[] args) {
        List<Student> ss = new ArrayList<>();
        for ( int i = 0; i < 10000000; i++ ){
            ss.add(new Student(i));
        }
        long start = System.currentTimeMillis();
        //串行
        //double ave = ss.stream().mapToInt(s->s.score).average().getAsDouble();
        // 并行
        double ave = ss.parallelStream().mapToInt(s->s.score).average().getAsDouble();
        System.out.println(ave);
        System.out.println("time:" + (System.currentTimeMillis() - start));
    }
}
