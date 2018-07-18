package javahighconcurrent.ch5.singleton;

public class Main {
    public static void main(String[] args) {
        StaticSingleton.getInstance();
        StaticSingleton.getInstance();
    }
}
