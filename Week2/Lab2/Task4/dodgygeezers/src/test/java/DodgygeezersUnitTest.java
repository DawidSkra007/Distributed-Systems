import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry; 
import java.rmi.server.UnicastRemoteObject;

import service.core.ClientInfo;
import service.core.Constants;
import service.core.Quotation;
import service.core.QuotationService;
import service.dodgygeezers.DGQService;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DodgygeezersUnitTest { 
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService dodgyService = new DGQService(); 
        try {
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(dodgyService,0);

            registry.bind(Constants.DODGY_GEEZERS_SERVICE, quotationService); 
        } catch (Exception e) {
            System.out.println("Trouble: " + e); 
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService) registry.lookup(Constants.DODGY_GEEZERS_SERVICE);
        assertNotNull(service); 
    }

    @Test
    public void QuatationTest() throws Exception {
        ClientInfo client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false);
        QuotationService service = (QuotationService) registry.lookup(Constants.DODGY_GEEZERS_SERVICE);
        Quotation quote = service.generateQuotation(client);
        assertEquals(quote.getClass(), Quotation.class);
        assertEquals(quote.company, "Dodgy Geezers Corp."); 
        assertEquals(quote.reference, "DG001000");
        //System.out.println(quote.price);
    }
}