package cz.fit.cvut.contract_manager.Notification;

import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Notification {
    private static Popup createPopup(final String message, final String className, final Stage stage) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.setOnShown(e -> {
            popup.setX(stage.getX() + 63);
            popup.setY(stage.getY() + 22);
        });
        Label label = new Label(message);
        label.setPrefWidth(stage.getWidth() - 63);
        label.setOnMouseReleased(e -> popup.hide());
        label.getStyleClass().add(className);
        label.getStylesheets().add("/css/style.css");
        popup.getContent().add(label);
        return popup;
    }

    public static void showPopupMessageErr(final String message, final Stage stage) {
        final Popup popup = createPopup(message, "popup_err", stage);
        popup.show(stage);
    }

    public static void showPopupMessageOk(final String message, final Stage stage) {
        final Popup popup = createPopup(message, "popup_ok", stage);
        popup.show(stage.getScene().getWindow());
    }
}
