package ostrovski.joao.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.db.helpers.PaginatedQuery;
import ostrovski.joao.ui.helpers.Login;
import ostrovski.joao.ui.helpers.ResourceBundleService;
import ostrovski.joao.ui.helpers.UpdatePage;

import java.util.Map;

public class ControllerTopBar {

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

    // search CHOICE BOX provides the type of data to search
    @FXML
    private ChoiceBox<String> searchBoxSelector;

    public String getSearchBoxSelectorValue() {
        return (String) this.searchBoxSelector.getSelectionModel().selectedItemProperty().get();
    }

    @FXML
    private TextField searchBar;

    @FXML
    private MenuItem menuItemLogin;

    public MenuItem getMenuItemLogin() {
        return menuItemLogin;
    }

    @FXML
    private ChoiceBox<String> themeSelector;
    public String getThemeSelectorSelection() {
        return (String) this.themeSelector.getSelectionModel().selectedItemProperty().get();
    }

    @FXML
    private ChoiceBox<String> languageSelector;
    public String getLanguageSelectorSelection() {
        return (String) this.languageSelector.getSelectionModel().selectedItemProperty().get();
    }

    // main Node of this controller
    @FXML
    private VBox topBarControllerVbox;

    public VBox getTopBarControllerVbox() {
        return topBarControllerVbox;
    }

    private static Map<String, Integer> pageConfig = Map.of(
            "currentPage", 1,
            "itemsPerPage", 50,
            "totalPages", 0
    );

    private static Map<String, String> queryFilters = Map.of(
            "currentFilter", "",
            "bookInstanceVariable", "",
            "requestedEntityQueryColumn", "title");

    @FXML
    private void searchData(KeyEvent keyEvent) {

        var query = searchBar.getText().toLowerCase();
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            home();
            // Selected filter used to setup the query type and fields
            String selectedFilter = getSearchBoxSelectorValue();
            String bookInstanceVariable = "";
            String requestedQueryColumn = "";

            switch (selectedFilter.toLowerCase()) {
                case "author", "autor" -> {
                    bookInstanceVariable = "authors";
                    requestedQueryColumn = "authorName";
                }
                case "publisher", "editora" -> {
                    bookInstanceVariable = "publishers";
                    requestedQueryColumn = "publisherName";
                }
                case "category", "categoria" -> {
                    bookInstanceVariable = "categories";
                    requestedQueryColumn = "categoryName";
                }
                default -> {
                    requestedQueryColumn = "title";
                }
            }
            queryFilters =
                    Map.of(
                            "currentFilter", query,
                            "bookInstanceVariable", bookInstanceVariable,
                            "requestedEntityQueryColumn", requestedQueryColumn
                    );
            // update the current page state using the selected queryFilters (total of pages, previous and next buttons)
            updatePageAction(0, false);
        }
    }

    public void updatePageAction(int value, boolean setCurrentPage) {

        // update currentPage to next or previous
        // if setCurrentPage is true, the currentPage value will be set to the int value param
        // else the currentPage value will increase/decrease based upon the int value param
        pageConfig = UpdatePage.updateCurrentPage(pageConfig, value, setCurrentPage);
        // updates pages total information using queryFilters and pageConfig
        pageConfig = UpdatePage.getPagesAndRecordsCount(queryFilters, pageConfig);
        int totalPages = pageConfig.get("totalPages");
        int currentPage = pageConfig.get("currentPage");

        // returns the current page if true
        if (setCurrentPage || currentPage > totalPages) {
            pageConfig = UpdatePage.updateCurrentPage(pageConfig, totalPages, true);
            totalPages = pageConfig.get("totalPages");
            currentPage = pageConfig.get("currentPage");
        }

        // call CONTROLLER BOTTOM BAR and updates page buttons and info with updated information
        getMainController()
                .getBottomBarController()
                .updatePageAction(currentPage, totalPages);

        // query new data with selected filters
        updateResults();
    }

    // query data with queryFilters and pageConfig
    @FXML
    public void updateResults() {
        getMainController()
                .getTableViewController()
                .addItemToObservableList(
                            PaginatedQuery.query(
                                pageConfig.get("currentPage"),
                                pageConfig.get("itemsPerPage"),
                                queryFilters.get("currentFilter"),
                                queryFilters.get("bookInstanceVariable"),
                                queryFilters.get("requestedEntityQueryColumn")));
        getMainController().getTableViewController().updateTableViewList();
    }

    @FXML
    public void login() {
        Login.login(this);
        getMainController().setMainBorderPaneBottom(new VBox());
        getMainController().getLeftMenuController().setToggleButtonState(false);
    }

    // loads main default home Node
    @FXML
    public void home() {
        getMainController()
                .getTableViewController()
                .loadNode();
        getMainController().setMainBorderPaneBottom(getMainController().getBottomBarController().getBottomBarControllerHbox());
        getMainController().getLeftMenuController().setToggleButtonState(false);
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }

    @FXML
    public void changeTheme() {
        String selection = getThemeSelectorSelection();
        Main.changeTheme("css/" + selection + ".css");
    }

    public void searchBoxSelectorLanguage() {
        ObservableList<String> list = FXCollections.observableArrayList(
                ResourceBundleService.getString("Book"),
                ResourceBundleService.getString("Author"),
                ResourceBundleService.getString("Category"),
                ResourceBundleService.getString("Publisher")
        );

        this.searchBoxSelector.getItems().clear();
        this.searchBoxSelector.getItems().setAll(list);
        this.searchBoxSelector.setValue(list.getFirst());
    }

    // Loads this controller main Node to the Main Border Pane in main CONTROLLER
    public void loadNode() {
        searchBoxSelectorLanguage();
        Controller mainController = this.getMainController();
        mainController
                .setMainBorderPaneTop(
                    this
                        .getTopBarControllerVbox());
    }
}