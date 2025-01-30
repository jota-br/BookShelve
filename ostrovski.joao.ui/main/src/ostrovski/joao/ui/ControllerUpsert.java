package ostrovski.joao.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.model.dto.AuthorDTO;
import ostrovski.joao.common.model.dto.BookDTO;
import ostrovski.joao.common.model.dto.CategoryDTO;
import ostrovski.joao.common.model.dto.PublisherDTO;
import ostrovski.joao.services.UpsertService;
import ostrovski.joao.ui.helpers.GetAlert;
import ostrovski.joao.ui.helpers.ResourceBundleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ControllerUpsert {

    private boolean flag = false;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

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
    private VBox upsertControllerVbox;

    public VBox getUpsertControllerVbox() {
        return this.upsertControllerVbox;
    }

    @FXML
    private VBox bookVbox;
    @FXML
    private VBox publisherVbox;
    @FXML
    private VBox authorVbox;

    @FXML
    private TextField titleField;
    @FXML
    private DatePicker releaseDateField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField categoryNameField;

    @FXML
    private ChoiceBox<String> publisherList;
    @FXML
    private TextField publisherNameField;
    @FXML
    private TextField publisherCountryField;

    @FXML
    private ChoiceBox<String> authorList;
    @FXML
    private TextField authorNameField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private TextField authorCountryNameField;

    @FXML
    public void datePickerSetField(DatePicker instance, TextField textField) {
        String currentField = textField.getText();
        if (currentField.isEmpty() || textField.getId().equals("releaseDateField")) {
            textField.setText(String.valueOf(instance.getValue()));
        } else {
            String newValue = String.join(",", currentField, String.valueOf(instance.getValue()));
            textField.setText(newValue);
        }
    }

    public void removeSelectedPublisher() {
        removeSelectionFromList(this.publisherList, ResourceBundleService.getString("remove_publisher_from_list"));
    }

    public void removeSelectedAuthor() {
        removeSelectionFromList(this.authorList, ResourceBundleService.getString("remove_author_from_list"));
    }

    public void removeSelectionFromList(ChoiceBox<String> list, String title) {
        if (list != null && list.getSelectionModel().getSelectedItem() != null) {
            String selection = list.getSelectionModel().getSelectedItem();
            Optional<ButtonType> result = GetAlert.getConfirmationAlert(title,
                    ResourceBundleService.getString("Remove")
                            + " " + selection + " " +
                            ResourceBundleService.getString("from_list_confirmation"),
                    ResourceBundleService.getString("click_ok_to_confirm"));
            if (result.isPresent() && result.get().getButtonData().isDefaultButton()) {
                list.getItems().remove(selection);
                list.getSelectionModel().selectFirst();
            }
        }
    }

    public void addNewItemToList(ChoiceBox<String> list, String item, String name, String header) {
        ObservableList<String> currentList = list.getItems();

        for (String string : list.getItems()) {
            if (string.contains(name)) {
                GetAlert.getInformationAlert(
                        ResourceBundleService.getString("diplicate_name"),
                        header,
                        ResourceBundleService.getString("only_unique_names_are_accepted"));
                return;
            }
        }
        currentList.add(item);

        Set<String> uniqueSet = new TreeSet<>(currentList);

        ObservableList<String> uniqueList = FXCollections.observableArrayList(uniqueSet);

        list.setItems(uniqueList);
        list.getSelectionModel().selectLast();
    }

    public void addPublisher() {
        String name = this.publisherNameField.getText();
        if (name == null || name.isEmpty()) {
            GetAlert.getInformationAlert(
                    ResourceBundleService.getString("missing_required_field"),
                    ResourceBundleService.getString("publisher_name_missing"),
                    ResourceBundleService.getString("insert_required_fields_to_proceed"));
            return;
        }

        String country = !this.publisherCountryField.getText().isEmpty() ? this.publisherCountryField.getText() : "_";

        StringBuilder publisherString = new StringBuilder();
        publisherString.append("[")
                .append(name)
                .append(", ")
                .append(country)
                .append("]");

        addNewItemToList(publisherList, publisherString.toString(), name,
                ResourceBundleService.getString("name")
                        + "(" + name + ")" +
                        ResourceBundleService.getString("found_in_list"));
    }

    public void addAuthor() {
        String name = authorNameField.getText();
        if (name == null || name.isEmpty()) {
            GetAlert.getInformationAlert(
                    ResourceBundleService.getString("missing_required_field"),
                    ResourceBundleService.getString("author_name_missing"),
                    ResourceBundleService.getString("insert_required_fields_to_proceed"));
            return;
        }

        String dob = dateOfBirthField.getValue() != null ? dateOfBirthField.getValue().toString() : "_";
        String country = !authorCountryNameField.getText().isEmpty() ? authorCountryNameField.getText() : "_";

        StringBuilder authorString = new StringBuilder();
        authorString.append("[")
                .append(name)
                .append(", ")
                .append(dob)
                .append(", ")
                .append(country)
                .append("]");

        addNewItemToList(authorList, authorString.toString(), name,
                ResourceBundleService.getString("name")
                        + "(" + name + ")" +
                        ResourceBundleService.getString("found_in_list"));
    }

    @FXML
    public void saveBook() {
        BookDTO selection = this.mainController
                .getLeftMenuController()
                .getTableViewSelection();

        // if flag == true = editing book
        try {
            if (flag) {
                processResult(selection, this::handleBookResult);
            } else {
                processResult(null, this::handleBookResult);
            }
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public void handleBookResult(BookDTO newBook) {
        if (newBook == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return;
        }

        this.mainController.getTableViewController().removeFromObservableListById(newBook.bookId());
        this.mainController.getTableViewController().addItemToObservableList(newBook);
        this.mainController.getTableViewController().getLastTableViewSelection();

        this.mainController
                .getLeftMenuController()
                .openDetailedView(newBook);
    }

    public void processResult(BookDTO oldBook, Consumer<BookDTO> callback) {

        if (titleField.getText().isEmpty()) {
            GetAlert.getInformationAlert(
                    ResourceBundleService.getString("missing_required_field"),
                    ResourceBundleService.getString("book_title_missing"),
                    ResourceBundleService.getString("insert_required_fields_to_proceed"));
            return;
        }

        Callable<BookDTO> task = getBookDTOCallable(oldBook);

        Controller.getExecutorService().submit(() -> {
            BookDTO result = null;
            try {
                result = task.call();
                BookDTO finalResult = result;
                Platform.runLater(() -> callback.accept(finalResult));
            } catch (Exception e) {
                Logger.log(e);
                Platform.runLater(() -> callback.accept(null));
            }
        });
    }

    private Callable<BookDTO> getBookDTOCallable(BookDTO oldBook) {
        String title = this.titleField.getText();
        String releaseDate = this.releaseDateField.getValue() != null ? this.releaseDateField.getValue().toString() : "";
        String description = this.descriptionField.getText();
        String categoriesString = this.categoryNameField.getText();
        List<String> authorList = this.authorList.getItems();
        List<String> publisherList = this.publisherList.getItems();


        // process upsert data and returns BookDTO object
        Callable<BookDTO> task = () ->
            UpsertService.processUpsertData(title, releaseDate, description, categoriesString, publisherList, authorList, oldBook);
        return task;
    }

    // set field values for editing books when upsert.fxml is loaded
    public void setFieldsValues(BookDTO data) {
        this.titleField.setText(data.title());

        this.releaseDateField.setValue(data.releaseDate() != null ?
                data.releaseDate() : LocalDate.now());

        this.descriptionField.setText(data.description());

        this.categoryNameField.setText(data.categories().stream()
                .map(CategoryDTO::categoryName)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", ")));

        for (AuthorDTO author : data.authors()) {
            if (author == null) {
                continue;
            }

            String name = author.authorName();
            String dob = author.dateOfBirth() != null ? author.dateOfBirth().toString() : "_";
            String country = author.country() != null ? author.country().countryName() : "_";

            StringBuilder authorString = new StringBuilder();
            authorString.append("[")
                    .append(name)
                    .append(", ")
                    .append(dob)
                    .append(", ")
                    .append(country)
                    .append("]");
            authorList.getItems().add(authorString.toString());
        }
        authorList.getSelectionModel().selectLast();

        for (PublisherDTO publisher : data.publishers()) {
            if (publisher == null) {
                continue;
            }

            String name = publisher.publisherName();
            String country = publisher.country() != null ? publisher.country().countryName() : "_";

            StringBuilder publisherString = new StringBuilder();
            publisherString.append("[")
                    .append(name)
                    .append(", ")
                    .append(country)
                    .append("]");
            this.publisherList.getItems().add(publisherString.toString());
        }
        this.publisherList.getSelectionModel().selectLast();
    }

    public void resetFields() {
        this.titleField.clear();
        this.descriptionField.clear();
        this.categoryNameField.clear();
        this.publisherNameField.clear();
        this.publisherCountryField.clear();
        this.authorNameField.clear();
        this.authorCountryNameField.clear();
        this.publisherList.getItems().clear();
        this.authorList.getItems().clear();

    }

    public void loadNode() {
        resetFields();
        Controller mainController = this.getMainController();
        mainController
                .setMainBorderPaneCenter(
                        this
                                .getUpsertControllerVbox());
    }
}
