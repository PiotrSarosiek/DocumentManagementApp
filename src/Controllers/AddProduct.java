package Controllers;

import DB.VirtualDataBase;
import Model.Product.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class AddProduct {

    @FXML
    private Button existProduct,newProduct,addButton;
    @FXML
    private Label chooseTypeLabel,productNameLabel,productPriceLabel,taxLabel,chooseProductLabel,productQuantity1Label,productQuantity2Label;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField productPriceTextField;
    @FXML
    private ComboBox taxComboBox,productsComboBox;
    @FXML
    private TextField productQuantity1TextField,productQuantity2TextField;
    private int way;
    List<Product> products;

    public AddProduct() {
        way = 0;
        products = VirtualDataBase.getINSTANCE().getProducts();
    }

    @FXML
    public void addAction() {
        try {
            if (way == 1) {
                String productName = productNameTextField.getText();
                int productAmount = Integer.parseInt(productQuantity1TextField.getText());
                double productPrice = Double.parseDouble(productPriceTextField.getText());
                String tax = (String)taxComboBox.getSelectionModel().getSelectedItem();
                if (!isFieldsCorrectForNewProduct(productName, productAmount, productPrice, tax)) {
                    throw new NumberFormatException();
                }
                String s = tax;
                Product product = null;
                switch (s) {
                    case "5%": {
                        product = new Product(productName, productAmount, productPrice, 0.05);
                        break;
                    }
                    case "8%": {
                        product = new Product(productName, productAmount, productPrice, 0.08);
                        break;
                    }
                    case "23%": {
                        product = new Product(productName, productAmount, productPrice, 0.23);
                        break;
                    }
                }
                VirtualDataBase.getINSTANCE().addProduct(product);
                Stage stage = (Stage)productNameTextField.getScene().getWindow();
                stage.close();
            }
            else {
                String productName = (String)productsComboBox.getSelectionModel().getSelectedItem();
                int productAmount = Integer.parseInt(productQuantity2TextField.getText());
                if (!isFieldsCorrectForExistProduct(productName, productAmount)) {
                    throw new NumberFormatException();
                }
                VirtualDataBase.getINSTANCE().addQuantity(productName, productAmount);
                Stage stage2 = (Stage)productNameTextField.getScene().getWindow();
                stage2.close();
            }
        }
        catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Sth went wrong");
            alert.show();
        }
    }

    public boolean isProductExistInDB(String productName) {
        return products.stream().anyMatch(m -> m.getName().equals(productName));
    }

    public boolean isFieldsCorrectForNewProduct(String productName, int productAmount, double productPrice, String tax) {
        return !productName.isEmpty() || productAmount > 0 || productPrice > 0.0 || !tax.isEmpty() || !isProductExistInDB(productName);
    }

    public boolean isFieldsCorrectForExistProduct(String productName, double productAmount) {
        return productName != null || productAmount > 0.0;
    }

    public void VisibilityForAddExistingProduct() {
        existProduct.setVisible(false);
        newProduct.setVisible(false);
        chooseTypeLabel.setVisible(false);
        productQuantity2TextField.setVisible(true);
        productQuantity2Label.setVisible(true);
        addButton.setVisible(true);
        chooseProductLabel.setVisible(true);
        productsComboBox.setVisible(true);
    }

    public void VisibilityForNewProduct() {
        existProduct.setVisible(false);
        newProduct.setVisible(false);
        chooseTypeLabel.setVisible(false);
        productNameLabel.setVisible(true);
        productPriceLabel.setVisible(true);
        taxLabel.setVisible(true);
        productNameTextField.setVisible(true);
        productPriceTextField.setVisible(true);
        taxComboBox.setVisible(true);
        productQuantity1TextField.setVisible(true);
        productQuantity1Label.setVisible(true);
        addButton.setVisible(true);
    }

    @FXML
    public void newProductAction() {
        way = 1;
        VisibilityForNewProduct();
        taxComboBox.getItems().addAll(new Object[] { "5%", "8%", "23%" });
    }

    @FXML
    public void existingProductAction() {
        way = 2;
        VisibilityForAddExistingProduct();
        for (Product product : products) {
            productsComboBox.getItems().add(product.getName());
        }
    }
}
