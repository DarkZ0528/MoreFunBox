package club.digitallove.controller;

import club.digitallove.MainApp;
import club.digitallove.consts.LiveUrls;
import club.digitallove.util.DialogBuilder;
import club.digitallove.util.DragUtil;
import club.digitallove.util.urlDecoder.*;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Pair;
import netscape.javascript.JSObject;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private MainApp myApp;
    @FXML
    private Button aboutBtn;
    @FXML
    private Label moveBtn;
    @FXML
    private ImageView videoView;
    @FXML
    private WebView webView;
    @FXML
    private JFXToggleButton enableSound;
    @FXML
    private VBox optWin;
    @FXML
    private JFXTextField videoSrc;

    private MediaPlayerFactory mediaPlayerFactory;
    private EmbeddedMediaPlayer embeddedMediaPlayer;
    private WebEngine webEngine;
    private JSObject win;

    public void setApp(MainApp app) {
        this.myApp = app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mediaPlayerFactory = new MediaPlayerFactory();
        embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        videoView.setPreserveRatio(true);
        embeddedMediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(videoView));
        this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
            }
        });
        webEngine = webView.getEngine();
        String url = getClass().getClassLoader().getResource("video.html").toExternalForm();
        webEngine.load(url);
        webEngine.getLoadWorker().stateProperty().addListener(
                (ObservableValue<? extends Worker.State> ov, Worker.State oldState,
                 Worker.State newState) -> {
                    if (newState == Worker.State.SUCCEEDED) {
                        win = (JSObject) webEngine.executeScript("window");
                        win.setMember("jsUtil", jsUtil);//设置变量
                    }
                });
    }

    @FXML
    private void start() {
        start(UrlUtil.GetVideoType(videoSrc.getText()), videoSrc.getText());
    }

    private void start(VideoType videoType, String url) {
        switch (videoType) {
            case NotSupposed:
//                new DialogBuilder(aboutBtn).addText("暂不支持该地址视频!").addText("可在[设置]中查看支持列表。").setTitle("oooops!还没支持呢").create();
                DialogeBuilderForJDK11.dialog("格式不支持","请到设置中查看详情。");
                return;
            case BiliBiliVideo:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            webView.setVisible(true);
                            webView.setManaged(true);
                            String realUrl = BiliBiliUtil.getMp4SrcFromUrl(url);
                            win.eval("start('" + realUrl + "','" + "BiliBiliVideo" + "')");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case HuYaLive:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            webView.setVisible(false);
                            webView.setManaged(false);
                            String lineName = null;
                            String realUrl = null;
                            Map<String, String> huyaMap = HuYaLiveUtil.getRealSrcFromUrl(url);
                            if (!huyaMap.entrySet().isEmpty()) {
                                lineName = huyaMap.entrySet().iterator().next().getKey();
                                realUrl = huyaMap.entrySet().iterator().next().getValue();
                            }
                            embeddedMediaPlayer.media().play(realUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                break;
            case CCTV:
            case HLSLive:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        webView.setVisible(false);
                        webView.setManaged(false);
                        embeddedMediaPlayer.media().play(url);
                    }
                });
                break;
        }
    }

    /**
     * 展开或隐藏下方控制面板
     */
    private void toggleControlBox() {
        if (optWin.isVisible()) {
            optWin.setVisible(false);
            optWin.setManaged(false);
            myApp.getStage().setHeight(190);
            myApp.getStage().setY(myApp.getStage().getY() + 200);
        } else {
            optWin.setVisible(true);
            optWin.setManaged(true);
            myApp.getStage().setHeight(390);
            myApp.getStage().setX(MainApp.width - 300);
            myApp.getStage().setY(MainApp.height - (390 + MainApp.taskBarHeight));
        }
    }

    @FXML
    private void about() {
//        AboutDialog aboutDialog = new AboutDialog();
//        aboutDialog.showAboutDialog();
//        new DialogBuilder(aboutBtn).addText("聚合精彩视频,伪装播放。").addText("上班摸鱼必备工具!!")
//                .addText("MoreFunBox")
//                .addText("作者:DarkZ")
//                .addText("版本:0.1 beta")
//                .setPositiveBtn("OK", new DialogBuilder.OnClickListener() {
//                    @Override
//                    public void onClick(DialogBuilder dialogBuilder) {
//
//                    }
//                }).setTitle("磨坊盒子 MoreFunBox").create();
        /**由于升级到openjfx11 jfoenix的Dialog不能使用,采用原生的替代*/

    }

    @FXML
    private void exit(MouseEvent event) {
        MouseButton button = event.getButton();
        //单击操作
        if (button == MouseButton.PRIMARY) {
            System.exit(-1);
        }
        //右键点击
        if (button == MouseButton.SECONDARY) {
            toggleControlBox();
        }
    }


    @FXML
    private void winMin() {
        Stage stage = (Stage) aboutBtn.getScene().getWindow();
        stage.setIconified(true);
    }


    @FXML
    private void move(MouseEvent event) {
        Stage stage = (Stage) aboutBtn.getScene().getWindow();
        DragUtil.addDragListener(stage, moveBtn);
    }

    @FXML
    private void muteControl() {
        embeddedMediaPlayer.audio().setMute(!embeddedMediaPlayer.audio().isMute());
    }

    @FXML
    private void setting() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setWidth(500);
        dialog.setTitle("详细设置");
        dialog.setHeaderText("磨坊盒子已支持：【BiliBili视频】【虎牙直播】【HLS直播源】\n[哔哩哔哩][虎牙直播]默认解析最高画质。");
        ImageView imageView = new ImageView(new Image(this.myApp.getClass().getClassLoader().getResourceAsStream("Logo.png")));
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        dialog.setGraphic(imageView);
        ButtonType OkBtnType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(OkBtnType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setMinWidth(500);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 30));
        ToggleGroup group = new ToggleGroup();
        RadioButton bilibiliBtn = new RadioButton("BiliBili视频");
        bilibiliBtn.setUserData("BiliBili视频");
        bilibiliBtn.setToggleGroup(group);
        bilibiliBtn.setSelected(true);
        RadioButton huyaBtn = new RadioButton("虎牙直播");
        huyaBtn.setUserData("虎牙直播");
        huyaBtn.setToggleGroup(group);
        RadioButton hlsBtn = new RadioButton("HLS直播源");
        hlsBtn.setUserData("HLS直播源");
        hlsBtn.setToggleGroup(group);
        RadioButton cctvBtn = new RadioButton("电视直播源");
        cctvBtn.setUserData("电视直播源");
        cctvBtn.setToggleGroup(group);

        TextField bilibiliInput = new TextField();
        bilibiliInput.setPromptText("例如:https://www.bilibili.com/video/BV1h7411h7AK?xxxxxxxx");
        bilibiliInput.setPrefWidth(300);
        TextField huyaInput = new TextField();
        huyaInput.setPromptText("例如:https://www.huya.com/949527");
        huyaInput.setPrefWidth(300);
        TextField hlsInput = new TextField();
        hlsInput.setPromptText("例如:http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        hlsInput.setPrefWidth(300);

        ListView<String> listView = new ListView<>();
        List liveUrlList = new ArrayList();
        for (String key : LiveUrls.LIVE_URLS.keySet()) {
            liveUrlList.add(key);
        }
        ObservableList<String> items = FXCollections.observableArrayList(liveUrlList);
        listView.setItems(items);
        listView.setPrefWidth(300);
        listView.setPrefHeight(100);


        grid.add(bilibiliBtn, 0, 0);
        grid.add(new Label("视频地址:"), 0, 1);
        grid.add(bilibiliInput, 1, 1);

        grid.add(huyaBtn, 0, 2);
        grid.add(new Label("直播地址:"), 0, 3);
        grid.add(huyaInput, 1, 3);

        grid.add(hlsBtn, 0, 4);
        grid.add(new Label("直播源:"), 0, 5);
        grid.add(hlsInput, 1, 5);

        grid.add(cctvBtn, 0, 6);
        grid.add(new Label("节目栏:"), 0, 7);
        grid.add(listView, 1, 7);

// Enable/Disable login button depending on whether a username was entered.
//        Node loginButton = dialog.getDialogPane().lookupButton(OkBtnType);
//        loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
//        username.textProperty().addListener((observable, oldValue, newValue) -> {
//            loginButton.setDisable(newValue.trim().isEmpty());
//        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
//        Platform.runLater(() -> username.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            String inputUrl="";
            if (dialogButton == OkBtnType) {
                switch (group.getSelectedToggle().getUserData().toString()) {
                    case "BiliBili视频":
                        inputUrl =bilibiliInput.getText();
                        if (!inputUrl.trim().isEmpty()){
                            start(VideoType.BiliBiliVideo,inputUrl.trim());
                        }
                        break;
                    case "虎牙直播":
                        inputUrl=huyaInput.getText();
                        if (!inputUrl.trim().isEmpty()){
                            start(VideoType.HuYaLive,inputUrl.trim());
                        }
                        break;
                    case "HLS直播源":
                        inputUrl=hlsInput.getText();
                        if (!inputUrl.trim().isEmpty()){
                            start(VideoType.HLSLive,inputUrl.trim());
                        }
                        break;
                    case "电视直播源":
                        inputUrl=LiveUrls.LIVE_URLS.get(listView.getSelectionModel().getSelectedItem());
                        if (!inputUrl.trim().isEmpty()){
                            start(VideoType.CCTV,inputUrl.trim());
                        }
                        break;
                }
                return null;
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
    }

    JsUtil jsUtil = new JsUtil();

    public class JsUtil {
        public void toggleControlBox() {
            MainController.this.toggleControlBox();
            return;
        }

        public void closeTheWin() {
            System.exit(0);
        }
    }
}
