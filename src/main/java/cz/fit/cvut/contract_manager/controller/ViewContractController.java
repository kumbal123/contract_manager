package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ViewContractController extends Controller {

    public TextField nameField;
    public TextField genderField;
    public TextField addressField;
    public TextField cityField;
    public TextField personalNumberField;
    public TextField cardIdNumberField;
    public TextField meuField;
    public TextField nationalityField;
    public TextField dateOfBirthField;
    public TextField creationDateField;
    public TextField lendPriceField;
    public TextField expireDateField;
    public TextField contractIdField;
    public TextField itemInfoField;
    public TextField itemSpecificationField;
    public TextField totalPriceField;

    public TableView<History> tvHistory;
    public TableColumn<History, String> colFromDate;
    public TableColumn<History, String> colToDate;
    public TableColumn<History, String> colStartPrice;
    public TableColumn<History, String> colInterest;
    public TableColumn<History, String> colTotalPrice;
    public Label labelTotalPrice;

    public BorderPane mainPane;
    public Pane rightPane;

    private Contract contract;

    public void initContractData(final Contract src) {
        contract = src;

        nameField.setText(contract.getName());
        genderField.setText(contract.getGender());
        addressField.setText(contract.getAddress());
        cityField.setText(contract.getCity());
        personalNumberField.setText(contract.getPersonalNumber());
        cardIdNumberField.setText(contract.getIdNumber());
        meuField.setText(contract.getMeu());
        nationalityField.setText(contract.getNationality());
        dateOfBirthField.setText(getStringFromDate(contract.getDateOfBirth()));
        creationDateField.setText(getStringFromDate(contract.getCreationDate()));
        lendPriceField.setText(contract.getLendPrice().toString());
        expireDateField.setText(getStringFromDate(contract.getExpireDateOrig()));
        contractIdField.setText(contract.getContractId());
        itemInfoField.setText(contract.getItemInfo());
        itemSpecificationField.setText(contract.getItemSpecification());
        totalPriceField.setText(contract.getTotalPriceOrig().toString());

        if(contract.getNumberOfProlongs() > 0) {
            rightPane.setVisible(true);

            ObservableList<History> historyList = FXCollections.observableArrayList(contract.getHistory());

            historyList.sort(Comparator.comparing(History::getFromDate));

            colStartPrice.setCellValueFactory(new PropertyValueFactory<History, String>("startPrice"));
            colInterest.setCellValueFactory(new PropertyValueFactory<History, String>("interest"));
            colTotalPrice.setCellValueFactory(new PropertyValueFactory<History, String>("totalPrice"));

            colFromDate.setCellValueFactory(
                cellData -> getStringPropertyFromDate(cellData.getValue().getFromDate())
            );

            colToDate.setCellValueFactory(
                cellData -> getStringPropertyFromDate(cellData.getValue().getToDate())
            );

            tvHistory.setItems(historyList);

            labelTotalPrice.setText(historyList.get(contract.getNumberOfProlongs() - 1).getTotalPrice().toString());
        }
    }

    @FXML
    public void takeOut(final MouseEvent event) {
        //TODO
    }

    @FXML
    public void fulfill(final MouseEvent event) {
        //TODO
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
