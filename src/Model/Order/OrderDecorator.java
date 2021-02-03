package Model.Order;

import Model.Product.Product;
import Model.TradingPartner.TradingPartner;

import java.util.List;

public class OrderDecorator implements IOrder{

    protected IOrder iOrder;
    public OrderDecorator(IOrder iOrder){
        this.iOrder=iOrder;
    }

    @Override
    public String getPartnerName() {
        return iOrder.getPartnerName();
    }

    @Override
    public void setPartner(TradingPartner tradingPartner) {
        iOrder.setPartner(tradingPartner);
    }

    @Override
    public List<Product> getProducts() {
        return iOrder.getProducts();
    }

    @Override
    public void setProducts(List<Product> products) {
        iOrder.setProducts(products);
    }

    @Override
    public TradingPartner getPartner() {
        return iOrder.getPartner();
    }

    @Override
    public double getSumPriceVat() {
        return iOrder.getSumPriceVat();
    }

    @Override
    public double getSumPriceNoVat() {
        return iOrder.getSumPriceNoVat();
    }

    @Override
    public int getOrderID() {
        return iOrder.getOrderID();
    }

    @Override
    public void setOrderID(int id) {
        iOrder.setOrderID(id);
    }

    @Override
    public double getSumPriceVatAfterDiscount(){ return iOrder.getSumPriceVatAfterDiscount(); }

    @Override
    public double getSumPriceNoVatAfterDiscount(){ return iOrder.getSumPriceNoVatAfterDiscount(); }
}
