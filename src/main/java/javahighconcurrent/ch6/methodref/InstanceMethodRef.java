package javahighconcurrent.ch6.methodref;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法引用是Java提出的用来简化lambda表达式的一种手段
 *  分为以下几种：
 *      1.静态方法引用：ClassName::methodName
 *      2.实例上的实例方法引用：instanceReference::methodName
 *      3.超类上的实例方法引用：super::methodName
 *      4.类型上的实例方法引用：ClassName::methodName
 *      5.构造方法引用：Class::new
 *      6.数组构造方法引用：TypeName::new
 *
 * Java会自动识别流中的元素作为调用目标还是方法的参数
 *
 */
public class InstanceMethodRef {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for ( int i = 1; i < 10; i++ ){
            users.add(new User(i, "billy" + Integer.toString(i)));
        }
        users.stream().map(User::getName).forEach(System.out::println);
    }


}
