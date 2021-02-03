package Model.Order;

import Model.Product.Product;
import Model.TradingPartner.TradingPartner;
import java.util.List;

public interface IOrder {
    String getPartnerName();

    void setPartner(TradingPartner tradingPartner);

    List<Product> getProducts();

    void setProducts(List<Product> products);

    TradingPartner getPartner();

    double getSumPriceVat();

    double getSumPriceNoVat();

    int getOrderID();

    void setOrderID(int id);

    double getSumPriceVatAfterDiscount();

    double getSumPriceNoVatAfterDiscount();
}
