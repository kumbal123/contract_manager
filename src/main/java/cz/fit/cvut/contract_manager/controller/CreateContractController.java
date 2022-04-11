package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.Notification.Notification;
import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.entity.History;
import cz.fit.cvut.contract_manager.entity.Price;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import cz.fit.cvut.contract_manager.service.WebService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CreateContractController extends Controller {

    public TextField nameField;
    public TextField genderField;
    public TextField birthPlaceField;
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

    public TableView<Price> tvPrices;
    public TableColumn<Price, String> colName;
    public TableColumn<Price, String> colPrice;

    public Label labelAvgPrice;

    public Button updateButton;
    public BorderPane mainPane;
    public Pane rightPane;
    public Pane contractDataPane;

    private Customer customer;
    private Contract contract;
    private ObservableList<Price> priceList;

    private final ContractRepositoryService contractService = ContractRepositoryService.getInstance();
    private final CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();
    private final WebService webService = WebService.getInstance();

    private Boolean setErrColorCustomer(final String name, final String personalNumber, final String dateOfBirth) {
        nameField.setStyle(name.isBlank() ? "-fx-background-color: #ff6161;" : "");
        personalNumberField.setStyle(personalNumber.isBlank() ? "-fx-background-color: #ff6161;" : "");
        dateOfBirthField.setStyle(!isDate(dateOfBirth) ? "-fx-background-color: #ff6161;" : "");

        return !name.isBlank() && !personalNumber.isBlank() && isDate(dateOfBirth);
    }

    private Boolean setErrColorContract(final String contractId, final String lendPrice, final String expireDate,
                                        final String itemInfo, final String totalPrice) {
        contractIdField.setStyle(contractId.isBlank() ? "-fx-background-color: #ff6161;" : "");
        lendPriceField.setStyle(!isInteger(lendPrice) ? "-fx-background-color: #ff6161;" : "");
        expireDateField.setStyle(!isDate(expireDate) ? "-fx-background-color: #ff6161;" : "");
        itemInfoField.setStyle(itemInfo.isBlank() ? "-fx-background-color: #ff6161;" : "");
        totalPriceField.setStyle(!isInteger(totalPrice) ? "-fx-background-color: #ff6161;" : "");

        return !contractId.isBlank() && isInteger(lendPrice) && isDate(expireDate) && !itemInfo.isBlank() && isInteger(totalPrice);
    }

    private void computeTotalPrice() {
        try {
            Date fromDate = getDateFromString(creationDateField.getText());
            Date toDate = getDateFromString(expireDateField.getText());
            int lendPrice = getInteger(lendPriceField.getText());
            int total = History.computeInterest(lendPrice, fromDate, toDate) + lendPrice;
            totalPriceField.setText(Integer.toString(total));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private int computeAvgPrice(final ObservableList<Price> prices) {
        if(prices.size() == 0) {
            return 0;
        }

        int sum = 0;

        for(Price price : prices) {
            sum += price.getPrice();
        }

        return sum / prices.size();
    }

    public void initContractData(final Contract src) {
        contract = src;

        lendPriceField.setText(contract.getLendPrice().toString());
        expireDateField.setText(getStringFromDate(contract.getExpireDateOrig()));
        contractIdField.setText(contract.getContractId());
        itemInfoField.setText(contract.getItemInfo());
        itemSpecificationField.setText(contract.getItemSpecification());
        totalPriceField.setText(contract.getTotalPriceOrig().toString());

        updateButton.setDisable(false);
    }

    public void initCustomerData(final Customer src) {
        customer = src;

        nameField.setText(customer.getName());
        genderField.setText(customer.getGender());
        birthPlaceField.setText(customer.getBirthPlace());
        addressField.setText(customer.getAddress());
        cityField.setText(customer.getCity());
        personalNumberField.setText(customer.getPersonalNumber());
        cardIdNumberField.setText(customer.getCardIdNumber());
        meuField.setText(customer.getMeu());
        nationalityField.setText(customer.getNationality());
        dateOfBirthField.setText(getStringFromDate(customer.getDateOfBirth()));

        nameField.setDisable(true);
        genderField.setDisable(true);
        birthPlaceField.setDisable(true);
        addressField.setDisable(true);
        cityField.setDisable(true);
        personalNumberField.setDisable(true);
        cardIdNumberField.setDisable(true);
        meuField.setDisable(true);
        nationalityField.setDisable(true);
        dateOfBirthField.setDisable(true);
        creationDateField.setDisable(true);
    }

    @FXML
    public void createContract(final MouseEvent event) throws IOException, ParseException {
        if(customer == null) {
            String name = nameField.getText().trim();
            String gender = genderField.getText().trim();
            String birthPlace = birthPlaceField.getText().trim();
            String address = addressField.getText().trim();
            String city = cityField.getText().trim();
            String personalNumber = personalNumberField.getText().trim();
            String cardIdNumber = cardIdNumberField.getText().trim();
            String meu = meuField.getText().trim();
            String nationality = nationalityField.getText().trim();
            String dateOfBirth = dateOfBirthField.getText().trim();

            if(setErrColorCustomer(name, personalNumber, dateOfBirth)) {
                customer = new Customer(name, gender, birthPlace, address, city, personalNumber, cardIdNumber, meu,
                        nationality, getDateFromString(dateOfBirth));
                customerService.create(customer);
            }
        }

        String contractId = contractIdField.getText().trim();
        String creationDate = creationDateField.getText().trim();
        String lendPrice = lendPriceField.getText().trim();
        String expireDate = expireDateField.getText().trim();
        String itemInfo = itemInfoField.getText().trim();
        String itemSpecification = itemSpecificationField.getText().trim();
        String totalPrice = totalPriceField.getText().trim();

        if(setErrColorContract(contractId, lendPrice, expireDate, itemInfo, totalPrice) && customer != null) {
            contract = new Contract(contractId, getDateFromString(creationDate), getInteger(lendPrice),
                    getDateFromString(expireDate), itemInfo, itemSpecification, getInteger(totalPrice));

            customerService.assignContract(customer, contract);
            contractService.create(contract);

            mainPane.getChildren().removeAll();
            mainPane.setCenter(getPage("contracts.fxml"));

            Notification.showPopupMessageOk("Hop dong da luu xong!", (Stage) mainPane.getScene().getWindow());
        } else {
            Notification.showPopupMessageErr("Chua khai het!", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void updateContract(final MouseEvent event) throws IOException, ParseException {
        String contractId = contractIdField.getText().trim();
        String lendPrice = lendPriceField.getText().trim();
        String expireDate = expireDateField.getText().trim();
        String itemInfo = itemInfoField.getText().trim();
        String itemSpecification = itemSpecificationField.getText().trim();
        String totalPrice = totalPriceField.getText().trim();

        if(setErrColorContract(contractId, lendPrice, expireDate, itemInfo, totalPrice)) {
            contract.setLendPrice(getInteger(lendPrice));
            contract.setExpireDateOrig(getDateFromString(expireDate));
            contract.setContractId(contractId);
            contract.setItemInfo(itemInfo);
            contract.setItemSpecification(itemSpecification);
            contract.setTotalPriceOrig(getInteger(totalPrice));
            contract.setCreationDate(getDateFromString(creationDateField.getText().trim()));

            contractService.update(contract);

            mainPane.getChildren().removeAll();
            mainPane.setCenter(getPage("contracts.fxml"));

            Notification.showPopupMessageOk("Hop dong da sua xong!", (Stage) mainPane.getScene().getWindow());
        } else {
            Notification.showPopupMessageErr("Chua khai het!", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void printContract(final MouseEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if(job != null) {
            PageLayout pageLayout = job.getPrinter().createPageLayout(Paper.A5, PageOrientation.PORTRAIT, 0, 0, 0, 0);

            String lendPrice = lendPriceField.getText().trim(), totalPrice = totalPriceField.getText().trim();

            lendPriceField.appendText(",-");
            totalPriceField.appendText(",-");

            contractDataPane.setScaleY(1);
            contractDataPane.setScaleX(1);

            boolean printed = job.printPage(pageLayout, contractDataPane);

            if(printed) {
                job.endJob();
                Notification.showPopupMessageOk("Dang in!", (Stage) mainPane.getScene().getWindow());
            }else {
                Notification.showPopupMessageErr("Khong in duoc!", (Stage) mainPane.getScene().getWindow());
            }

            lendPriceField.setText(lendPrice);
            totalPriceField.setText(totalPrice);
            contractDataPane.setScaleY(1.2);
            contractDataPane.setScaleX(1.2);
        }else {
            Notification.showPopupMessageErr("Khong in duoc!", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void searchPrices(final MouseEvent event) {
        priceList = FXCollections.observableArrayList(webService.getPrices(itemInfoField.getText(), (Stage) mainPane.getScene().getWindow()));

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tvPrices.setItems(priceList);
        labelAvgPrice.setText(String.valueOf(computeAvgPrice(priceList)));
        rightPane.setVisible(true);
    }

    @FXML
    public void handleMouseEvent(final MouseEvent event) throws URISyntaxException, IOException {
        Price price = tvPrices.getSelectionModel().getSelectedItem();

        if(event.getButton() == MouseButton.SECONDARY) {
            priceList.remove(price);
            labelAvgPrice.setText(String.valueOf(computeAvgPrice(priceList)));
            tvPrices.setItems(priceList);
        } else if(event.getClickCount() > 1 && price != null && Desktop.isDesktopSupported()
                && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(price.getLink()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        creationDateField.setText(new SimpleDateFormat("dd.MM.yy").format(new Date()));
        contractDataPane.setScaleY(1.2);
        contractDataPane.setScaleX(1.2);

        lendPriceField.textProperty().addListener((observable, oldVal, newVal) -> {
            if(!"".equals(expireDateField.getText()) && !newVal.isEmpty() && isInteger(newVal)) {
                computeTotalPrice();
            }
        });

        expireDateField.textProperty().addListener((observable, oldVal, newVal) -> {
            if(!"".equals(lendPriceField.getText()) && !newVal.isEmpty() && isDate(newVal)) {
                computeTotalPrice();
            }
        });
    }
}
