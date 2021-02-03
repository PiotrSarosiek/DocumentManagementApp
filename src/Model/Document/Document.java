package Model.Document;

import Model.Order.IOrder;

import java.time.LocalDate;

public class Document {
    private IOrder iOrder;
    private LocalDate date;
    private DocumentStrategy documentStrategy;
    private String documentType;
    private String partnerName;
    private String Nip;

    public Document(IOrder iOrder,DocumentStrategy documentStrategy,LocalDate date){
        this.iOrder=iOrder;
        this.documentStrategy=documentStrategy;
        this.date=date;
        documentType=getDocumentType();
        partnerName=getPartnerName();
        Nip=getNip();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public IOrder getOrder() {
        return iOrder;
    }

    public void setOrder(IOrder iOrder) {
        this.iOrder = iOrder;
    }

    public DocumentStrategy getDocumentStrategy() {
        return documentStrategy;
    }

    public void setDocumentStrategy(DocumentStrategy documentStrategy) {
        this.documentStrategy = documentStrategy;
    }

    public String getPartnerName(){
        return documentStrategy.getPartnerName();
    }

    public String getDocumentType(){
        return documentStrategy.getDocumentType();
    }

    public String getNip(){
        return documentStrategy.getNip();
    }
}
