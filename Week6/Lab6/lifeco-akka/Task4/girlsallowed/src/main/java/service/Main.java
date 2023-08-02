package service;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.message.RegisterMessage;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "girlsallowed");
        System.out.println("Girlsallowed initialized with ref = " + ref);

        ActorSelection selection = system.actorSelection("akka.tcp://default@localhost:2550/user/broker");
        selection.tell(new RegisterMessage(), ref); //register with broker
    }
}
