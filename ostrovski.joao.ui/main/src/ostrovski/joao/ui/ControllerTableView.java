package ostrovski.joao.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import ostrovski.joao.common.helpers.DateTimeFormatters;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.model.dto.AuthorDTO;
import ostrovski.joao.common.model.dto.BookDTO;
import ostrovski.joao.common.model.dto.CategoryDTO;
import ostrovski.joao.common.model.dto.PublisherDTO;
import ostrovski.joao.ui.helpers.ResourceBundleService;

import java.util.stream.Collectors;

public class ControllerTableView {

    // Main Controller
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

    // Table View
    @FXML
    private TableView<BookDTO> tableView;

    public BookDTO getTableViewSelection() {
        return this.tableView
                .getSelectionModel()
                .getSelectedItem();
    }

    public BookDTO getLastTableViewSelection() {
        this.tableView
                .getSelectionModel()
                .selectLast();
        return this.tableView
                .getSelectionModel()
                .getSelectedItem();
    }

    @FXML
    private VBox tableViewControllerVbox;

    // FXML Table View Node
    public VBox getTableViewControllerVbox() {
        return tableViewControllerVbox;
    }

    // List to bind data to the Table View
    private final ObservableList<BookDTO> observableList = FXCollections.observableArrayList();

    // replaces the current items in the Table View binding
    public void updateTableViewList() {
        this.tableView.setItems(this.observableList);
    }

    // replaces the current items in the ObservableList
    public void addItemToObservableList(ObservableList<BookDTO> items) {
        this.observableList.setAll(items);
    }

    // add new item to ObservableList
    public void addItemToObservableList(BookDTO item) {
        this.observableList.add(item);
    }

    // Removes item from ObservableList
    public void removeItemFromObservableList(BookDTO item) {
        this.observableList.remove(item);
    }

    // Removes item from ObservableList by id
    public void removeFromObservableListById(int id) {
        this.observableList.removeIf(book -> book.bookId() == id);
    }

    // Updates Table View current state
    @FXML
    public void changeTableView() {

        this.tableView.getColumns().clear();

        TableColumn<BookDTO, String> colTitle = new TableColumn<>(ResourceBundleService.getString("Title"));
        colTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().title()));

        TableColumn<BookDTO, String> colDescription = new TableColumn<>(ResourceBundleService.getString("release_date"));
        colDescription.setCellValueFactory(c -> {
            if (c.getValue().releaseDate() != null) {
                return new SimpleStringProperty(DateTimeFormatters.getFormattedLocalDateWithLocale(c.getValue().releaseDate()));
            }
            return null;
        });

        TableColumn<BookDTO, String> colAuthor = new TableColumn<>(ResourceBundleService.getString("Author"));
        colAuthor.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().authors().stream()
                        .map(AuthorDTO::authorName)
                        .collect(Collectors.joining(", "))
        ));

        TableColumn<BookDTO, String> colPublisher = new TableColumn<>(ResourceBundleService.getString("Publisher"));
        colPublisher.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().publishers().stream()
                        .map(PublisherDTO::publisherName)
                        .collect(Collectors.joining(", "))
        ));

        TableColumn<BookDTO, String> colCategory = new TableColumn<>(ResourceBundleService.getString("Category"));
        colCategory.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().categories().stream()
                        .map(CategoryDTO::categoryName)
                        .collect(Collectors.joining(", "))
        ));

        this.tableView
                .getColumns()
                .addAll(
                        colTitle, colDescription, colAuthor, colPublisher, colCategory
                );
    }

    // Loads this controller Main Node to the Main Border Pane in main CONTROLLER
    public void loadNode() {
        Controller mainController = this.getMainController();
        mainController
                .setMainBorderPaneCenter(
                    this
                        .getTableViewControllerVbox());
    }
}