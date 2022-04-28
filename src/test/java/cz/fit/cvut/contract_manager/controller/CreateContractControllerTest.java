package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.entity.Contract;
import cz.fit.cvut.contract_manager.service.ContractRepositoryService;
import cz.fit.cvut.contract_manager.service.CustomerRepositoryService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.TextInputControlMatchers;

import static cz.fit.cvut.contract_manager.controller.Controller.getStringFromDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

class CreateContractControllerTest extends JavaFxTest {

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
    void shouldCreateContractWhenGivenAllRequiredInformation(final FxRobot robot) {
        String name = "Mike Adams";
        String dateOfBirth = "12.10.98";
        String personalNumber = "124312/4534";
        String contractNumber = "A1";
        String creationDate = "01.01.22";
        String lendPrice = "1000";
        String expireDate = "11.01.22";
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

        Label popUp = robot.lookup("#notification").queryAs(Label.class);
        assertTrue(popUp.getStyleClass().contains("popup_ok"));
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
    void shouldCalculateTotalPriceWhenGivenLendPriceAndExpireDate(final FxRobot robot) {
        String creationDate = "01.01.2022";
        String lendPrice = "1000";
        String expireDate = "11.01.2022";

        TextField creationDateField = robot.lookup("#creationDateField").queryAs(TextField.class);
        creationDateField.setText(creationDate);

        TextField lendPriceField = robot.lookup("#lendPriceField").queryAs(TextField.class);
        lendPriceField.setText(lendPrice);

        TextField expireDateField = robot.lookup("#expireDateField").queryAs(TextField.class);
        expireDateField.setText(expireDate);

        verifyThat("#totalPriceField", TextInputControlMatchers.hasText("1100"));

        lendPriceField.setText("2000");
        verifyThat("#totalPriceField", TextInputControlMatchers.hasText("2200"));

        expireDateField.setText("21.01.2022");
        verifyThat("#totalPriceField", TextInputControlMatchers.hasText("2400"));
    }

    @Test
    void shouldNotCreateContractWhenSomeRequiredInformationIsMissing(final FxRobot robot) {
        String contractNumber = "A1";

        TextField contractIdField = robot.lookup("#contractIdField").queryAs(TextField.class);
        contractIdField.setText(contractNumber);

        robot.clickOn("#createButton");

        Label popUp = robot.lookup("#notification").queryAs(Label.class);
        assertTrue(popUp.getStyleClass().contains("popup_err"));
        robot.clickOn("#notification");

        Contract contract = contractService.getMostRecentByContractId("A1");

        assertNull(contract);
    }
}