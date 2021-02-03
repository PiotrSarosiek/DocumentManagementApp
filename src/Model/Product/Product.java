package Model.Product;

public class Product {
    private String name;
    private int quantity;
    private double priceVat;
    private double priceNoVat;
    private double vat;

    public Product(String name,int quantity,double priceNoVat, double vat)
    {
        this.name=name;
        this.quantity=quantity;
        this.priceNoVat=priceNoVat;
        this.vat=vat;
        priceVat=priceNoVat*(1+vat);
    }

    public double getPriceNoVat() {
        return priceNoVat;
    }

    public double getPriceVat() {
        return priceVat;
    }

    public double getVat() {
        return vat;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPriceNoVat(double priceNoVat) {
        this.priceNoVat = priceNoVat;
    }

    public void setPriceVat(double priceVat) {
        this.priceVat = priceVat;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public boolean reduceQuantity(int i){
        if(quantity>i) {
            quantity-=i;
            return true;
        }
        return false;
    }
}
