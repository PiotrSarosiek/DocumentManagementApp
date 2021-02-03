package Controllers;

import DB.VirtualDataBase;
import Model.Order.IOrder;
import Model.Document.Document;
import Model.Report.DirectorReport;
import Model.Report.EXCELBuilder;
import Model.Report.PDFBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


public class Documents {
    public TableView tableView;
    public List<IOrder> orders;

    @FXML
    public void initialize() {

        List<Document> documents=VirtualDataBase.getINSTANCE().getDocuments();

        TableColumn<Document, LocalDate> date=(TableColumn<Document, LocalDate>)new TableColumn("Date");
        date.setCellValueFactory(new PropertyValueFactory("date"));

        TableColumn<Document, String > documentType=(TableColumn<Document, String >)new TableColumn("Document type");
        documentType.setCellValueFactory(new PropertyValueFactory("documentType"));

        TableColumn<Document, String > partnerName=(TableColumn<Document, String >)new TableColumn("Partner name");
        partnerName.setCellValueFactory(new PropertyValueFactory("partnerName"));

        TableColumn<Document, String > nip=(TableColumn<Document, String >)new TableColumn("Nip");
        nip.setCellValueFactory(new PropertyValueFactory("nip"));

        tableView.getColumns().addAll(new Object[] { date, documentType,partnerName,nip });
        tableView.setColumnResizePolicy(tableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getItems().addAll((Collection)documents);
    }

    public void ViewMore(ActionEvent actionEvent) {
        try {
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/viewMore.fxml"));
            Parent tableViewParent=loader.load();

            Scene tableViewStage=new Scene(tableViewParent);
            ViewMore controller=loader.getController();
            controller.initialize((Document) tableView.getSelectionModel().getSelectedItem());

            Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(tableViewStage);
            window.show();
        }
        catch (IOException ex) {
            System.out.println("Cannot load view\n");
            ex.printStackTrace();
        }
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Parent parent= FXMLLoader.load(getClass().getResource("../View/Main.fxml"));
        Scene scene=new Scene(parent);
        Stage window=(Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void XLSXButton(ActionEvent actionEvent) {
        DirectorReport report = new DirectorReport(new EXCELBuilder());
        report.construct();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("saved successfully\n");
        alert.show();
    }

    public void PDFButton(ActionEvent actionEvent) {
        DirectorReport report = new DirectorReport(new PDFBuilder());
        report.construct();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("saved successfully\n");
        alert.show();
    }
}
