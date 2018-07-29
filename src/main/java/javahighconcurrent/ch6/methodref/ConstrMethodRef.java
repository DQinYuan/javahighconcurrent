package javahighconcurrent.ch6.methodref;

import java.util.ArrayList;
import java.util.List;

public class ConstrMethodRef {
    @FunctionalInterface
    interface UserFactory<U extends User>{
        U create(int id, String name);
    }

    /**
     * 编译器会自动根据UserFactory的方法签名来选择合适的User构造函数赋给它
     */
    static UserFactory<User> uf = User::new;

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        for ( int i = 1; i < 10; i++ ){
            users.add(uf.create(i, "billy" + Integer.toString(i)));
        }
        users.stream().map(User::getName).forEach(System.out::println);
    }
}
