package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.entity.Customer;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static cz.fit.cvut.contract_manager.controller.Controller.getStringFromDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class CreateContractControllerTest {

    private ContractRepositoryService contractService = ContractRepositoryService.getInstance();
    private CustomerRepositoryService customerService = CustomerRepositoryService.getInstance();

    @AfterEach
    void tearDown() {
        contractService.deleteAll();
        customerService.deleteAll();
    }

    @Start
    public void start(final Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/createContract.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void createContract(final FxRobot robot) {
        String name = "Mike Adams";
        String dateOfBirth = "12.10.1998";
        String personalNumber = "124312/4534";
        String contractNumber = "A1";
        String creationDate = "01.01.2022";
        String lendPrice = "1000";
        String expireDate = "11.01.2022";
        String itemInfo = "Phone Samsung";
        String totalPrice = "1100";

        TextField nameField = robot.lookup("#nameField").queryAs(TextField.class);
        nameField.setText(name);

        TextField dateOfBirthField = robot.lookup("#dateOfBirthField").queryAs(TextField.class);
        dateOfBirthField.setText(dateOfBirth);

        TextField personalNumberField = robot.lookup("#personalNumberField").queryAs(TextField.class);
        personalNumberField.setText(personalNumber);

        TextField contractIdField = robot.lookup("#contractIdField").queryAs(TextField.class);
        contractIdField.setText(contractNumber);

        TextField creationDateField = robot.lookup("#creationDateField").queryAs(TextField.class);
        creationDateField.setText(creationDate);

        TextField lendPriceField = robot.lookup("#lendPriceField").queryAs(TextField.class);
        lendPriceField.setText(lendPrice);

        TextField expireDateField = robot.lookup("#expireDateField").queryAs(TextField.class);
        expireDateField.setText(expireDate);

        TextField itemInfoField = robot.lookup("#itemInfoField").queryAs(TextField.class);
        itemInfoField.setText(itemInfo);

        TextField totalPriceField = robot.lookup("#totalPriceField").queryAs(TextField.class);
        totalPriceField.setText(totalPrice);

        robot.clickOn("#createButton");
        robot.clickOn("#notification");

        Contract contract = contractService.getMostRecentByContractId("A1");

        assertEquals(name, contract.getName());
        assertEquals(dateOfBirth, getStringFromDate(contract.getDateOfBirth()));
        assertEquals(personalNumber, contract.getPersonalNumber());
        assertEquals(contractNumber, contract.getContractId());
        assertEquals(creationDate, getStringFromDate(contract.getCreationDate()));
        assertEquals(lendPrice, String.valueOf(contract.getLendPrice()));
        assertEquals(expireDate, getStringFromDate(contract.getExpireDateOrig()));
        assertEquals(itemInfo, contract.getItemInfo());
        assertEquals(totalPrice, String.valueOf(contract.getTotalPriceOrig()));
    }

    @Test
    void printContract() {
    }

    @Test
    void searchPrices() {
    }

    @Test
    void handleMouseEvent() {
    }
}