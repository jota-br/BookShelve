package ostrovski.joao.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.model.dto.AuthorDTO;
import ostrovski.joao.common.model.dto.BookDTO;
import ostrovski.joao.common.model.dto.CategoryDTO;
import ostrovski.joao.common.model.dto.PublisherDTO;
import ostrovski.joao.ui.helpers.ResourceBundleService;

import java.util.stream.Collectors;

public class ControllerDetailedView {

    private BookDTO data;

    public void setDetailedViewItem(BookDTO data) {
        this.data = data;
    }

    public BookDTO getData() {
        return data;
    }

    @FXML
    private VBox bookFields;
    @FXML
    private Label bookTitleField;
    @FXML
    private Label bookReleaseDateField;
    @FXML
    private Label bookDescriptionField;
    @FXML
    private Label bookCategoriesField;

    @FXML
    private VBox publishersField;
    @FXML
    private VBox authorsField;

    @FXML
    public void setFieldValues(BookDTO data) {
        if (data == null) {
                Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
                return;
        }
        
        setDetailedViewItem(data);
        // set values to the DETAILED VIEW fields
        bookTitleField.setText(data.title());

        String releaseDate = data.releaseDate() != null ? data.releaseDate().toString() : "";
        HBox hbox = new HBox();
        Label labelReleaseDate = new Label();
        labelReleaseDate.setText(ResourceBundleService.getString("release_date") + ": ");
        labelReleaseDate.getStyleClass().add("labelSubTitle");

        Label labelReleaseDateData = new Label();
        labelReleaseDateData.setText(releaseDate);
        labelReleaseDateData.getStyleClass().add("labelTitle");
        hbox.getChildren().addAll(labelReleaseDate, labelReleaseDateData);
        bookFields.getChildren().add(hbox);

        hbox = new HBox();
        Label labelDescription = new Label();
        labelDescription.setText(ResourceBundleService.getString("Description") + ": ");
        labelDescription.getStyleClass().add("labelSubTitle");

        Label labelDescriptionData = new Label();
        labelDescriptionData.setText(data.description());
        labelDescriptionData.getStyleClass().add("labelTitle");
        hbox.getChildren().addAll(labelDescription, labelDescriptionData);
        bookFields.getChildren().add(hbox);

        String categories =
        data.categories().stream()
                .map(CategoryDTO::categoryName)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));
        Label labelCategoriesData = new Label();
        labelDescription.setText(ResourceBundleService.getString("Category") + ": ");
        labelDescription.getStyleClass().add("labelSubTitle");
        bookFields.getChildren().add(labelCategoriesData);

        for (PublisherDTO publisher : data.publishers()) {
            String name = publisher.publisherName();
            String country = publisher.country() != null ? publisher.country().countryName() : "";

            hbox = new HBox();
            Label labelName = new Label();
            labelName.setText(ResourceBundleService.getString("Publisher") + ": ");
            labelName.getStyleClass().add("labelSubTitle");

            Label labelNameData = new Label();
            labelNameData.setText(name);
            labelNameData.getStyleClass().add("labelTitle");
            hbox.getChildren().addAll(labelName, labelNameData);
            publishersField.getChildren().add(hbox);

            hbox = new HBox();
            Label labelCountry = new Label();
            labelCountry.setText(ResourceBundleService.getString("Country") + ": ");
            labelCountry.getStyleClass().add("labelSubTitle");

            Label labelNCountryData = new Label();
            labelNCountryData.setText(country);
            labelNCountryData.getStyleClass().add("labelTitle");
            hbox.getChildren().addAll(labelCountry, labelNCountryData);
            publishersField.getChildren().add(hbox);
        }

        for (AuthorDTO author : data.authors()) {
            String name = author.authorName();
            String dateOfBirth = author.dateOfBirth() != null ? author.dateOfBirth().toString() : "";
            String country = author.country() != null ? author.country().countryName() : "";

            hbox = new HBox();
            Label labelNameData = new Label();
            labelNameData.setText(name);
            labelNameData.getStyleClass().add("labelTitle");

            Label labelDob = new Label();
            labelDob.setText(ResourceBundleService.getString("date_of_birth") + ": ");
            labelDob.getStyleClass().add("labelSubTitle");

            Label labelDobData = new Label();
            labelDobData.setText(dateOfBirth);
            labelDobData.getStyleClass().add("labelTitle");
            hbox.getChildren().addAll(labelDob, labelDobData);
            authorsField.getChildren().addAll(labelNameData, hbox);

            hbox = new HBox();
            Label labelCountry = new Label();
            labelCountry.setText(ResourceBundleService.getString("Country") + ": ");
            labelCountry.getStyleClass().add("labelSubTitle");

            Label labelCountryData = new Label();
            labelCountryData.setText(country);
            labelCountryData.getStyleClass().add("labelTitle");
            hbox.getChildren().addAll(labelCountry, labelCountryData);
            authorsField.getChildren().add(hbox);
        }
    }
}
