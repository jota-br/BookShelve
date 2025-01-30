package ostrovski.joao.ui.helpers;

import ostrovski.joao.common.model.dto.UserDTO;
import ostrovski.joao.services.UserService;
import ostrovski.joao.services.UserSession;
import ostrovski.joao.ui.ControllerLogin;
import ostrovski.joao.ui.ControllerTopBar;

public class Login {

    // load ControllerLogin to Main Controller -> Main Border Pane CENTER
    public static void login(ControllerTopBar topBarController) {

        String userInSession = UserSession.getInstance().getUserInSessionPrivilege();
        if (userInSession == null) {
            // set Main Border Pane CENTER content to log in fields
            topBarController.getMainController()
                    .getLoginController()
                    .loadNode();
        } else {
            // user is loged-in and is now login-out
            // login out alert
            var result = GetAlert.getConfirmationAlert(
                    ResourceBundleService.getString("Logout"),
                    ResourceBundleService.getString("login_out_of_the_system"),
                    ResourceBundleService.getString("click_ok_to_confirm"));
            if (result.isPresent() && result.get().getButtonData().isDefaultButton()) {
                // MenuItem text changed to Login
                // Changes the top ContextMenu TEXT to 'Login'
                topBarController
                        .getMainController()
                        .getTopBarController()
                        .getMenuItemLogin()
                        .setText(ResourceBundleService.getString("Login"));

                // closes current session
                UserSession.getInstance().clearUserInSession();
                // remove admin menu items / reloads menu (LEFT MENU)
                topBarController
                        .getMainController()
                        .getLeftMenuController()
                        .loadNode();
            }
        }
    }

    public static void registration(ControllerLogin controllerLogin) {
        String login = controllerLogin.getLoginField().getText();
        String password = controllerLogin.getPasswordField().getText();
        UserDTO user = UserService.userGeneration(login, password, 3);
        if (user != null) {
            GetAlert.getConfirmationAlert(
                    ResourceBundleService.getString("Registration"),
                    ResourceBundleService.getString("registration_was_successful"),
                    ResourceBundleService.getString("click_ok_to_confirm"));
        } else {
            GetAlert.getConfirmationAlert(
                    ResourceBundleService.getString("Registration"),
                    ResourceBundleService.getString("registration_was_canceled_login_and_password_are_required"),
                    ResourceBundleService.getString("click_ok_to_confirm"));
        }
    }

    public static void authentication(ControllerLogin controllerLogin) {
        String login = controllerLogin.getLoginField().getText();
        String password = controllerLogin.getPasswordField().getText();
        UserDTO user = UserService.authentication(login, password);

        if (user != null) {
            UserSession.getInstance().setUserInSession(user);
            // MenuItem text changed to Log-out
            // Changes the top ContextMenu TEXT to 'Logout'
            controllerLogin
                    .getMainController()
                    .getTopBarController()
                    .getMenuItemLogin()
                    .setText("Logout");
            if (UserSession.getInstance().isAdmin()) {
                // Reloads LEFT MENU CONTROLLER to update LEFT MENU with admin buttons
                controllerLogin.getMainController().getLeftMenuController().showAdminLeftMenu();
            }
            // Returns to HOME content TABLE VIEW CONTROLLER
            controllerLogin.getMainController().getTableViewController().loadNode();
            // Reset login fields
            controllerLogin.getLoginField().setText("");
            controllerLogin.getPasswordField().setText("");
            controllerLogin.getMainController().setMainBorderPaneBottom(controllerLogin.getMainController().getBottomBarController().getBottomBarControllerHbox());
        } else {
            // Login was unsuccessful
            GetAlert.getConfirmationAlert(
                    ResourceBundleService.getString("Login"),
                    ResourceBundleService.getString("login_was_unsuccessful"),
                    ResourceBundleService.getString("click_ok_to_confirm"));
        }
    }
}