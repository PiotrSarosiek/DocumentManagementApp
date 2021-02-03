package Model.Document;

import Model.TradingPartner.TradingPartner;

public class InvoiceStrategy implements DocumentStrategy{

    private TradingPartner tradingPartner;
    private String nip;

    public InvoiceStrategy(TradingPartner tradingPartner, String nip){
        this.tradingPartner = tradingPartner;
        this.nip=nip;
    }

    @Override
    public String getPartnerName() {
        return tradingPartner.getName();
    }

    @Override
    public String getDocumentType() {
        return "Invoice";
    }

    @Override
    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }
}
