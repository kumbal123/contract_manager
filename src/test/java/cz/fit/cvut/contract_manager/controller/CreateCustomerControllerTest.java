package cz.fit.cvut.contract_manager.controller;

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
import org.testfx.matcher.control.TableViewMatchers;

import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(ApplicationExtension.class)
class CreateCustomerControllerTest {

    private CustomerRepositoryService service = CustomerRepositoryService.getInstance();

    @AfterEach
    void tearDown() {
        service.deleteAll();
    }

    @Start
    public void start(final Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/createCustomer.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    void createCustomer(final FxRobot robot) {
        String name = "Mike Adams";
        String dateOfBirth = "12.10.1998";
        String address = "Unknown 123";
        String personalNumber = "124312/4534";

        TextField nameField = robot.lookup("#nameField").queryAs(TextField.class);
        nameField.setText(name);

        TextField dateOfBirthField = robot.lookup("#dateOfBirthField").queryAs(TextField.class);
        dateOfBirthField.setText(dateOfBirth);

        TextField addressField = robot.lookup("#addressField").queryAs(TextField.class);
        addressField.setText(address);

        TextField personalNumberField = robot.lookup("#personalNumberField").queryAs(TextField.class);
        personalNumberField.setText(personalNumber);

        robot.clickOn("#createButton");
        robot.clickOn("#notification");

        verifyThat("#tvCustomers", TableViewMatchers.hasNumRows(1));
        verifyThat("#tvCustomers", TableViewMatchers.containsRow(name, personalNumber, address, "0", dateOfBirth));
    }
}