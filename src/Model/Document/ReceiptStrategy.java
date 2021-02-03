package Model.Document;

public class ReceiptStrategy implements DocumentStrategy{

    @Override
    public String getPartnerName() {
        return "Client";
    }

    @Override
    public String getDocumentType() {
        return "Receipt";
    }

    @Override
    public String getNip() {
        return "-";
    }
}
