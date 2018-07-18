package javahighconcurrent.ch5.singleton;

/**
 * 书中认为的单例模式的最优实现方案
 */
public class StaticSingleton {

    private StaticSingleton(){
        System.out.println("StaticSingleton is create");
    }

    private static class SingletonHolder{   //利用了JVM对内部类的延迟加载
        private static StaticSingleton instance = new StaticSingleton();
    }

    public static StaticSingleton getInstance(){   //只有在第一次调用该方法时，单例才会被创建
        return SingletonHolder.instance;
    }

}
