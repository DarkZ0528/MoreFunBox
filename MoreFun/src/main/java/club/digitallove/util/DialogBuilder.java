package club.digitallove.util;


import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author StarsOne
 * @date Create in  2019/6/2 0002 20:51
 * @description
 */
public class DialogBuilder {
    private String title;
    private JFXButton negativeBtn = null;
    private JFXButton positiveBtn = null;
    JFXDialogLayout layout=new JFXDialogLayout();
    VBox bodyPane=new VBox();
    private Window window;
    private Paint negativeBtnPaint = Paint.valueOf("#747474");//否定按钮文字颜色，默认灰色
    private Paint positiveBtnPaint = Paint.valueOf("#0099ff");
    private JFXAlert<String> alert;
    private Map<String, JFXTextField> insertMap=new HashMap<>();

    /**
     * 构造方法
     *
     * @param control any a control
     */
    public DialogBuilder(Control control) {
        window = control.getScene().getWindow();
    }

    public DialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    public DialogBuilder setNegativeBtn(String negativeBtnText) {
        return setNegativeBtn(negativeBtnText, null, null);
    }

    /**
     * set text and color of button
     *
     * @param negativeBtnText text
     * @param color           colorvalue like #fafafa
     * @return
     */
    public DialogBuilder setNegativeBtn(String negativeBtnText, String color) {
        return setNegativeBtn(negativeBtnText, null, color);
    }

    /**
     * set text,onclicklistener and color of cancel button
     *
     * @param negativeBtnText
     * @param negativeBtnOnclickListener
     * @param color                      文字颜色 十六进制 #fafafa
     * @return
     */
    public DialogBuilder setNegativeBtn(String negativeBtnText,  OnClickListener negativeBtnOnclickListener, String color) {
        if (color != null) {
            this.negativeBtnPaint = Paint.valueOf(color);
        }
        return setNegativeBtn(negativeBtnText, negativeBtnOnclickListener);
    }


    /**
     * set text and listener of cancel button
     *
     * @param negativeBtnText            text
     * @param negativeBtnOnclickListener listener
     * @return
     */
    public DialogBuilder setNegativeBtn(String negativeBtnText, OnClickListener negativeBtnOnclickListener) {

        negativeBtn = new JFXButton(negativeBtnText);
        negativeBtn.setCancelButton(true);
        negativeBtn.setTextFill(negativeBtnPaint);
        negativeBtn.setButtonType(JFXButton.ButtonType.FLAT);
        negativeBtn.setOnAction(addEvent -> {
            alert.hideWithAnimation();
            if (negativeBtnOnclickListener != null) {
                negativeBtnOnclickListener.onClick(this);
            }
        });
        return this;
    }

    /**
     * set text and color of ok button
     *
     * @param positiveBtnText text
     * @param color           color like #fafafa
     * @return
     */
    public DialogBuilder setPositiveBtn(String positiveBtnText, String color) {
        return setPositiveBtn(positiveBtnText, null, color);
    }

    /**
     * 设置按钮文字，颜色和点击监听器
     *
     * @param positiveBtnText            文字
     * @param positiveBtnOnclickListener 点击监听器
     * @param color                      颜色 十六进制 #fafafa
     * @return
     */
    public DialogBuilder setPositiveBtn(String positiveBtnText, OnClickListener positiveBtnOnclickListener, String color) {
        this.positiveBtnPaint = Paint.valueOf(color);
        return setPositiveBtn(positiveBtnText, positiveBtnOnclickListener);
    }

    /**
     * 设置按钮文字和监听器
     *
     * @param positiveBtnText            文字
     * @param positiveBtnOnclickListener 点击监听器
     * @return
     */
    public DialogBuilder setPositiveBtn(String positiveBtnText,  OnClickListener positiveBtnOnclickListener) {
        positiveBtn = new JFXButton(positiveBtnText);
        positiveBtn.setDefaultButton(true);
        positiveBtn.setTextFill(positiveBtnPaint);
        positiveBtn.setOnAction(closeEvent -> {
            alert.hideWithAnimation();
            if (positiveBtnOnclickListener != null) {
                positiveBtnOnclickListener.onClick(this);//回调onClick方法
            }
        });
        return this;
    }

    /**
     * 创建对话框并显示
     *
     * @return JFXAlert<String>
     */
    public JFXAlert<String> create() {
        alert = new JFXAlert<>((Stage) (window));
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(true);
        layout.setHeading(new Label(title));
        layout.setStyle("-fx-pref-width:200");
        bodyPane.setStyle("-fx-spacing:5");
        layout.setBody(bodyPane);
        if (negativeBtn != null && positiveBtn != null) {
            layout.setActions(negativeBtn,positiveBtn);
        }else {
            if (negativeBtn != null) {
                layout.setActions(negativeBtn);
            } else if (positiveBtn != null) {
                layout.setActions(positiveBtn);
            }
        }
        alert.setContent(layout);
        alert.show();
        return alert;
    }

    public DialogBuilder addText(String text){
        Label label=new Label(text);
        label.setStyle("-fx-font-size: 12");
        bodyPane.getChildren().add(label);
        return this;
    }
    public DialogBuilder addTextField(String fieldKey,JFXTextField field){
        bodyPane.getChildren().add(field);
        insertMap.put(fieldKey,field);
        return this;
    }

    public interface OnClickListener {
        void onClick(DialogBuilder dialogBuilder);
    }
}