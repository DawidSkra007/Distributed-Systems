package service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
// import service.core.ClientInfo;
// import service.message.ClientInfoMessage;
// import service.message.RegisterMessage;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(Broker.class), "broker");
        System.out.println("Broker initialized with ref = " + ref);

        // TEST
        // ref.tell(new RegisterMessage(), ref);
        // ref.tell(new ClientInfoMessage(new ClientInfo("Dawid Skraba", ClientInfo.FEMALE, 49, 1.5494, 80, false, false)), ref);
    }
}
