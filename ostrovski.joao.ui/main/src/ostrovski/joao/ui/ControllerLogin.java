package ostrovski.joao.ui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.ui.helpers.Login;

public class ControllerLogin {

    private Controller mainController;

    public void setMainController(Controller controller) {
        if (controller == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        this.mainController = controller;
    }

    public Controller getMainController() {
        return this.mainController;
    }

    @FXML
    private VBox loginControllerVbox;

    public VBox getLoginControllerVbox() {
        return loginControllerVbox;
    }

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    public TextField getLoginField() {
        return loginField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    @FXML
    public void handleLogin() {
        Login.authentication(this);
    }

    @FXML
    public void handleRegistration() {
        Login.registration(this);
    }

    public void loadNode() {
        Controller mainController = this.getMainController();
        mainController
                .setMainBorderPaneCenter(
                        this
                            .getLoginControllerVbox());
    }
}