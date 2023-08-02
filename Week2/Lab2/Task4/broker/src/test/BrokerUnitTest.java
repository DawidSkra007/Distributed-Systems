import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import service.broker.LocalBrokerService;
import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Constants;
import service.core.Quotation;


import org.junit.*;
import static org.junit.Assert.assertNotNull;

public class BrokerUnitTest {
    private static Registry registry;
    @BeforeClass
    public static void setup() {
        LocalBrokerService service = new LocalBrokerService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(service, 0);
            registry.bind(Constants.BROKER_SERVICE, brokerService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionBrokerTest() throws Exception {
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void getQuotationsTest() throws Exception {
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        ClientInfo Client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false);

        List<Quotation> quotations = service.getQuotations(Client); // getting list of quotations
        System.out.println("Quotations list: " + quotations);
        assertNotNull(quotations);
    }
}

