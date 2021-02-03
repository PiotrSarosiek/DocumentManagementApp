package Controllers;

import DB.VirtualDataBase;
import Model.Product.Product;
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
import java.util.Collection;
import java.util.List;

public class Products {

    public TextField quantityField;

    @FXML
    private TableView tableView;

    @FXML
    public void initialize() {

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
        List<Product> products = VirtualDataBase.getINSTANCE().getProducts();
        tableView.getItems().addAll((Collection)products);
    }

    public void ReduceProductQuantity(ActionEvent actionEvent) {
        try {
            Product product = (Product)tableView.getSelectionModel().getSelectedItem();
            if (product == null) {
                throw new ClassCastException();
            }
            if (quantityField.getText().isEmpty()) {
                throw new NumberFormatException();
            }
            int value = Integer.valueOf(quantityField.getText());
            boolean reduced = VirtualDataBase.getINSTANCE().isEnoughInStock(product, value);
            if (!reduced || value < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("not enough in stock\n");
                alert.show();
            }
            else {
                VirtualDataBase.getINSTANCE().reduceQuantity(product.getName(),value);
                tableView.getItems().clear();
                tableView.getItems().addAll((Collection)VirtualDataBase.getINSTANCE().getProducts());
                tableView.refresh();
            }

        }
        catch (ClassCastException ex) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setContentText("pls select a product\n");
            alert2.show();
        }
        catch (NumberFormatException ex2) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setContentText("sth went wrong");
            alert2.show();
        }
    }

    public void AddProduct(ActionEvent actionEvent) {
        try {
            Parent root = (Parent) FXMLLoader.load(getClass().getClassLoader().getResource("View/AddProduct.fxml"));
            Stage addProductStage = new Stage();
            addProductStage.setOnHiding(n->{
                tableView.getItems().clear();
                tableView.getItems().addAll((Collection)VirtualDataBase.getINSTANCE().getProducts());
            });
            addProductStage.setTitle("Add product");
            addProductStage.setScene(new Scene(root, 298, 207));
            addProductStage.show();
        }
        catch (IOException ex) {
            System.out.println("Cannot load view\n");
            ex.printStackTrace();
        }
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Parent parent=FXMLLoader.load(getClass().getResource("../View/Main.fxml"));
        Scene scene=new Scene(parent);
        Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
