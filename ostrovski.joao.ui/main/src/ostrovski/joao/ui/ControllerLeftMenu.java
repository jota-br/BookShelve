package ostrovski.joao.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.model.dto.AuthorDTO;
import ostrovski.joao.common.model.dto.BookDTO;
import ostrovski.joao.common.model.dto.CategoryDTO;
import ostrovski.joao.common.model.dto.PublisherDTO;
import ostrovski.joao.services.BookService;
import ostrovski.joao.services.UserSession;
import ostrovski.joao.ui.helpers.DialogResult;
import ostrovski.joao.ui.helpers.GetAlert;
import ostrovski.joao.ui.helpers.ResourceBundleService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ControllerLeftMenu {

    // main CONTROLLER
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

    // get selected item from the tableView
    public BookDTO getTableViewSelection() {
        if (this.getMainController() == null && this.getMainController().getTableViewController() == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return null;
        }

        return this.getMainController()
                .getTableViewController()
                .getTableViewSelection();
    }

    // this controller main Node
    @FXML
    private VBox leftMenuControllerVbox;

    public VBox getLeftMenuControllerVbox() {
        return leftMenuControllerVbox;
    }

    @FXML
    private VBox userMenu;

    @FXML
    private VBox adminMenu;

    @FXML
    private VBox adminMenuDetailedView;

    @FXML
    private ToggleButton viewBookToggleButton;

    @FXML
    public void setToggleButtonState (boolean newState) {
        this.viewBookToggleButton.setSelected(newState);
    }

    // loads upsert Node for adding new books
    @FXML
    public void handleAdd() {

        if (!UserSession.getInstance().isAdmin()) {
            return;
        }

        if (this.getMainController() == null && this.getMainController().getUpsertController() == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }

        getMainController().getUpsertController().setFlag(false);
        this.getMainController().getUpsertController().loadNode();
        setToggleButtonState(false);
        showAdminLeftMenu();
    }

    // loads upsert Node for editing existing books
    @FXML
    public void handleEdit() {

        if (!UserSession.getInstance().isAdmin()) {
            return;
        }

        BookDTO selection = null;
        selection = getTableViewSelection();
        if (selection == null) {
            GetAlert.getInformationAlert(
                    ResourceBundleService.getString("no_selection"),
                    ResourceBundleService.getString("no_book_selected"),
                    ResourceBundleService.getString("select_a_book_and_try_again"));
            return;
        }

        if (this.getMainController() == null && this.getMainController().getUpsertController() == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }

        getMainController().getUpsertController().setFlag(true);
        getMainController().getUpsertController().loadNode();
        getMainController().getUpsertController().setFieldsValues(selection);
        getMainController().setMainBorderPaneBottom(new VBox());
        setToggleButtonState(false);
        showAdminLeftMenu();
    }

    // handles DELETE keypress
    @FXML
    public void handleDeleteKeyPress(KeyEvent keyEvent) {

        if (!UserSession.getInstance().isAdmin()) {
            return;
        }

        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            handleDelete();
        }
    }

    // handles deletion of books
    @FXML
    public void handleDelete() {

        if (!UserSession.getInstance().isAdmin()) {
            return;
        }
        
        BookDTO selection = getTableViewSelection();
        if (selection == null) {
            GetAlert.getInformationAlert(
                    ResourceBundleService.getString("no_selection"),
                    ResourceBundleService.getString("no_book_selected"),
                    ResourceBundleService.getString("select_a_book_and_try_again"));
            return;
        }

        Optional<ButtonType> result = GetAlert.getConfirmationAlert(
                ResourceBundleService.getString("attention"),
                ResourceBundleService.getString("deleting") + " " + selection.title(),
                ResourceBundleService.getString("deletion_irreversible_confirmation"));
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            BookService.executeDelete(selection);
            getMainController()
                    .getTableViewController()
                    .removeItemFromObservableList(selection);
            getMainController()
                    .getTopBarController()
                    .updatePageAction(0, false);
            if (viewBookToggleButton.isSelected()) {
                setToggleButtonState(false);
            }
            return;
        } else if (result.isPresent() && (result.get() == ButtonType.CANCEL)) {
            return;
        }
        Logger.log(new IllegalStateException(ExceptionMessage.METHOD_FAILURE.getMessage()));
    }

    private DialogResult getEditDialog(String title, String header) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainController.getMainBorderPaneScene());
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editDialog.fxml"));
        fxmlLoader.setResources(ResourceBundleService.getResourceBundle());

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            Logger.log(e);
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ControllerEditDialog editDialogController = fxmlLoader.getController();
        return new DialogResult(dialog, editDialogController);
    }

    @FXML
    public void editAuthor() {

        if (!UserSession.getInstance().isAdmin()) {
            return;
        }

        if (getTableViewSelection() == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return;
        }
        List<AuthorDTO> selection = getTableViewSelection().authors();

        if (selection == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return;
        }

        DialogResult dialogResult = getEditDialog(
                ResourceBundleService.getString("select_author_to_edit"),
                ResourceBundleService.getString("select_an_author_to_edit_info"));

        Button okButton = (Button) dialogResult.getDialog().getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> dialogResult.getController().handleOkButtonAuthorEdit(event, dialogResult, selection));

        dialogResult.getController().setMainController(this.getMainController());
        boolean result = dialogResult.getController().loadSelectAuthorEditDialog(selection);
        if (result) {
            dialogResult.getDialog().showAndWait();
        } else {
            GetAlert.getInformationAlert(
                    ResourceBundleService.getString("no_author_found"),
                    ResourceBundleService.getString("book_has_no_author"),
                    ResourceBundleService.getString("edit_book_to_include_author"));
        }
    }

    @FXML
    public void editPublisher() {

        if (!UserSession.getInstance().isAdmin()) {
            return;
        }

        if (getTableViewSelection() == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return;
        }
        List<PublisherDTO> selection = getTableViewSelection().publishers();

        if (selection == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return;
        }

        DialogResult dialogResult = getEditDialog(
                ResourceBundleService.getString("select_publisher_to_edit"),
                ResourceBundleService.getString("select_a_publisher_to_edit_info"));

        Button okButton = (Button) dialogResult.getDialog().getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> dialogResult.getController().handleOkButtonPublisherEdit(event, dialogResult, selection));

        dialogResult.getController().setMainController(this.getMainController());
        boolean result = dialogResult.getController().loadSelectPublisherEditDialog(selection);
        if (result) {
            dialogResult.getDialog().showAndWait();
        } else {
            GetAlert.getInformationAlert(
                    ResourceBundleService.getString("no_publisher_found"),
                    ResourceBundleService.getString("book_has_no_publisher"),
                    ResourceBundleService.getString("edit_book_to_include_publisher"));
        }
    }

    @FXML
    public void editCategory() {

        if (!UserSession.getInstance().isAdmin()) {
            return;
        }

        if (getTableViewSelection() == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return;
        }
        List<CategoryDTO> selection = getTableViewSelection().categories();

        if (selection == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return;
        }

        DialogResult dialogResult = getEditDialog(
                ResourceBundleService.getString("select_category_to_edit"),
                ResourceBundleService.getString("select_a_category_to_edit_info"));

        Button okButton = (Button) dialogResult.getDialog().getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> dialogResult.getController().handleOkButtonCategoryEdit(event, dialogResult, selection));

        dialogResult.getController().setMainController(this.getMainController());
        boolean result = dialogResult.getController().loadSelectCategoryEditDialog(selection);
        if (result) {
            dialogResult.getDialog().showAndWait();
        } else {
            GetAlert.getInformationAlert(
                    ResourceBundleService.getString("no_category_found"),
                    ResourceBundleService.getString("book_has_no_category"),
                    ResourceBundleService.getString("edit_book_to_include_category"));
        }
    }

    // call from fxml ToogleButton to open book information
    @FXML
    public void openDetailedViewWithSelection() {
        // null to get selected entity from table view
        // to select a entity send as parameter the BookDTO to view
        openDetailedView(null);

        if (UserSession.getInstance().isAdmin()) {
            if (viewBookToggleButton.isSelected()) {
                showAdminDetailedViewLeftMenu();
            } else {
                showAdminLeftMenu();
            }
        }
    }

    // handles fxml ToogleButton action
    // selection required
    @FXML
    public void openDetailedView(BookDTO book) {
        if (viewBookToggleButton.isSelected()) {
            BookDTO selection = book != null ? book : getTableViewSelection();
            if (selection == null) {
                GetAlert.getInformationAlert(
                        ResourceBundleService.getString("no_book_selected"),
                        ResourceBundleService.getString("detailed_view_needs_selected_book"),
                        ResourceBundleService.getString("select_a_book_and_try_again"));
                return;
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detailedView.fxml"));
                fxmlLoader.setResources(ResourceBundleService.getResourceBundle());

                Node detailedView = fxmlLoader.load();
                ControllerDetailedView detailedViewController = fxmlLoader.getController();
                detailedViewController.setFieldValues(selection);

                getMainController().setMainBorderPaneCenter(detailedView);
                getMainController().setMainBorderPaneBottom(new VBox());
            } catch (IOException e) {
                Logger.log(e);
            }
        } else {
            getMainController().getTableViewController().loadNode();
            getMainController().setMainBorderPaneBottom(getMainController().getBottomBarController().getBottomBarControllerHbox());
        }
    }

    public void showAdminDetailedViewLeftMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("leftMenu/leftMenuAdminDetailedView.fxml"));
            loader.setController(this);
            loader.setResources(ResourceBundleService.getResourceBundle());

            Node leftMenuAdminDetailedView = loader.load();

            adminMenuDetailedView.getChildren().add(leftMenuAdminDetailedView);
            adminMenu.getChildren().clear();
        } catch (IOException e) {
            Logger.log(e);
        }
    }

    public void showAdminLeftMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("leftMenu/leftMenuAdmin.fxml"));
            loader.setController(this);
            loader.setResources(ResourceBundleService.getResourceBundle());

            Node leftMenuAdmin = loader.load();
            adminMenu.getChildren().add(leftMenuAdmin);
            adminMenuDetailedView.getChildren().clear();
        } catch (IOException e) {
            Logger.log(e);
        }
    }

    public void loadNode() {
        Controller mainController = this.getMainController();
        mainController
                .setMainBorderPaneLeft(
                        this
                            .getLeftMenuControllerVbox());
    }
}