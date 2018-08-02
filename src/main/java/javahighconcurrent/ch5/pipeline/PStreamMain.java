package javahighconcurrent.ch5.pipeline;

/**
 * 使用流水线模式，能够将原本存在数据依赖的流程并行化
 */
public class PStreamMain {

    public static void main(String[] args) {
        new Thread(new Plus()).start();
        new Thread(new Multiply()).start();
        new Thread(new Div()).start();

        for ( int i = 1; i <= 1000; i++ ){
            for ( int j = 1; j <= 1000; j++ ){
                Msg msg = new Msg();
                msg.i = i;
                msg.j = j;
                msg.orgStr = "((" + i + "+" + j + ")*" + i + ")/2";
                Plus.bq.add(msg);
            }
        }
    }

}
