package service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import service.message.ClientInfoMessage;
import service.message.ClientMessage;
import scala.concurrent.duration.Duration;
import service.message.OfferMessage;
import service.message.QuotationMessage;
import service.message.RegisterMessage;
import service.message.TimeOutMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Broker extends AbstractActor {

    private Map<Long, OfferMessage> cache = new HashMap<>();
    private List<ActorRef> actors = new ArrayList<>(); // list of quoters
    private ActorRef clientRef = null;
    private long token = 0;

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()  //Registry 
            .match(RegisterMessage.class,
                msg -> {
                    // quoters register with broker
                    actors.add(getSender());
                    System.out.println(getSender() + " registered. " + actors.size() + " services available");
        })
        //Receive request from client
        .match(ClientInfoMessage.class, msg -> {
            //System.out.println("IN HERE");
            clientRef = getSender();
            for (ActorRef ref : actors) { // send request to all quoters
                ref.tell(new ClientMessage(token, msg.getClientInfo()), getSelf());
            }
            cache.put(token, new OfferMessage(msg.getClientInfo(), new LinkedList<>()));
            getContext().system().scheduler().scheduleOnce(Duration.create(2, TimeUnit.SECONDS), getSelf(),
                new TimeOutMessage(token++),
                getContext().dispatcher(),
                null);
        })
        // get quotations from quoter service 
        .match(QuotationMessage.class, msg -> {
            if(cache.containsKey(msg.getToken())) {
                cache.get(msg.getToken()).getQuotations().add(msg.getQuotation()); // add quote to offerMessage
            } else { 
                System.out.println("Error: " + msg.getToken() + " doesn't exist.");
            }
        })
        // timeout
        .match(TimeOutMessage.class, msg -> {
            clientRef.tell(cache.get(msg.getToken()), getSelf());
            cache.remove(msg.getToken()); // send offer to client and remove from cache
        })
        .build();
    }
    
}
