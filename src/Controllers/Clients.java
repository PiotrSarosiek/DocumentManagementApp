package Controllers;

import DB.VirtualDataBase;
import Model.TradingPartner.TradingPartner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class Clients {

    public TableView tableView;
    public TextField ClientName;
    List<TradingPartner> tradingPartners;

    public Clients(){
        tradingPartners=VirtualDataBase.getINSTANCE().getPartners();
    }

    @FXML
    public void initialize() {
        TableColumn<TradingPartner, String> name = (TableColumn<TradingPartner, String>)new TableColumn("Trading partner name");
        name.setCellValueFactory(new PropertyValueFactory("name"));
        tableView.getColumns().addAll(new Object[] {name});
        tableView.setColumnResizePolicy(tableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getItems().addAll((Collection)tradingPartners);
    }

    public void Add(ActionEvent actionEvent) {
        try{
            if (ClientName.getText().isEmpty()) {
                throw new NumberFormatException();
            }
            VirtualDataBase.getINSTANCE().addPartner(new TradingPartner(ClientName.getText()));
            tableView.getItems().clear();
            tableView.getItems().addAll((Collection)VirtualDataBase.getINSTANCE().getPartners());
            ClientName.clear();
        }
        catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("fill name field");
            alert.show();
        }
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Parent parent= FXMLLoader.load(getClass().getResource("../View/Main.fxml"));
        Scene scene=new Scene(parent);
        Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
