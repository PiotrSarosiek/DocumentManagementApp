package DB;

import Model.Document.Document;
import Model.Document.DocumentStrategy;
import Model.Order.IOrder;
import Model.Order.Order;
import Model.Product.Product;
import Model.TradingPartner.TradingPartner;


import java.util.LinkedList;
import java.util.List;

public class VirtualDataBase implements Data{

    private List<Product> products;
    private DataBase dataBase;
    private List<TradingPartner> partners;
    private List<IOrder> orders;
    private List<Document> documents;
    private static volatile VirtualDataBase INSTANCE;

    public static VirtualDataBase getINSTANCE(){
        if(INSTANCE==null) {
            INSTANCE=new VirtualDataBase();
        }
        return INSTANCE;
    }

    private VirtualDataBase(){
        dataBase=new DataBase();
    }

    @Override
    public List<Product> getProducts() {
        if(products==null){
            products=dataBase.getProducts();
        }
        return products;
    }

    @Override
    public void addProduct(Product product) {
        if(products==null){
            products=new LinkedList<Product>();
        }
        products.add(product);
        dataBase.addProduct(product);
    }

    @Override
    public List<TradingPartner> getPartners() {
        if (partners == null) {
            partners = dataBase.getPartners();
        }
        return partners;
    }

    @Override
    public void addPartner(TradingPartner tradingPartner) {
        if (partners == null) {
            partners = new LinkedList<TradingPartner>();
        }
        partners.add(tradingPartner);
        this.dataBase.addPartner(tradingPartner);
    }

    @Override
    public void disconnect() {
        dataBase.disconnect();
    }

    @Override
    public void addQuantity(String name, int temp) {
        if(products==null)
            products=dataBase.getProducts();
        Product product=products.stream().filter(n -> n.getName().equals(name)).findAny().orElse(null);
        product.setQuantity(product.getQuantity()+temp);
        dataBase.addQuantity(name, product.getQuantity());
    }

    @Override
    public void reduceQuantity(String name, int temp) {
        if(products==null)
            products=dataBase.getProducts();
        Product product=products.stream().filter(n -> n.getName().equals(name)).findAny().orElse(null);
        product.setQuantity(product.getQuantity()-temp);
        dataBase.reduceQuantity(name, temp);
    }

    @Override
    public void addOrder(IOrder iOrder) {
        if(orders==null)
            orders=new LinkedList<IOrder>();
        orders.add(iOrder);
        dataBase.addOrder(iOrder);
        orders=dataBase.getOrders();
        documents=dataBase.getDocuments();
    }

    @Override
    public List<IOrder> getOrders() {
        if(orders==null)
            orders=dataBase.getOrders();
        return orders;
    }

    @Override
    public void addReceipt(Document document) {
        if(documents==null)
            documents=new LinkedList<Document>();
        documents.add(document);
        dataBase.addReceipt(document);
    }

    @Override
    public void addInvoice(Document document) {
        if(documents==null)
            documents=new LinkedList<Document>();
        documents.add(document);
        dataBase.addInvoice(document);
    }



    @Override
    public List<Document> getDocuments() {
        if(documents==null)
            documents=dataBase.getDocuments();
        return documents;
    }

    @Override
    public Order getOrderById(int id) {
        if(orders==null)
            orders=dataBase.getOrders();
        IOrder order = orders.stream().filter(m -> m.getOrderID()==id).findAny().orElse(null);
        Order tempOrder=new Order(order.getProducts(),new TradingPartner(order.getPartnerName()));
        tempOrder.setOrderID(order.getOrderID());
        tempOrder.setSumPriceNoVatAfterDiscount(order.getSumPriceNoVatAfterDiscount());
        tempOrder.setSumPriceVatAfterDiscount(order.getSumPriceVatAfterDiscount());
        return tempOrder;
    }

    public boolean isEnoughInStock(Product product, int value) {
        Product tempProduct = products.stream().filter(m -> m.getName().equals(product.getName())).findAny().orElse(null);
        int quantity = tempProduct.getQuantity() - value;
        return quantity >= 0;
    }
}
