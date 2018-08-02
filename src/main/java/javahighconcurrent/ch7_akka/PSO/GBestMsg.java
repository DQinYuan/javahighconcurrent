package javahighconcurrent.ch7_akka.PSO;

/**
 * 代表全局最优
 */
public final class GBestMsg {
    final PsoValue value;

    public GBestMsg(PsoValue value) {
        this.value = value;
    }

    public PsoValue getValue(){
        return value;
    }
}
