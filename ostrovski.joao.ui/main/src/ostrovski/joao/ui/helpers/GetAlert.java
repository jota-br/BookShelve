package ostrovski.joao.ui.helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class GetAlert {

    public static Optional<ButtonType> getInformationAlert(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(context);
            return alert.showAndWait();
    }

    public static Optional<ButtonType> getConfirmationAlert(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        return alert.showAndWait();
    }
}