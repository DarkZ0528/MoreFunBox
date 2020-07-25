package club.digitallove.util.urlDecoder;

import javafx.scene.control.Alert;

public class DialogeBuilderForJDK11 {
    public static void dialog(String titile,String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titile);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
