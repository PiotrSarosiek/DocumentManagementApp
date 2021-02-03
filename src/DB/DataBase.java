package DB;

import Model.Document.Document;
import Model.Document.DocumentStrategy;
import Model.Document.InvoiceStrategy;
import Model.Document.ReceiptStrategy;
import Model.Order.IOrder;
import Model.Order.Order;
import Model.Product.Product;
import Model.TradingPartner.TradingPartner;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DataBase implements Data {
    private Connection connection;

    public DataBase(){
        String dbName="DocumentDataBase";
        String userName="root";
        String password="";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getProducts() {
        List<Product> products=new LinkedList<Product>();
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM PRODUCTS");
            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                int quantity = resultSet.getInt("QUANTITY");
                Double priceNoVat = resultSet.getDouble("PRICE");
                Double vat = resultSet.getDouble("VAT");
                Product product = new Product(name, quantity, priceNoVat, vat);
                products.add(product);
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByOrderId(int orderId) {
        List<Product> products=new LinkedList<Product>();
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM ORDER_PRODUCTS WHERE ORDERID="+orderId+"");
            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                int quantity = resultSet.getInt("QUANTITY");
                Double priceNoVat = resultSet.getDouble("PRICE");
                Double vat = resultSet.getDouble("VAT");
                Product product = new Product(name, quantity, priceNoVat, vat);
                products.add(product);
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void addProduct(Product product) {
        try{
            Statement statement=connection.createStatement();
            String sql="INSERT INTO PRODUCTS VALUES('"+product.getName()+"', "+product.getQuantity()+","+product.getPriceNoVat()+","+product.getVat()+")";
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public List<TradingPartner> getPartners() {
        List<TradingPartner> partners=new LinkedList<TradingPartner>();
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM PARTNERS");
            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                partners.add(new TradingPartner(name));
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return partners;
    }

    @Override
    public void addPartner(TradingPartner tradingPartner) {
        try{
            Statement statement=connection.createStatement();
            String sql="INSERT INTO PARTNERS VALUES(NULL,'"+tradingPartner.getName()+"')";
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public TradingPartner getPartnerById(int id){
        TradingPartner tradingPartner=null;
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM PARTNERS WHERE PARTNERID="+id+"");
            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                tradingPartner=new TradingPartner(name);
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return tradingPartner;
    }

    public int getPartnerByName(String name){
        int id=0;
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT PARTNERID FROM PARTNERS WHERE NAME='"+name+"'");
            while (resultSet.next()) {
                id = resultSet.getInt("PARTNERID");
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void addOrder(IOrder iOrder){
        try{
            Statement statement=connection.createStatement();
            String sql="INSERT INTO ORDERS VALUES(NULL,"+getPartnerByName(iOrder.getPartnerName())+","+iOrder.getSumPriceNoVat()+","+iOrder.getSumPriceVat()+")";
            statement.executeUpdate(sql);
            ResultSet resultSet=statement.executeQuery("SELECT MAX(ORDERID) FROM ORDERS");
            while (resultSet.next()) {
                int id = resultSet.getInt("MAX(ORDERID)");
                iOrder.setOrderID(id);
                for(Product product:iOrder.getProducts()){
                    addOrderProduct(product,id);
                    reduceQuantity(product.getName(),product.getQuantity());
                }
            }
            statement.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public List<IOrder> getOrders() {
        List<IOrder> orders = new LinkedList<IOrder>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM ORDERS");
            while (resultSet.next()){
                int orderId=resultSet.getInt("ORDERID");
                int partnerId=resultSet.getInt("PARTNERID");
                TradingPartner tradingPartner=getPartnerById(partnerId);
                List<Product> products=getProductsByOrderId(orderId);
                Order order=new Order(products,tradingPartner);
                order.setOrderID(orderId);
                order.setSumPriceNoVatAfterDiscount(resultSet.getDouble("PRICENOVAT"));
                order.setSumPriceVatAfterDiscount(resultSet.getDouble("PRICEVAT"));
                orders.add(order);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return orders;
    }

    public Order getOrderById(int id){
        Order order=null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM ORDERS WHERE ORDERID="+id+"");
            while (resultSet.next()){
                int orderId=resultSet.getInt("ORDERID");
                int partnerId=resultSet.getInt("PARTNERID");
                TradingPartner tradingPartner=getPartnerById(partnerId);
                List<Product> products=getProductsByOrderId(orderId);
                order=new Order(products,tradingPartner);
                order.setOrderID(orderId);
                order.setSumPriceNoVatAfterDiscount(resultSet.getDouble("PRICENOVAT"));
                order.setSumPriceVatAfterDiscount(resultSet.getDouble("PRICEVAT"));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return order;
    }

    public void addOrderProduct(Product product, int orderId){
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO ORDER_PRODUCTS VALUES('"+product.getName()+"',"+product.getQuantity()+","+product.getPriceNoVat()+","+product.getVat()+","+orderId+")";
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addQuantity(String name, int temp){
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE PRODUCTS SET QUANTITY="+temp+" WHERE PRODUCTS.NAME='"+name+"'";
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void reduceQuantity(String name, int temp){
        int quantity=getProducts().stream().filter(n -> n.getName().equals(name)).findAny().orElse(null).getQuantity()-temp;
        try{
            Statement statement=connection.createStatement();
            statement.executeUpdate("UPDATE PRODUCTS SET QUANTITY="+quantity+" WHERE PRODUCTS.NAME='"+name+"'");
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addReceipt(Document document){
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO RECEIPTS VALUES(NULL,"+document.getOrder().getOrderID()+",'"+document.getDate().toString()+"')";
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addInvoice(Document document) {
        try{
            addReceipt(document);
            Statement statement=connection.createStatement();
            String sql="INSERT INTO INVOICES VALUES("+getDocumentID(document)+",'"+document.getDocumentStrategy().getPartnerName()+"','"+document.getDocumentStrategy().getNip()+"')";
            statement.executeUpdate(sql);
            statement.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public List<Document> getDocuments() {
        List<Document> documents=new LinkedList<>();
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT * FROM RECEIPTS WHERE DOCUMENTID!=ALL(SELECT DOCUMENTID FROM INVOICES) ");
            while (resultSet.next()) {
                Document document=new Document(getOrderById(resultSet.getInt("ORDERID")),new ReceiptStrategy(),LocalDate.parse(resultSet.getString("DATE")));
                documents.add(document);
            }
            resultSet=statement.executeQuery("SELECT ORDERID,PARTNERNAME,NIP,DATE FROM INVOICES LEFT JOIN RECEIPTS ON INVOICES.DOCUMENTID = RECEIPTS.DOCUMENTID");
            while (resultSet.next()) {
                Document document=new Document(getOrderById(resultSet.getInt("ORDERID")),new InvoiceStrategy(new TradingPartner(resultSet.getString("PARTNERNAME")),resultSet.getString("NIP")),LocalDate.parse(resultSet.getString("DATE")));
                documents.add(document);
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return documents;
    }

    public int getDocumentID(Document document){
        int id=0;
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery("SELECT DOCUMENTID FROM RECEIPTS WHERE ORDERID="+document.getOrder().getOrderID()+"");
            while (resultSet.next()) {
                id = resultSet.getInt("DOCUMENTID");
            }
            statement.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void disconnect() {
        try{
            connection.close();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
