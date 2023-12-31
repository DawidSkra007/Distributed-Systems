package service.core;

import java.rmi.RemoteException;

//import service.registry.Service;

/**
 * Interface to define the behaviour of a quotation service.
 * 
 * @author Rem
 *
 */
public interface QuotationService extends java.rmi.Remote {
	public Quotation generateQuotation(ClientInfo info) throws RemoteException;
}
