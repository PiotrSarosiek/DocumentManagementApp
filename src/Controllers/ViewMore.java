package Controllers;

import DB.VirtualDataBase;
import Model.Document.Document;
import Model.Order.Order;
import Model.Product.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;

public class ViewMore {
    public TableView tableView;
    public TextField TotalPriceVatTextField;
    public TextField DiscountTextField;
    private Document document;

    @FXML
    public void initialize(Document document) {
        this.document=document;

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
        tableView.setColumnResizePolicy(tableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getItems().addAll((Collection)document.getOrder().getProducts());

        Order order=VirtualDataBase.getINSTANCE().getOrderById(document.getOrder().getOrderID());

        double temp=0.0;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        try {
            temp= DecimalFormat.getNumberInstance().parse(nf.format(order.getSumPriceVat()-order.getSumPriceVatAfterDiscount())).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TotalPriceVatTextField.setText(Double.toString(order.getSumPriceVatAfterDiscount()));
        DiscountTextField.setText(Double.toString(temp));
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Parent parent= FXMLLoader.load(getClass().getResource("../View/Documents.fxml"));
        Scene scene=new Scene(parent);
        Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
