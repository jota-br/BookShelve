package ostrovski.joao.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.ui.helpers.ResourceBundleService;

public class ControllerBottomBar {

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
    private HBox bottomBarControllerHbox;

    public HBox getBottomBarControllerHbox() {
        return bottomBarControllerHbox;
    }

    @FXML
    private Label paginationDetails;
    @FXML
    private FlowPane paginationButtonNext;
    @FXML
    private FlowPane paginationButtonPrevious;

    // call method in TOP BAR CONTROLLER to get queryFilters and pageConfig
    @FXML
    public void handlePageButton(int value) {
        // increases current page number by the value
        // 1 for next || -1 for previous button OnAction
        getMainController()
                .getTopBarController()
                .updatePageAction(value, false); // false = current page will be increased by the value instead of set TO THE value
    }

    public void updatePageAction(int currentPage, int totalPages) {
        if (currentPage <= 0 || totalPages <= 0) {
            Logger.log(new IllegalArgumentException(ExceptionMessage.ILLEGAL_NUM_PARAM.getMessage()));
            return;
        }
        // called by the TOP BAR CONTROLLER with current and total pages information
        // updates the bottom of the MAIN BORDER PANE in the main CONTROLLER with pagination information
        paginationDetails.setText(
                ResourceBundleService.getString("Page")
                        + " " +
                        currentPage +
                        ResourceBundleService.getString("of")
                        + " " +
                        totalPages);

        // insert NEXT button
        if (currentPage < totalPages) {
            ImageView nextButtonImage = new ImageView(new Image(getClass().getResourceAsStream("imgs/Forward24.gif")));
            Button nextButton = new Button(null, nextButtonImage);
            nextButton.setOnAction(event -> handlePageButton(1));
            paginationButtonNext.getChildren().clear();
            paginationButtonNext.getChildren().add(nextButton);
        } else {
            paginationButtonNext.getChildren().clear();
        }

        // insert PREVIOUS button
        if (currentPage > 1) {
            ImageView previousButtonImage = new ImageView(new Image(getClass().getResourceAsStream("imgs/Back24.gif")));
            Button previousButton = new Button(null, previousButtonImage);
            previousButton.setOnAction(event -> handlePageButton(-1));
            paginationButtonPrevious.getChildren().clear();
            paginationButtonPrevious.getChildren().add(previousButton);
        } else {
            paginationButtonPrevious.getChildren().clear();
        }
    }

    // loads this controller in the main CONTROLLER -> BORDER PANE -> BOTTOM
    public void loadNode() {
        if (this.getMainController() == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        Controller mainController = this.getMainController();
        mainController
                .setMainBorderPaneBottom(
                        this
                                .getBottomBarControllerHbox());
    }
}