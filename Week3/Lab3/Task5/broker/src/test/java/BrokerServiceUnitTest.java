import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.junit.BeforeClass;
import org.junit.Test;

import service.broker.LocalBrokerService;
import service.core.ClientInfo;
import service.core.Quotation;

public class BrokerServiceUnitTest {

    static LocalBrokerService broker;
    static LinkedList<String> urls = new LinkedList<>();

    @BeforeClass
    public static void setup() throws MalformedURLException {
        broker = new LocalBrokerService();
        Endpoint.publish( "http://0.0.0.0:9006/broker", broker);
}

@Test
public void connectionTest() throws Exception {
    URL wsdlUrl = new URL("http://localhost:9006/broker?wsdl"); 
    QName serviceName = new QName("http://core.service/", "broker"); 
    Service service = Service.create(wsdlUrl, serviceName);
    assertNotNull(service);
}

@Test
public void QuotationTest() throws Exception {
    LinkedList<Quotation> quotations = new LinkedList<>();
    ClientInfo client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 
    1.5494, 80, false, false);
     quotations = broker.getQuotations(client);
     assertEquals(quotations.size(), 0);
    }

}
