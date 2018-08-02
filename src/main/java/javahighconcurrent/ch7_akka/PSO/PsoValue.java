package javahighconcurrent.ch7_akka.PSO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PsoValue {
    //投资方案的收益值
    final double value;
    /**
     * x[1...4]分别表示第1年到第4年的投资额
     * 本程序忽略了x[0]
     */
    final List<Double> x;

    public PsoValue(double value, List<Double> x) {
        this.value = value;
        List<Double> b = new ArrayList<>();
        b.addAll(x);   //把x复制了一份，避免影响原来的x列表
        this.x = Collections.unmodifiableList(b);
    }

    public double getValue() {
        return value;
    }

    public List<Double> getX() {
        return x;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("value:").append(value).append("\n")
                .append(x.toString());
        return sb.toString();
    }
}
