package ostrovski.joao.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    private Controller mainController;
    private ControllerBottomBar bottomBarController;
    private ControllerDetailedView detailedViewController;
    private ControllerLeftMenu leftMenuController;
    private ControllerLogin loginController;
    private ControllerTableView tableViewController;
    private ControllerTopBar topBarController;
    private ControllerUpsert upsertController;

    // Controller
    public Controller getMainController() {
        return mainController;
    }

    public void setMainController(Controller mainController) {
        if (mainController == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        this.mainController = mainController;
    }

    // ControllerBottomBar
    public ControllerBottomBar getBottomBarController() {
        return bottomBarController;
    }

    public void setBottomBarController(ControllerBottomBar bottomBarController) {
        if (bottomBarController == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        this.bottomBarController = bottomBarController;
    }

    // ControllerLeftMenu
    public ControllerLeftMenu getLeftMenuController() {
        return leftMenuController;
    }

    public void setLeftMenuController(ControllerLeftMenu leftMenuController) {
        if (leftMenuController == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        this.leftMenuController = leftMenuController;
    }

    // ControllerLogin
    public ControllerLogin getLoginController() {
        return loginController;
    }

    public void setLoginController(ControllerLogin loginController) {
        if (loginController == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        this.loginController = loginController;
    }

    // ControllerTableView
    public ControllerTableView getTableViewController() {
        return tableViewController;
    }

    public void setTableViewController(ControllerTableView tableViewController) {
        if (tableViewController == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        this.tableViewController = tableViewController;
    }

    // ControllerTopBar
    public ControllerTopBar getTopBarController() {
        return topBarController;
    }

    public void setTopBarController(ControllerTopBar topBarController) {
        if (topBarController == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        this.topBarController = topBarController;
    }

    // ControllerUpsert
    public ControllerUpsert getUpsertController() {
        return upsertController;
    }

    public void setUpsertController(ControllerUpsert upsertController) {
        if (upsertController == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_CONTROLLER.getMessage()));
            return;
        }
        this.upsertController = upsertController;
    }

    // Main Border Pane
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private VBox mainFrameTop;
    @FXML
    private VBox mainFrameLeft;
    @FXML
    private VBox mainFrameCenter;
    @FXML
    private VBox mainFrameBottom;

    @FXML
    public Window getMainBorderPaneScene() {
        return this.mainBorderPane.getScene().getWindow();
    }

    public void setMainBorderPaneTop(Node node) {
        if (node == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_NODE.getMessage()));
            return;
        }
        mainFrameTop.getChildren().clear();
        mainFrameTop.getChildren().add(node);
    }

    public void setMainBorderPaneRight(Node node) {
        if (node == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_NODE.getMessage()));
            return;
        }
        System.out.println("Border Pane Right is out of use");
    }

    public void setMainBorderPaneCenter(Node node) {
        if (node == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_NODE.getMessage()));
            return;
        }
        mainFrameCenter.getChildren().clear();
        mainFrameCenter.getChildren().add(node);
    }

    public void setMainBorderPaneBottom(Node node) {
        if (node == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_NODE.getMessage()));
            return;
        }
        mainFrameBottom.getChildren().clear();
        mainFrameBottom.getChildren().add(node);
    }

    public void setMainBorderPaneLeft(Node node) {
        if (node == null) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_NODE.getMessage()));
            return;
        }
        mainFrameLeft.getChildren().clear();
        mainFrameLeft.getChildren().add(node);
    }

    @FXML
    public void initialize() {}
}
