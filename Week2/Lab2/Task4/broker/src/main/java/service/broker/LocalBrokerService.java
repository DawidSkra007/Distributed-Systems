package service.broker;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;
import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 *
 */
public class LocalBrokerService implements BrokerService {
    private static Registry serviceRegistry;

	
	@Override
	public List<Quotation> getQuotations(ClientInfo info) throws AccessException, RemoteException, NotBoundException {
		List<Quotation> quotations = new LinkedList<Quotation>();
		
		for (String name : serviceRegistry.list()) {
			if (name.startsWith("qs-")) {
				QuotationService service = (QuotationService) serviceRegistry.lookup(name);
				quotations.add(service.generateQuotation(info));
			}
		}

		return quotations;
	}
}
