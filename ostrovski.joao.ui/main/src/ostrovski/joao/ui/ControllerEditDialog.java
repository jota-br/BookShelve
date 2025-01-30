package ostrovski.joao.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.model.dto.*;
import ostrovski.joao.services.BookService;
import ostrovski.joao.services.EditDialogService;
import ostrovski.joao.ui.helpers.DialogResult;
import ostrovski.joao.ui.helpers.GetAlert;
import ostrovski.joao.ui.helpers.ResourceBundleService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ControllerEditDialog {

    // main CONTROLLER
    private Controller mainController;

    public void setMainController(Controller controller) {
        if (controller == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }

        this.mainController = controller;
    }

    private int flag = 0;

    // main VBox
    @FXML
    private VBox editDialogVbox;

    // select list
    @FXML
    private VBox selectEditDialogVbox;
    @FXML
    private ChoiceBox<String> selectEntityEditDialog;

    // author fields
    @FXML
    private TextField authorNameField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private TextField authorCountryNameField;

    // category fields
    @FXML
    private TextField categoryNameField;

    // publisher fields
    @FXML
    private TextField publisherNameField;
    @FXML
    private TextField publisherCountryNameField;

    private int editingId;

    public void updateTableView() {
        int bookId = this.mainController.getTableViewController().getTableViewSelection().bookId();
        BookDTO book = BookService.findBookByID(bookId);
        this.mainController.getTableViewController().removeFromObservableListById(bookId);
        this.mainController.getTableViewController().addItemToObservableList(book);
        this.mainController.getTableViewController().getLastTableViewSelection();
        this.mainController.getLeftMenuController().openDetailedView(book);
    }

    public void updateVbox(String fxml) {
        if (fxml == null || fxml.isEmpty()) {
            Logger.log(new IllegalArgumentException(ExceptionMessage.ILLEGAL_PARAM.getMessage()));
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            loader.setController(this);
            Node node = loader.load();
            this.editDialogVbox.getChildren().clear();
            this.editDialogVbox.getChildren().add(node);
        } catch (IOException e) {
            Logger.log(e);
        }
    }

    // Author
    public void handleOkButtonAuthorEdit(ActionEvent event, DialogResult dialogResult, List<AuthorDTO> list) {

        switch (this.flag) {
            case 0 -> {
                this.flag++;
                authorEditDialogResult(list);
            }
            case 1 -> {
                this.flag++;
                processAuthorData();
                dialogResult.getDialog().close();
            }
        }
        event.consume();
    }

    public void loadEditDialogList() {
        updateVbox("editModules/entitySelector.fxml");
    }

    public boolean loadSelectAuthorEditDialog(List<AuthorDTO> list) {
        if (list == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return false;
        }

        loadEditDialogList();
        if (!this.selectEntityEditDialog.getItems().isEmpty()) {
            this.selectEntityEditDialog.getItems().clear();
        }

        List<String> names = list.stream().map(AuthorDTO::authorName).toList();
        if (names.isEmpty()) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return false;
        }
        this.selectEntityEditDialog.setItems(FXCollections.observableArrayList(names));
        this.selectEntityEditDialog.setValue(names.getFirst());
        return true;
    }

    public void authorEditDialogResult(List<AuthorDTO> list) {
        String selection = this.selectEntityEditDialog.getSelectionModel().selectedItemProperty().get();

        if (selection != null && (!selection.isEmpty())) {
            int index = 0;
            for (AuthorDTO author : list) {
                if (author.authorName().equals(selection)) {
                    index = list.indexOf(author);
                    break;
                }
            }
            if (index > -1) {
                AuthorDTO selectedAuthor = list.get(index);
                this.loadAuthorEditDialog(selectedAuthor);
                return;
            }
        }
        Logger.log(new IllegalStateException(ExceptionMessage.METHOD_FAILURE.getMessage()));
    }

    public void loadAuthorEditDialog(AuthorDTO author) {
        if (author == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return;
        }
        updateVbox("editModules/authorFields.fxml");
        this.authorNameField.setText(author.authorName());
        this.dateOfBirthField.setValue(author.dateOfBirth() != null ? LocalDate.parse(author.dateOfBirth().toString()) : LocalDate.now().minusYears(50));
        this.authorCountryNameField.setText(author.country() != null ? author.country().countryName() : "");
        this.editingId = author.authorId();

    }

    public void processAuthorData() {
        String name = this.authorNameField.getText();
        String country = this.authorCountryNameField.getText();
        String dateOfBirth = String.valueOf(this.dateOfBirthField.getValue());

        AuthorDTO author = EditDialogService.processAuthorsEditDialogData(this.editingId, name, dateOfBirth, country);
        if (author == null) {
            GetAlert.getInformationAlert(ResourceBundleService.getString("duplicate_name"),
                    ResourceBundleService.getString("Author") + " " + name + " " + ResourceBundleService.getString("already_exists"),
                    ResourceBundleService.getString("only_unique_names_are_accepted"));
        } else {
            updateTableView();
        }
    }

    // Publisher
    public void handleOkButtonPublisherEdit(ActionEvent event, DialogResult dialogResult, List<PublisherDTO> list) {

        switch (this.flag) {
            case 0 -> {
                this.flag++;
                publisherEditDialogResult(list);
            }
            case 1 -> {
                this.flag++;
                processPublisherData();
                dialogResult.getDialog().close();
            }
        }
        event.consume();
    }

    public boolean loadSelectPublisherEditDialog(List<PublisherDTO> list) {
        if (list == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return false;
        }

        loadEditDialogList();
        if (!this.selectEntityEditDialog.getItems().isEmpty()) {
            this.selectEntityEditDialog.getItems().clear();
        }

        List<String> names = list.stream().map(PublisherDTO::publisherName).toList();
        if (names.isEmpty()) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return false;
        }
        this.selectEntityEditDialog.setItems(FXCollections.observableArrayList(names));
        this.selectEntityEditDialog.setValue(names.getFirst());
        return true;
    }

    public void publisherEditDialogResult(List<PublisherDTO> list) {
        String selection = this.selectEntityEditDialog.getSelectionModel().selectedItemProperty().get();

        if (selection != null && (!selection.isEmpty())) {
            int index = 0;
            for (PublisherDTO publisher : list) {
                if (publisher.publisherName().equals(selection)) {
                    index = list.indexOf(publisher);
                    break;
                }
            }
            if (index > -1) {
                PublisherDTO selectedPublisher = list.get(index);
                this.loadPublisherEditDialog(selectedPublisher);
                return;
            }
        }
        Logger.log(new IllegalStateException(ExceptionMessage.METHOD_FAILURE.getMessage()));
    }

    public void loadPublisherEditDialog(PublisherDTO publisher) {
        if (publisher == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return;
        }
        updateVbox("editModules/publisherFields.fxml");
        this.publisherNameField.setText(publisher.publisherName());
        this.publisherCountryNameField.setText(publisher.country() != null ? publisher.country().countryName() : "");
        this.editingId = publisher.publisherId();

    }

    public void processPublisherData() {
        String name = this.publisherNameField.getText();
        String country = this.publisherCountryNameField.getText();

        PublisherDTO publisher = EditDialogService.processPublishersEditDialogData(this.editingId, name, country);
        if (publisher == null) {
            GetAlert.getInformationAlert(ResourceBundleService.getString("duplicate_name"),
                    ResourceBundleService.getString("Publisher") + " " + name + " " + ResourceBundleService.getString("already_exists"),
                    ResourceBundleService.getString("only_unique_names_are_accepted"));
        } else {
            updateTableView();
        }
    }

    // Category
    public void handleOkButtonCategoryEdit(ActionEvent event, DialogResult dialogResult, List<CategoryDTO> list) {

        switch (this.flag) {
            case 0 -> {
                this.flag++;
                categoryEditDialogResult(list);
            }
            case 1 -> {
                this.flag++;
                processCategoryData();
                dialogResult.getDialog().close();
            }
        }
        event.consume();
    }

    public boolean loadSelectCategoryEditDialog(List<CategoryDTO> list) {
        if (list == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return false;
        }

        loadEditDialogList();
        if (!this.selectEntityEditDialog.getItems().isEmpty()) {
            this.selectEntityEditDialog.getItems().clear();
        }

        List<String> names = list.stream().map(CategoryDTO::categoryName).toList();
        if (names.isEmpty()) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_VAR_RETURN.getMessage()));
            return false;
        }
        this.selectEntityEditDialog.setItems(FXCollections.observableArrayList(names));
        this.selectEntityEditDialog.setValue(names.getFirst());
        return true;
    }

    public void categoryEditDialogResult(List<CategoryDTO> list) {
        String selection = this.selectEntityEditDialog.getSelectionModel().selectedItemProperty().get();

        if (selection != null && (!selection.isEmpty())) {
            int index = 0;
            for (CategoryDTO category : list) {
                if (category.categoryName().equals(selection)) {
                    index = list.indexOf(category);
                    break;
                }
            }
            if (index > -1) {
                CategoryDTO selectedCategory = list.get(index);
                this.loadCategoryEditDialog(selectedCategory);
                return;
            }
        }
        Logger.log(new IllegalStateException(ExceptionMessage.METHOD_FAILURE.getMessage()));
    }

    public void loadCategoryEditDialog(CategoryDTO category) {
        if (category == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return;
        }
        updateVbox("editModules/categoryFields.fxml");
        this.categoryNameField.setText(category.categoryName());
        this.editingId = category.categoryId();

    }

    public void processCategoryData() {
        String name = this.categoryNameField.getText();

        CategoryDTO updatedCategory = EditDialogService.processCategoriesEditDialogData(this.editingId, name);
        if (updatedCategory == null) {
            GetAlert.getInformationAlert(ResourceBundleService.getString("duplicate_name"),
                    ResourceBundleService.getString("Category") + " " + name + " " + ResourceBundleService.getString("already_exists"),
                    ResourceBundleService.getString("only_unique_names_are_accepted"));
        } else {
            updateTableView();
        }
    }

    // Country 
    // Need to evaluate how to conact all countries from authors and publishers to display in the Choice Box
    // and how to retrieve it to edit
//    public void loadCountryEditDialog(CountryDTO country) {
//        if (country == null) {
//            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
//            return;
//        }
//
//        updateVbox("editModules/countryFields.fxml");
//        this.countryNameField.setText(country.countryName());
//        this.editingId = country.countryId();
//    }
}