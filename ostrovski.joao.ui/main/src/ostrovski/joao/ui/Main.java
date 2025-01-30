package ostrovski.joao.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.db.helpers.DatabaseInitialization;
import ostrovski.joao.ui.helpers.ResourceBundleService;

import java.util.concurrent.TimeUnit;

public class Main extends Application {

    public static Scene mainScene;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader mainLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(mainLoader.load());
        stage.setMaximized(true);

        mainLoader.setResources(ResourceBundleService.getResourceBundle());

        mainScene = scene;

        Controller mainController = mainLoader.getController();

        FXMLLoader tableViewLoader = new FXMLLoader(getClass().getResource("tableView.fxml"));
        Node tableView = tableViewLoader.load();
        tableViewLoader.setResources(ResourceBundleService.getResourceBundle());
        ControllerTableView tableViewController = tableViewLoader.getController();
        tableViewController.setMainController(mainController);

        FXMLLoader topBarLoader = new FXMLLoader(getClass().getResource("topBar.fxml"));
        topBarLoader.setResources(ResourceBundleService.getResourceBundle());
        Node topBar = topBarLoader.load();
        ControllerTopBar topBarController = topBarLoader.getController();
        topBarController.setMainController(mainController);

        FXMLLoader leftMenuLoader = new FXMLLoader(getClass().getResource("leftMenu/leftMenu.fxml"));
        leftMenuLoader.setResources(ResourceBundleService.getResourceBundle());
        Node leftMenu = leftMenuLoader.load();
        ControllerLeftMenu leftMenuController = leftMenuLoader.getController();
        leftMenuController.setMainController(mainController);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        loginLoader.setResources(ResourceBundleService.getResourceBundle());
        loginLoader.load();
        ControllerLogin loginController = loginLoader.getController();
        loginController.setMainController(mainController);

        FXMLLoader upsertLoader = new FXMLLoader(getClass().getResource("upsert.fxml"));
        upsertLoader.setResources(ResourceBundleService.getResourceBundle());
        upsertLoader.load();
        ControllerUpsert upsertController = upsertLoader.getController();
        upsertController.setMainController(mainController);

        FXMLLoader bottomBarLoader = new FXMLLoader(getClass().getResource("bottomBar.fxml"));
        bottomBarLoader.setResources(ResourceBundleService.getResourceBundle());
        Node bottomBar = bottomBarLoader.load();
        ControllerBottomBar bottomBarController = bottomBarLoader.getController();
        bottomBarController.setMainController(mainController);

        mainController.setTableViewController(tableViewController);
        mainController.setTopBarController(topBarController);
        mainController.setLeftMenuController(leftMenuController);
        mainController.setLoginController(loginController);
        mainController.setBottomBarController(bottomBarController);
        mainController.setUpsertController(upsertController);

        topBarController.loadNode();
        bottomBarController.loadNode();
        tableViewController.loadNode();
        leftMenuController.loadNode();

        tableViewController.changeTableView();
        topBarController.updateResults();
        topBarController.updatePageAction(0, false);

        stage.setTitle("Book Shelve v0.233");
        stage.setScene(scene);
        Application.setUserAgentStylesheet(getClass().getResource("css/nord-light.css").toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        boolean isClosed = Controller.getExecutorService().awaitTermination(5000, TimeUnit.MILLISECONDS);
        if (!isClosed) {
            System.out.println(Controller.getExecutorService().shutdownNow());
        }
    }

    @Override
    public void init() throws Exception {
        DatabaseInitialization.checkSchema();
    }

    public static void changeTheme(String path) {
        if (path == null || path.isEmpty()) {
            Logger.log(new NullPointerException(ExceptionMessage.NULL_PARAM.getMessage()));
            return;
        }

        mainScene.getStylesheets().clear();
        mainScene.getStylesheets().add(Main.class.getResource(path).toExternalForm());
    }
}
