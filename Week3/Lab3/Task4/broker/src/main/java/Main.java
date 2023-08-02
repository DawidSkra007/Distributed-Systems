import java.net.MalformedURLException;
import java.util.LinkedList;

import javax.xml.ws.Endpoint;

import service.broker.LocalBrokerService;
import service.core.ClientInfo;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        LinkedList<String> urls = new LinkedList<>();
        //urls.add("http://0.0.0.0:9001/quotations"); //TEST
        for (int i = 0; i < args.length; i++) {
            urls.add(args[i]);
            //System.out.println("Passed Arg: \n" + args[i].toString());
        }
        LocalBrokerService broker = new LocalBrokerService(urls);
        Endpoint.publish("http://0.0.0.0:9012/broker", broker);
        
        // TEST VALUE
        // ClientInfo client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49,
        // 1.5494, 80, false, false); 
        // broker.getQuotations(client); // invoking method in broker Service
    }
}