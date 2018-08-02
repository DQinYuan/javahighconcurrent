package javahighconcurrent.ch5.future_model.myfuture;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        Data data = client.request("name");
        System.out.println("请求完毕");
        try {
            //模拟处理业务逻辑的过程
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据 = " + data.getResult());
    }
}
