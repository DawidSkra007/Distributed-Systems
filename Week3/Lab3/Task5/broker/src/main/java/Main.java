import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.xml.ws.Endpoint;

import service.broker.LocalBrokerService;

public class Main {
    public static void main(String[] args) throws UnknownHostException, IOException {
        LinkedList<String> urls = new LinkedList<>();
        if (args.length == 0) { //if no arguments specified, run auldfellas
            urls.add("http://0.0.0.0:9001/quotations"); 
        } else {
            for (int i = 0; i < args.length; i++) {
                urls.add(args[i]);
                //System.out.println("Passed Arg: \n" + args[i].toString());
            }
        }
        LocalBrokerService broker = new LocalBrokerService(urls);
        Endpoint.publish("http://0.0.0.0:9012/quotations", broker);
        
        // TEST VALUE
        // ClientInfo client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49,
        // 1.5494, 80, false, false); 
        // broker.getQuotations(client); // invoking method in broker Service

        JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
        ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "broker", 9012, "path=http://0.0.0.0:9012/quotations?wsdl");
        jmdns.registerService(serviceInfo);
    }
}