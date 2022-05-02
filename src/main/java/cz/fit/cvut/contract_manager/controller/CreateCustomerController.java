package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.Notification.Notification;
import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import cz.fit.cvut.contract_manager.util.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class CreateCustomerController extends Controller {
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

    public Button updateButton;
    public Button createContractButton;
    public Pane bottomLeftPane;
    public Pane bottomRightPane;

    public TextField fromYearField;
    public TextField toYearField;

    public Button monthsButton;
    public Button yearsButton;

    public PieChart pieChart;
    public Label labelTotalContracts;

    public LineChart<String, Integer> lineChartMoney;
    public Label labelTotalMoney;

    public LineChart<String, Integer> lineChartContracts;
    public Label labelTotalContractInterval;

    public BorderPane mainPane;

    public CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();
    private Customer customer;
    private List<Contract> contracts;

    public int getContractCount(int month, int year) {
        int count = 0;

        for(Contract contract : contracts) {
            int contractMonth = Util.getMonth(contract.getCreationDate());
            int contractYear = Util.getYear(contract.getCreationDate());

            count += contractMonth == month && contractYear == year ? 1 : 0;
        }

        return count;
    }

    public int getContractCount(int year) {
        int count = 0;

        for(Contract contract : contracts) {
            count += Util.getYear(contract.getCreationDate()) == year ? 1 : 0;
        }

        return count;
    }

    public int getMoneySpent(int month, int year) {
        int money = 0;

        for(Contract contract : contracts) {
            int contractMonth = Util.getMonth(contract.getCreationDate());
            int contractYear = Util.getYear(contract.getCreationDate());
            int interest = contract.getTotalPriceCurr() - contract.getLendPrice();

            money += contractMonth == month && contractYear == year && contract.isWithdrawn() ? interest : 0;
        }

        return money;
    }

    public int getMoneySpent(int year) {
        int money = 0;

        for(Contract contract : contracts) {
            int interest = contract.getTotalPriceCurr() - contract.getLendPrice();
            money += Util.getYear(contract.getCreationDate()) == year && contract.isWithdrawn() ? interest : 0;
        }

        return money;
    }

    private Boolean setErrColorCustomer(final String name, final String personalNumber, final String dateOfBirth) {
        nameField.setStyle(name.isBlank() ? "-fx-background-color: #ff6161;" : "");
        personalNumberField.setStyle(personalNumber.isBlank() ? "-fx-background-color: #ff6161;" : "");
        dateOfBirthField.setStyle(!isDate(dateOfBirth) ? "-fx-background-color: #ff6161;" : "");

        return !name.isBlank() && !personalNumber.isBlank() && isDate(dateOfBirth);
    }

    private void initPieChart() {
        int withdrawn = 0, left = 0, expired = 0, totalContracts = customer.getNumberOfContracts();

        for(Contract contract : contracts) {
            withdrawn += contract.isWithdrawn() ? 1 : 0;
            left += contract.isTakenOut() ? 1 : 0;
            expired += contract.isExpired() ? 1 : 0;
        }

        int stillValid = totalContracts - left - withdrawn - expired;

        if(totalContracts != 0) {
            double percentage = 100.0/totalContracts;
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Bo - " + left + " (" + String.format("%.02f", left * percentage) + "%)", left),
                new PieChart.Data("Da Lay - " + withdrawn + " (" + String.format("%.02f", withdrawn * percentage) + "%)", withdrawn),
                new PieChart.Data("Con Han - " + stillValid + " (" + String.format("%.02f", stillValid * percentage) + "%)", stillValid),
                new PieChart.Data("Het Han - " + expired + " (" + String.format("%.02f", expired * percentage) + "%)", expired)
            );

            pieChart.setData(pieChartData);
        }

        labelTotalContracts.setText(String.valueOf(totalContracts));
    }

    public void analyzeInMonths() {
        String fromYearStr = fromYearField.getText().trim();

        if(isInteger(fromYearStr) && getInteger(fromYearStr) > 0) {
            int year = getInteger(fromYearStr), totalSpent = 0, totalContracts = 0;

            List<XYChart.Data<String, Integer>> listMoney = new ArrayList<>();
            List<XYChart.Data<String, Integer>> listContracts = new ArrayList<>();

            for(int i = 0; i < 12; i++) {
                int money = getMoneySpent(i, year);
                listMoney.add(new XYChart.Data<>(Util.months[i], money));
                totalSpent += money;

                int numOfContracts = getContractCount(i, year);
                listContracts.add(new XYChart.Data<>(Util.months[i], numOfContracts));
                totalContracts += numOfContracts;
            }

            ObservableList<XYChart.Data<String, Integer>> lineChartMoneyData = FXCollections.observableArrayList(listMoney);
            labelTotalMoney.setText(String.valueOf(totalSpent));
            XYChart.Series<String, Integer> seriesMoney = new XYChart.Series<>(lineChartMoneyData);
            seriesMoney.setName(String.valueOf(year));

            ObservableList<XYChart.Data<String, Integer>> lineChartContractData = FXCollections.observableArrayList(listContracts);
            labelTotalContractInterval.setText(String.valueOf(totalContracts));
            XYChart.Series<String, Integer> seriesContracts = new XYChart.Series<>(lineChartContractData);
            seriesContracts.setName(String.valueOf(year));

            lineChartMoney.getData().add(seriesMoney);
            lineChartContracts.getData().add(seriesContracts);
        } else {
            Notification.showPopupMessageErr("So nam da nhap khong dung!", (Stage) mainPane.getScene().getWindow());
        }
    }

    public void initUpdateCustomer(final Customer src) {
        customer = src;
        contracts = new ArrayList<>(customerService.getContracts(customer.getId()));

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

        updateButton.setDisable(false);
        createContractButton.setDisable(false);
        monthsButton.setDisable(false);
        yearsButton.setDisable(false);

        initPieChart();

        fromYearField.setText(String.valueOf(Util.getYear(new Date())));
        analyzeInMonths();
    }

    @FXML
    public void createCustomer(final MouseEvent event) throws IOException, ParseException {
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
            mainPane.setCenter(getPage("customers.fxml"));

            Notification.showPopupMessageOk("Khach da luu xong!", (Stage) mainPane.getScene().getWindow());
        } else {
            Notification.showPopupMessageErr("Chua khai het!", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void updateCustomer(final MouseEvent event) throws IOException, ParseException {
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
            customer.setName(name);
            customer.setGender(gender);
            customer.setBirthPlace(birthPlace);
            customer.setAddress(address);
            customer.setCity(city);
            customer.setPersonalNumber(personalNumber);
            customer.setCardIdNumber(cardIdNumber);
            customer.setMeu(meu);
            customer.setNationality(nationality);
            customer.setDateOfBirth(getDateFromString(dateOfBirth));

            customerService.update(customer);
            mainPane.setCenter(getPage("customers.fxml"));

            Notification.showPopupMessageOk("Khach da sua xong!", (Stage) mainPane.getScene().getWindow());
        } else {
            Notification.showPopupMessageErr("Chua khai het!", (Stage) mainPane.getScene().getWindow());
        }

    }

    @FXML
    public void createContract(final MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/createContract.fxml"));
        Pane pane = loader.load();

        CreateContractController createContractController = loader.getController();
        createContractController.initCustomerData(customer);

        mainPane.setCenter(pane);
    }

    @FXML
    public void analyzeInMonths(final MouseEvent event) {
        analyzeInMonths();
    }

    @FXML
    public void analyzeInYears(final MouseEvent event) {
        String fromYearStr = fromYearField.getText().trim();
        String toYearStr = toYearField.getText().trim();

        if(isInteger(fromYearStr) && isInteger(toYearStr) && getInteger(fromYearStr) > 0 && getInteger(toYearStr) > 0) {
            int fromYear = getInteger(fromYearStr), toYear = getInteger(toYearStr);
            int totalSpent = 0, totalContracts = 0;

            List<XYChart.Data<String, Integer>> listMoney = new ArrayList<>();
            List<XYChart.Data<String, Integer>> listContracts = new ArrayList<>();

            for(int i = fromYear; i <= toYear; i++) {
                int money = getMoneySpent(i);
                listMoney.add(new XYChart.Data<>(String.valueOf(i), money));
                totalSpent += money;

                int numOfContracts = getContractCount(i);
                listContracts.add(new XYChart.Data<>(String.valueOf(i), numOfContracts));
                totalContracts += numOfContracts;
            }

            ObservableList<XYChart.Data<String, Integer>> lineChartMoneyData = FXCollections.observableArrayList(listMoney);
            labelTotalMoney.setText(String.valueOf(totalSpent));
            XYChart.Series<String, Integer> seriesMoney = new XYChart.Series<>(lineChartMoneyData);
            seriesMoney.setName(fromYear + "-" + toYear);

            ObservableList<XYChart.Data<String, Integer>> lineChartContractData = FXCollections.observableArrayList(listContracts);
            labelTotalContractInterval.setText(String.valueOf(totalContracts));
            XYChart.Series<String, Integer> seriesContracts = new XYChart.Series<>(lineChartContractData);
            seriesContracts.setName(fromYear + "-" + toYear);

            lineChartMoney.getData().add(seriesMoney);
            lineChartContracts.getData().add(seriesContracts);
        } else {
            Notification.showPopupMessageErr("So nam da nhap khong dung!", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void clearChart(final MouseEvent event) {
        labelTotalMoney.setText("0");
        labelTotalContractInterval.setText("0");

        lineChartMoney.getData().clear();
        lineChartContracts.getData().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
