package Controllers;


import DB.VirtualDataBase;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @FXML
    public void initialize() {}
    public void GoToProducts(ActionEvent actionEvent) throws Exception{
        redirectTo(actionEvent,"../View/Products.fxml");
    }

    public void GoToOrders(ActionEvent actionEvent) throws IOException {
        redirectTo(actionEvent,"../View/makeOrder.fxml");
    }

    public void GoToClients(ActionEvent actionEvent) throws Exception{
        redirectTo(actionEvent,"../View/Clients.fxml");
    }

    public void GoToDocuments(ActionEvent actionEvent) throws Exception {
        redirectTo(actionEvent,"../View/Documents.fxml");
    }

    public void redirectTo(ActionEvent actionEvent,String url) throws IOException {
        Parent parent=FXMLLoader.load(getClass().getResource(url));
        Scene scene=new Scene(parent);
        Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/Main.fxml"));
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("DocumentApp");
        primaryStage.setOnCloseRequest(m -> VirtualDataBase.getINSTANCE().disconnect());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
