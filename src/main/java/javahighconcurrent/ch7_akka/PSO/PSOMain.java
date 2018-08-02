package javahighconcurrent.ch7_akka.PSO;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class PSOMain {

    //10万个粒子
    public static final int BIRD_COUNT = 100000;

    public static void main(String[] args) {
        ActorSystem system = ActorSystem
                .create("psoSystem", ConfigFactory.load("samplehello.conf"));
        system.actorOf(Props.create(MasterBird.class), "masterbird");
        for (  int i = 0; i < BIRD_COUNT; i++){
            system.actorOf(Props.create(Bird.class), "bird_" + i);
        }
    }

}
