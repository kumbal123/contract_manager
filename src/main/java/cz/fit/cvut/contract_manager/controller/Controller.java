package cz.fit.cvut.contract_manager.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class Controller implements Initializable {

    public Pane getPage(final String fileName) throws IOException {
        return FXMLLoader.load(getClass().getResource("/fxml/" + fileName));
    }

    public static Date getDateFromString(final String str) throws ParseException {
        return new SimpleDateFormat("dd.MM.yy").parse(str);
    }

    public static String getStringFromDate(final Date date) {
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }

    public SimpleStringProperty getStringPropertyFromDate(final Date date) {
        return new SimpleStringProperty(getStringFromDate(date));
    }

    public Integer getInteger(final String str) {
        return Integer.parseInt(str);
    }

    public Boolean isInteger(final String str) {
        try {
            getInteger(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Boolean isDate(final String str) {
        try {
            getDateFromString(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
