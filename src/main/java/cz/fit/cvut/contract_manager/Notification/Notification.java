package cz.fit.cvut.contract_manager.Notification;

import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Notification {
    private static Popup createPopup(final String message, final String className) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseReleased(e -> popup.hide());
        label.setPrefWidth(1100);
        label.setLayoutX(75);
        label.setLayoutY(-328);
        label.getStyleClass().add(className);
        label.getStylesheets().add("/css/style.css");
        popup.getContent().add(label);
        return popup;
    }



    public static void showPopupMessageErr(final String message, final Stage stage) {
        final Popup popup = createPopup(message, "popup_err");
        popup.show(stage);
    }

    public static void showPopupMessageOk(final String message, final Stage stage) {
        final Popup popup = createPopup(message, "popup_ok");
        popup.show(stage);
    }
}
