package service.message;

import service.core.Quotation;

public class QuotationMessage { // quoter -> broker
    private long token;
    private Quotation quotation;
    public QuotationMessage(long token, Quotation quotation) { 
        this.token = token;
        this.quotation = quotation;
    }

    public long getToken() { 
        return token;
    }

    public Quotation getQuotation() { 
        return quotation;
    }
}
