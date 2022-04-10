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
    public PieChart pieChart;
    public Label labelTotalContracts;
    public Pane bottomLeftPane;
    public Pane bottomRightPane;

    public LineChart<String, Integer> lineChartMoney;
    public TextField fromYearMoneyField;
    public TextField toYearMoneyField;
    public Label labelTotalMoney;

    public LineChart<String, Integer> lineChartContracts;
    public TextField fromYearContractsField;
    public TextField toYearContractsField;
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
        int withdrawn = 0, left = 0, totalContracts = customer.getNumberOfContracts();

        for(Contract contract : contracts) {
            withdrawn += contract.isWithdrawn() ? 1 : 0;
            left += contract.isTakenOut() ? 1 : 0;
        }

        int stillValid = totalContracts - left - withdrawn;

        if(totalContracts != 0) {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Bo - " + left + " - " + (left * 100/totalContracts) + "%", left),
                new PieChart.Data("Da Lay - " + withdrawn + " - " + (withdrawn * 100/totalContracts) + "%", withdrawn),
                new PieChart.Data("Con han - " + stillValid + " - " + (stillValid * 100/totalContracts) + "%", stillValid)
            );

            pieChart.setData(pieChartData);
        }

        labelTotalContracts.setText(String.valueOf(totalContracts));
    }

    private void initLineChartContractCount() {
        List<XYChart.Data<String, Integer>> list = new ArrayList<>();

        int year = Util.getYear(new Date()), total = 0;

        for(int i = 0; i < 12; i++) {
            int contractCount = getContractCount(i, year);
            list.add(new XYChart.Data<>(Util.months[i], contractCount));
            total += contractCount;
        }

        ObservableList<XYChart.Data<String, Integer>> lineChartContractData = FXCollections.observableArrayList(list);

        fromYearContractsField.setText(String.valueOf(year));
        labelTotalContractInterval.setText(String.valueOf(total));

        XYChart.Series<String, Integer> series = new XYChart.Series<>(lineChartContractData);
        series.setName(String.valueOf(year));

        lineChartContracts.getData().add(series);
    }

    private void initLineChartMoney() {
        List<XYChart.Data<String, Integer>> list = new ArrayList<>();

        int year = Util.getYear(new Date()), total = 0;

        for(int i = 0; i < 12; i++) {
            int money = getMoneySpent(i, year);
            list.add(new XYChart.Data<>(Util.months[i], money));
            total += money;
        }

        ObservableList<XYChart.Data<String, Integer>> lineChartMoneyData = FXCollections.observableArrayList(list);

        fromYearMoneyField.setText(String.valueOf(year));
        labelTotalMoney.setText(String.valueOf(total));

        XYChart.Series<String, Integer> series = new XYChart.Series<>(lineChartMoneyData);
        series.setName(String.valueOf(year));

        lineChartMoney.getData().add(series);
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

        initPieChart();
        initLineChartContractCount();
        initLineChartMoney();
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

            Notification.showPopupMessageOk("Contract successfully created!", (Stage) mainPane.getScene().getWindow());
        } else {
            Notification.showPopupMessageErr("Some required fields are empty!", (Stage) mainPane.getScene().getWindow());
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

            Notification.showPopupMessageOk("Contract successfully created!", (Stage) mainPane.getScene().getWindow());
        } else {
            Notification.showPopupMessageErr("Some required fields are empty!", (Stage) mainPane.getScene().getWindow());
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
    public void analyzeMoneyInMonths(final MouseEvent event) {
        String fromYearStr = fromYearMoneyField.getText().trim();

        // check if not negative number
        if(isInteger(fromYearStr)) {
            int year = getInteger(fromYearStr), total = 0;

            List<XYChart.Data<String, Integer>> list = new ArrayList<>();

            for(int i = 0; i < 12; i++) {
                int money = getMoneySpent(i, year);
                list.add(new XYChart.Data<>(Util.months[i], money));
                total += money;
            }

            ObservableList<XYChart.Data<String, Integer>> lineChartMoneyData = FXCollections.observableArrayList(list);

            labelTotalMoney.setText(String.valueOf(total));

            XYChart.Series<String, Integer> series = new XYChart.Series<>(lineChartMoneyData);
            series.setName(String.valueOf(year));

            lineChartMoney.getData().add(series);
        } else {
            Notification.showPopupMessageErr("Enter a valid year", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void analyzeMoneyInYears(final MouseEvent event) {
        String fromYearStr = fromYearMoneyField.getText().trim();
        String toYearStr = toYearMoneyField.getText().trim();

        if(isInteger(fromYearStr) && isInteger(toYearStr)) {
            int fromYear = getInteger(fromYearStr), toYear = getInteger(toYearStr), total = 0;

            List<XYChart.Data<String, Integer>> list = new ArrayList<>();

            for(int i = fromYear; i <= toYear; i++) {
                int money = getMoneySpent(i);
                list.add(new XYChart.Data<>(String.valueOf(i), money));
                total += money;
            }

            ObservableList<XYChart.Data<String, Integer>> lineChartContractData = FXCollections.observableArrayList(list);

            labelTotalMoney.setText(String.valueOf(total));
            lineChartMoney.getData().add(new XYChart.Series<>(lineChartContractData));
        } else {
            Notification.showPopupMessageErr("Enter valid years", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void analyzeContractCountInMonths(final MouseEvent event) {
        String fromYearStr = fromYearContractsField.getText().trim();

        // check if not negative number
        if(isInteger(fromYearStr)) {
            int year = getInteger(fromYearStr), total = 0;

            List<XYChart.Data<String, Integer>> list = new ArrayList<>();

            for(int i = 0; i < 12; i++) {
                int money = getContractCount(i, year);
                list.add(new XYChart.Data<>(Util.months[i], money));
                total += money;
            }

            ObservableList<XYChart.Data<String, Integer>> lineChartContractData = FXCollections.observableArrayList(list);

            labelTotalContractInterval.setText(String.valueOf(total));

            XYChart.Series<String, Integer> series = new XYChart.Series<>(lineChartContractData);
            series.setName(String.valueOf(year));

            lineChartContracts.getData().add(series);
        } else {
            Notification.showPopupMessageErr("Enter a valid year", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void analyzeContractCountInYears(final MouseEvent event) {
        String fromYearStr = fromYearContractsField.getText().trim();
        String toYearStr = toYearContractsField.getText().trim();

        if(isInteger(fromYearStr) && isInteger(toYearStr)) {
            int fromYear = getInteger(fromYearStr), toYear = getInteger(toYearStr), total = 0;

            List<XYChart.Data<String, Integer>> list = new ArrayList<>();

            for(int i = fromYear; i <= toYear; i++) {
                int money = getContractCount(i);
                list.add(new XYChart.Data<>(String.valueOf(i), money));
                total += money;
            }

            ObservableList<XYChart.Data<String, Integer>> lineChartContractData = FXCollections.observableArrayList(list);

            labelTotalContractInterval.setText(String.valueOf(total));
            lineChartContracts.getData().add(new XYChart.Series<>(lineChartContractData));
        } else {
            Notification.showPopupMessageErr("Enter valid years", (Stage) mainPane.getScene().getWindow());
        }
    }

    @FXML
    public void clearMoneyChart(final MouseEvent event) {
        labelTotalMoney.setText("0");
        lineChartMoney.getData().clear();
    }

    @FXML
    public void clearContractChart(final MouseEvent event) {
        labelTotalContractInterval.setText("0");
        lineChartContracts.getData().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
