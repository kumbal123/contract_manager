package cz.fit.cvut.contract_manager.controller;

import cz.fit.cvut.contract_manager.service.ContractService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ContractController implements Initializable {

    public TextField contractField;
    public TextField priceField;

    private final ContractService service = ContractService.getInstance();

    @FXML
    public void createContract(MouseEvent event) {
        //Node node = (Node) event.getSource();
        //Stage stage = (Stage) node.getScene().getWindow();
        String id = contractField.getText().trim();
        String price = priceField.getText().trim();

        service.saveContract(id, Integer.parseInt(price));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
