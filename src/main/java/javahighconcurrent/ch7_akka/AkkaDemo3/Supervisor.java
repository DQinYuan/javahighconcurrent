package javahighconcurrent.ch7_akka.AkkaDemo3;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.SupervisorStrategy.Directive;
import akka.actor.UntypedActor;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Supervisor extends UntypedActor {
    //3.自定义监督策略，遇到错误后在1分钟内进行3次重试，超过这个频率直接杀死Actor
    //这里定义的是监督策略的上限(重启次数的上限)，超过这个上限时系统不再进行处理，而是直接关闭
    private static SupervisorStrategy strategy = new OneForOneStrategy(3
            , Duration.create(1, TimeUnit.MINUTES),
            new Function<Throwable, Directive>() {

                @Override
                //4.这里是指Actor收到消息后进行相应处理时有可能会遇到的异常，然后对异常进行处理。
                public Directive apply(Throwable t) throws Exception {
                    //算术异常，比如除零错误
                    if (t instanceof ArithmeticException) {
                        System.out.println("meet ArithmeticException,just resume");
                        //不作任处理
                        return SupervisorStrategy.resume();
                    } else if (t instanceof NullPointerException) {
                        System.out.println("meet NullPointerException,restart");
                        //重启Actor
                        return SupervisorStrategy.restart();
                    } else if (t instanceof IllegalArgumentException) {
                        //停止Actor
                        return SupervisorStrategy.stop();
                    } else {
                        //escalate是英文升级的意思，表示将异常抛给更上层的Actor处理
                        return SupervisorStrategy.escalate();
                    }

                }
            });

    //覆盖父类的supervisorStrategy方法，设置使用自定义的监督策略
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    //2.收到一个对象时就会生成一个RestartActor,并对他进行监督(收到对象是在Main方法中的tell函数进行)
    public void onReceive(Object o) throws Exception {
        if (o instanceof Props) {
            getContext().actorOf((Props) o, "restartActor");
        } else {
            unhandled(o);
        }
    }

}
