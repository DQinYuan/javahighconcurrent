package javahighconcurrent.ch6.introduction;

/**
 * 函数式接口的定义：
 *     接口只有一个“抽象方法”，注意该方法不能已经被Object实现
 */
@FunctionalInterface
public interface FunctionalInterfaceDemo {
    void handle(int i);
}

/**
 * 这不是一个函数式接口，因为equals已经被Object实现了
 */
interface NonFunc{
    boolean equals(Object obj);
}

/**
 * 下面的接口是一个函数式接口
 */
@FunctionalInterface
interface IntHandler{
    void hanle(int i);

    boolean equals(Object obj);
}
