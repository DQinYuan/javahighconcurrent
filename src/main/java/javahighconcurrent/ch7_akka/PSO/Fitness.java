package javahighconcurrent.ch7_akka.PSO;

import java.util.List;

public class Fitness {
    /**
     * 将xp[1..4]换算成value
     * @param x
     * @return
     */
    public static double fitness(List<Double> x){
        double sum = 0;
        for ( int i = 1; i < x.size(); i++ ){
            sum += Math.sqrt(x.get(i));
        }
        return sum;
    }

}
