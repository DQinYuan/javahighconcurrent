package javahighconcurrent.ch2;


public class NoVisibility {
    private static boolean ready;
    private static int number;

    private static void setReady(boolean re){
        ready = re;
    }

    private static class ReaderThread extends Thread{
        @Override
        public void run() {
            while (!ready);
            System.out.println(number);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReaderThread().start();
        Thread.sleep(1000);
        number = 42;
        setReady(true);
        Thread.sleep(10000);
    }
}
