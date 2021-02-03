package Model.Order;

import Model.Product.Product;
import Model.TradingPartner.TradingPartner;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class Order implements IOrder{

    int OrderID;
    protected List<Product> products;
    private TradingPartner tradingPartner;
    private double sumPriceNoVatAfterDiscount;
    private double sumPriceVatAfterDiscount;

    public Order(List<Product> products, TradingPartner tradingPartner){
        this.products=products;
        this.tradingPartner = tradingPartner;
    }
    @Override
    public String getPartnerName() {
        return tradingPartner.getName();
    }

    @Override
    public void setPartner(TradingPartner tradingPartner) {
        this.tradingPartner = tradingPartner;
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public void setProducts(List<Product> products) {
        this.products=products;
    }

    @Override
    public TradingPartner getPartner() {
        return tradingPartner;
    }

    @Override
    public double getSumPriceVat() {
        double sumPriceVat=0.0;
        for(Product p:products){
            sumPriceVat+=p.getPriceVat()* p.getQuantity();
        }
        return sumPriceVat;

    }

    @Override
    public double getSumPriceNoVat() {
        double sumPriceNoVat=0.0;
        for(Product p:products){
            sumPriceNoVat+=p.getPriceNoVat()*p.getQuantity();
        }
        return sumPriceNoVat;
    }

    @Override
    public int getOrderID() {
        return OrderID;
    }

    @Override
    public void setOrderID(int id) {
        this.OrderID=id;
    }
    @Override
    public double getSumPriceNoVatAfterDiscount(){
        return sumPriceNoVatAfterDiscount;
    }
    @Override
    public double getSumPriceVatAfterDiscount(){
        return sumPriceVatAfterDiscount;
    }

    public void setSumPriceNoVatAfterDiscount(double priceNoVatAfterDiscount) {
        this.sumPriceNoVatAfterDiscount=priceNoVatAfterDiscount;
    }

    public void setSumPriceVatAfterDiscount(double priceVatAfterDiscount){
        this.sumPriceVatAfterDiscount=priceVatAfterDiscount;
    }

}
