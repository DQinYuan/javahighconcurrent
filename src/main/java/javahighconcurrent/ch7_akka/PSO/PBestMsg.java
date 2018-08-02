package javahighconcurrent.ch7_akka.PSO;

/**
 * 代表个体最优
 */
public class PBestMsg {
    final PsoValue value;

    public PBestMsg(PsoValue value) {
        this.value = value;
    }

    public PsoValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
