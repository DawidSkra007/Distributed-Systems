package client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.text.NumberFormat;

import service.auldfellas.AFQService;
import service.broker.LocalBrokerService;
//import service.dodgygeezers.DGQService;
//import service.girlsallowed.GAQService;
import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Constants;
import service.core.Quotation;

public class Main {
	static Registry registry;
	static {
		// Create the services and bind them to the registry.
		try {
			registry = LocateRegistry.getRegistry(1099);
		} catch (RemoteException e) {
			System.out.println("Trouble: " + e);
		}
		try{
			 registry.bind(Constants.AULD_FELLAS_SERVICE, new AFQService());
			 //registry.bind(Constants.DODGY_GEEZERS_SERVICE, new DGQService()); TESTING 
			 registry.bind(Constants.BROKER_SERVICE, new LocalBrokerService());
             //registry.bind(Constants.GIRLS_ALLOWED_SERVICE, new GAQService()); TESTING 
		}
		catch(Exception e){
			System.out.println("binding Trouble: " + e);
		}
	}
	
	/**
	 * This is the starting point for the application. Here, we must
	 * get a reference to the Broker Service and then invoke the
	 * getQuotations() method on that service.
	 * 
	 * Finally, you should print out all quotations returned
	 * by the service.
	 * 
	 * @param args
	 * @throws NotBoundException
	 * @throws RemoteException
	 * @throws AccessException
	 */
	public static void main(String[] args) throws AccessException, RemoteException, NotBoundException {
		if(registry == null) { //empty registry
			System.out.println("registry empty");
            try {
                registry = LocateRegistry.getRegistry(1099);
            } catch (RemoteException e) {
                System.out.println(" registry Trouble:" + e);
            }
	    }
		BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
		System.out.println("Broker Service Found!");

		for(ClientInfo info : clients) {
			displayProfile(info);
			try{
				for(Quotation quotation : brokerService.getQuotations(info)) {
					displayQuotation(quotation);
				}
			}
			catch(Exception e){
				System.out.println("Displaying trouble: " + e);
			}
			System.out.println("\n");
		}
	}
	
	/**
	 * Display the client info nicely.
	 * 
	 * @param info
	 */
	public static void displayProfile(ClientInfo info) {
		System.out.println("|=================================================================================================================|");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println(
				"| Name: " + String.format("%1$-29s", info.name) + 
				" | Gender: " + String.format("%1$-27s", (info.gender==ClientInfo.MALE?"Male":"Female")) +
				" | Age: " + String.format("%1$-30s", info.age)+" |");
		System.out.println(
				"| Weight/Height: " + String.format("%1$-20s", info.weight+"kg/"+info.height+"m") + 
				" | Smoker: " + String.format("%1$-27s", info.smoker?"YES":"NO") +
				" | Medical Problems: " + String.format("%1$-17s", info.medicalIssues?"YES":"NO")+" |");
		System.out.println("|                                     |                                     |                                     |");
		System.out.println("|=================================================================================================================|");
	}

	/**
	 * Display a quotation nicely - note that the assumption is that the quotation will follow
	 * immediately after the profile (so the top of the quotation box is missing).
	 * 
	 * @param quotation
	 */
	public static void displayQuotation(Quotation quotation) {
		System.out.println(
				"| Company: " + String.format("%1$-26s", quotation.company) + 
				" | Reference: " + String.format("%1$-24s", quotation.reference) +
				" | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price))+" |");
		System.out.println("|=================================================================================================================|");
	}
	
	/**
	 * Test Data
	 */
	public static final ClientInfo[] clients = {
		new ClientInfo("Niki Collier", ClientInfo.FEMALE, 49, 1.5494, 80, false, false),
		new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 1.6, 100, true, true),
		new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 21, 1.78, 65, false, false),
		new ClientInfo("Rem Collier", ClientInfo.MALE, 49, 1.8, 120, false, true),
		new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 1.9, 75, true, false),
		new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 0.45, 1.6, false, false)
	};
}

