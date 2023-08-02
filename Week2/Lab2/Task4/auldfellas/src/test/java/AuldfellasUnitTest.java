import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry; 
import java.rmi.server.UnicastRemoteObject;

import service.core.ClientInfo;
import service.core.Constants;
import service.core.Quotation;
import service.core.QuotationService; 
import service.auldfellas.AFQService;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AuldfellasUnitTest { 
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService afqService = new AFQService(); 
        try {
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(afqService,0);

            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService); 
        } catch (Exception e) {
            System.out.println("Trouble: " + e); 
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService) registry.lookup(Constants.AULD_FELLAS_SERVICE);
        assertNotNull(service); 
    }

    @Test
    public void QuatationTest() throws Exception {
        ClientInfo client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false);
        QuotationService service = (QuotationService) registry.lookup(Constants.AULD_FELLAS_SERVICE);
        Quotation quote = service.generateQuotation(client);
        assertEquals(quote.getClass(), Quotation.class);
        assertEquals(quote.company, "Auld Fellas Ltd."); 
        assertEquals(quote.reference, "AF001000");
        //System.out.println(quote.price);
    }
}