package service.broker;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import service.core.BrokerService;
import service.core.ClientInfo;
import service.core.Quotation;
import service.core.QuotationService;

/**
 * Implementation of the broker service that uses the Service Registry.
 * 
 * @author Rem
 * @param <Main>
 *
 */
@WebService(name="broker", targetNamespace="http://core.service/", serviceName="broker")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public class LocalBrokerService implements BrokerService {
	LinkedList<String> urls = new LinkedList<>();

	public LocalBrokerService(LinkedList<String> urls) {
		this.urls = urls;
	}

	public LocalBrokerService() {
	}

	@WebMethod
	public LinkedList<Quotation> getQuotations(ClientInfo info) {
		LinkedList<Quotation> quotations = new LinkedList<Quotation>();
		String url;
		String localPart = null;
		String localPort = null;

		for(int i = 0; i < urls.size(); i++) {
			url = urls.get(i);

			URL wsdlUrl = null;
			try {
				wsdlUrl = new URL(url + "?wsdl"); //needed to display wsdl page
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} 
			if(url.contains("9001")) {
				localPart = "auldfellas";
				localPort = "auldfellasPort";
			} else if (url.contains("9002")) {
				localPart = "dodgygeezers";
				localPort = "dodgygeezersPort";
			} else if (url.contains("9003")) {
				localPart = "girlsallowed";
				localPort = "girlsallowedPort";
			} else {
				System.out.println("Invalid Port number."); //invalid port number
				System.exit(0);
			}
			QName serviceName = new QName("http://core.service/", localPart);
			Service service = Service.create(wsdlUrl, serviceName);
			QName portName = new QName("http://core.service/", localPort);
			QuotationService quotationService = service.getPort(portName, QuotationService.class); 
			Quotation quotation = quotationService.generateQuotation(info);
			quotations.add(quotation);
			// TESTING
			// System.out.println("Company: " + quotation.company);
			// System.out.println("Quotation: " + quotation.price); 
		}

		return quotations;
	}
}
