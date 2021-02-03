package DB;

import Model.Document.Document;
import Model.Document.DocumentStrategy;
import Model.Order.IOrder;
import Model.Order.Order;
import Model.Product.Product;
import Model.TradingPartner.TradingPartner;

import java.util.List;

public interface Data {
    List<Product> getProducts();
    void addProduct(Product product);
    List<TradingPartner> getPartners();
    void addPartner(TradingPartner tradingPartner);
    void disconnect();
    void addQuantity(String name, int temp);
    void reduceQuantity(String name, int temp);
    void addOrder(IOrder iOrder);
    List<IOrder> getOrders();
    void addReceipt(Document document);
    void addInvoice(Document document);
    List<Document> getDocuments();
    Order getOrderById(int id);
}
