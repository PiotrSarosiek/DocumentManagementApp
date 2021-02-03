package Controllers;

import DB.VirtualDataBase;
import Model.Document.Document;
import Model.Document.InvoiceStrategy;
import Model.Document.ReceiptStrategy;
import Model.Order.IOrder;
import Model.Order.Order;
import Model.Order.OrderWithNomianalDiscount;
import Model.Order.OrderWithPercentageDiscount;
import Model.Product.Product;
import Model.TradingPartner.TradingPartner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MakeOrder {
    public javafx.scene.control.TableView TableView;
    public TextField NipTextField;
    public ComboBox ClientComboBox;
    public ComboBox AddProductComboBox;
    public TextField QuantityTextField;
    public ComboBox DocumentTypeComboBox;
    public Label PriceSum;
    public ComboBox AddDiscountComboBox;

    List<Product> products;
    List<Product> productsInBasket;
    List<TradingPartner> tradingPartners;
    Document document;
    IOrder tempOrder;
    IOrder order;

    public MakeOrder(){
        products= VirtualDataBase.getINSTANCE().getProducts();
        tradingPartners=VirtualDataBase.getINSTANCE().getPartners();
        productsInBasket=new LinkedList<Product>();
    }

    @FXML
    public void initialize() {
        makeTable(TableView);
        for (Product product : products) {
            AddProductComboBox.getItems().add(product.getName());
        }
        for(TradingPartner tradingPartner:tradingPartners){
            ClientComboBox.getItems().add(tradingPartner.getName());
        }
        AddDiscountComboBox.getItems().add("10%");
        AddDiscountComboBox.getItems().add("100$");
        DocumentTypeComboBox.getItems().add("Receipt");
        DocumentTypeComboBox.getItems().add("Invoice");
    }

    public boolean completeOrder(){
        String nip=null;
        TradingPartner tradingPartner;
        try{
            if(productsInBasket.isEmpty())
                throw new ClassNotFoundException();
            if(ClientComboBox.getSelectionModel().getSelectedItem()==null)
                throw new NullPointerException();
            else tradingPartner=new TradingPartner(((String)ClientComboBox.getSelectionModel().getSelectedItem()));
            setOrder(tradingPartner);
            if(DocumentTypeComboBox.getSelectionModel().getSelectedItem()!=null){
                if(DocumentTypeComboBox.getSelectionModel().getSelectedItem()=="Receipt")
                    document=new Document(order, new ReceiptStrategy(), LocalDate.now());
                else {
                    if(NipTextField.getText().isEmpty())
                        throw new NullPointerException();
                    else nip=NipTextField.getText();
                    document=new Document(order,new InvoiceStrategy(tradingPartner,nip),LocalDate.now());
                }
            }
            else throw new NullPointerException();
            return true;
        }
        catch (NullPointerException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("pls complete all required fields\n");
            alert.show();
            return false;
        }
        catch (ClassNotFoundException c){
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setContentText("order list is empty\n");
            alert2.show();
            return false;
        }
    }

    public void setOrder(TradingPartner tradingPartner){
        if(AddDiscountComboBox.getSelectionModel().getSelectedItem()==null){
            order=new Order(productsInBasket,tradingPartner);
        }else if(AddDiscountComboBox.getSelectionModel().getSelectedItem()=="10%"){
            order=new OrderWithPercentageDiscount(new Order(productsInBasket,tradingPartner));
        }else order=new OrderWithNomianalDiscount(new Order(productsInBasket,tradingPartner));
        PriceSum.setText(Double.toString(order.getSumPriceVat()));
    }

    public void AddProductButton(ActionEvent actionEvent) {
        try {
            String productName = (String)AddProductComboBox.getSelectionModel().getSelectedItem();
            Product product = products.stream().filter(m -> m.getName().equals(productName)).findAny().orElse(null);
            int value = Integer.valueOf(QuantityTextField.getText());
            if (productName == null) {
                throw new NullPointerException();
            }
            if (QuantityTextField.getText().isEmpty()||value==0) {
                throw new NumberFormatException();
            }
            if (product.getQuantity()<value || value < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("not enough in stock\nin stock: "+product.getQuantity());
                alert.show();
            }
            else {
                AddProductComboBox.getItems().remove(product.getName());
                product.setQuantity(value);
                productsInBasket.add(product);
                TableView.getItems().clear();
                TableView.getItems().addAll((Collection)productsInBasket);
                setOrder(new TradingPartner("temp"));
            }
        }
        catch (NullPointerException ex) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setContentText("pls select a product\n");
            alert2.show();
        }
        catch (NumberFormatException ex2) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setContentText("enter a number\n");
            alert2.show();
        }
    }

    public void SubmitButton(ActionEvent actionEvent) throws IOException {
        try{
            if(completeOrder()!=true) throw new NullPointerException();
            VirtualDataBase.getINSTANCE().addOrder(order);
            if(document.getDocumentStrategy().getDocumentType()=="Receipt")
                VirtualDataBase.getINSTANCE().addReceipt(document);
            else
                VirtualDataBase.getINSTANCE().addInvoice(document);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("ordered successfully\n");
            alert.show();
            Back(actionEvent);
        }
        catch (NullPointerException n){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("fill required fields\n");
            alert.show();
        }
    }

    public void makeTable(javafx.scene.control.TableView tableView){
        TableColumn<Product, String> name = (TableColumn<Product, String>)new TableColumn("Name");
        name.setCellValueFactory(new PropertyValueFactory("name"));
        TableColumn<Product, Integer> quantity = (TableColumn<Product, Integer>)new TableColumn("Quantity");
        quantity.setCellValueFactory(new PropertyValueFactory("quantity"));
        TableColumn<Product, Double> priceNoVat = (TableColumn<Product, Double>)new TableColumn("Price");
        priceNoVat.setCellValueFactory(new PropertyValueFactory("priceNoVat"));
        priceNoVat.setCellFactory(col -> new TableCell<Product, Double>() {
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText((String)null);
                }
                else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(2);
                    setText(nf.format(item));
                }
            }
        });
        TableColumn<Product, Double> priceVat = (TableColumn<Product, Double>)new TableColumn("Price with tax");
        priceVat.setCellValueFactory((Callback)new PropertyValueFactory("priceVat"));
        priceVat.setCellFactory(col -> new TableCell<Product, Double>() {
            protected void updateItem(Double item, boolean empty) {
                super.updateItem((Double) item, empty);
                if (empty) {
                    setText((String)null);
                }
                else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setMaximumFractionDigits(2);
                    setText(nf.format(item));
                }
            }
        });
        tableView.getColumns().addAll(new Object[] { name, quantity, priceNoVat, priceVat });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void AddDiscount(ActionEvent actionEvent) {
        setOrder(new TradingPartner("temp"));
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Parent parent= FXMLLoader.load(getClass().getResource("../View/Main.fxml"));
        Scene scene=new Scene(parent);
        Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
