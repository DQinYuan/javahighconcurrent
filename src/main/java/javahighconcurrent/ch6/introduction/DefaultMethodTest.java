package javahighconcurrent.ch6.introduction;

/**
 * java默认方法的相关demo
 */

public class DefaultMethodTest {
    public static void main(String[] args) {
        Mule m = new Mule();
        m.run();
        m.breath();
    }
}

interface IHorse{
    void eat();

    default void run(){
        System.out.println("horses run");
    }
}

interface IAnimal{
    default void breath(){
        System.out.println("breath");
    }
}

interface IDonkey {
    void eat();
    default void run(){
        System.out.println("Donkey run");
    }
}

class Mule implements IHorse, IDonkey, IAnimal{

    @Override
    public void eat() {
        System.out.println("Mule eat");
    }

    /**
     * 为了解决多继承中的父类方法重名问题
     * 这里指定继承自IHorse
     */
    @Override
    public void run() {
        IHorse.super.run();
    }

}
