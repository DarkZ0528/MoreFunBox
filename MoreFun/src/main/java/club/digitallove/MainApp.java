package club.digitallove;

import club.digitallove.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;

/**
 * @author DarkZ
 * @title: CenterMain
 * @projectName DigitalLoveClub
 * @description: TODO
 * @date 2020/7/2 16:44
 */
public class MainApp extends Application {
    private Stage mainStage;
    public static double width;
    public static double height;
    public static double taskBarHeight;

    public void start(Stage primaryStage) throws Exception {
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        width = screenRectangle.getWidth();
        height = screenRectangle.getHeight();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(new JFrame().getGraphicsConfiguration());
        taskBarHeight = screenInsets.bottom;
        mainStage = primaryStage;
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setResizable(false);
        mainStage.setTitle("磨坊盒子");
        mainStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Logo.png")));
        //System.out.println(this.getClass().getClassLoader().getResource("/"));
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.setApp(this);
        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getClassLoader().getResource("css/listview.css").toExternalForm());
        mainStage.setScene(scene);
        mainStage.setAlwaysOnTop(true);
        mainStage.setX(width - 300);
        mainStage.setY(height - (390 + taskBarHeight));
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getStage() {
        return mainStage;
    }
}
