package com.jurocknsail.jfxtest.textfieldslider;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;

public class PrimaryController implements Initializable {

    @FXML
    TextField tf1;

    @FXML
    TextField tf2;

    @FXML
    TextField tf3;

    @FXML
    HBox hbox;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Slider sl1 = new Slider(0, 255, 120);
        sl1.setOrientation(Orientation.VERTICAL);
        sl1.setShowTickMarks(true);
        sl1.setShowTickLabels(true);

        Slider sl2 = new Slider(0, 255, 220);
        sl2.setOrientation(Orientation.VERTICAL);
        sl2.setShowTickMarks(true);
        sl2.setShowTickLabels(true);

        Slider sl3 = new Slider(0, 255, 50);
        sl3.setOrientation(Orientation.VERTICAL);
        sl3.setShowTickMarks(true);
        sl3.setShowTickLabels(true);

        PopOver popover1 = new PopOver(sl1);
        PopOver popover2 = new PopOver(sl2);
        PopOver popover3 = new PopOver(sl3);

        // Set focus on parent HBOX to avoid default TF selected
        Platform.runLater(() -> hbox.requestFocus());

        // -----------

        popover1.setArrowLocation(ArrowLocation.TOP_CENTER);
        popover1.focusedProperty().addListener((change, oldVal, newVal) -> {
            if (!newVal && !popover2.isFocused() && !popover3.isFocused())
                Platform.runLater(() -> hbox.requestFocus());
        });

        tf1.setOnMouseClicked(clicked -> {
            popover1.show(tf1, -10);
            tf1.requestFocus();
        });
        tf1.setTextFormatter(createTextFormatter(tf1));
        tf1.textProperty().bindBidirectional(sl1.valueProperty(), NumberFormat.getNumberInstance());

        // -------------

        popover2.setArrowLocation(ArrowLocation.TOP_CENTER);
        popover2.focusedProperty().addListener((change, oldVal, newVal) -> {
            if (!newVal && !popover1.isFocused() && !popover3.isFocused())
                Platform.runLater(() -> hbox.requestFocus());
        });

        tf2.setOnMouseClicked(clicked -> {
            popover2.show(tf2, -10);
            tf2.requestFocus();
        });
        tf2.setTextFormatter(createTextFormatter(tf2));
        tf2.textProperty().bindBidirectional(sl2.valueProperty(), NumberFormat.getNumberInstance());

        // --------------

        popover3.setArrowLocation(ArrowLocation.TOP_CENTER);
        popover3.focusedProperty().addListener((change, oldVal, newVal) -> {
            if (!newVal && !popover2.isFocused() && !popover1.isFocused())
                Platform.runLater(() -> hbox.requestFocus());
        });

        tf3.setOnMouseClicked(clicked -> {
            popover3.show(tf3, -10);
            tf3.requestFocus();
        });
        tf3.setTextFormatter(createTextFormatter(tf3));
        tf3.textProperty().bindBidirectional(sl3.valueProperty(), NumberFormat.getNumberInstance());

        // --------------

    }

    private TextFormatter createTextFormatter(TextField textfield) {

        TextFormatter<String> tf = new TextFormatter<String>(change -> {

            final int oldLength = change.getControlText().length();
            int newLength = change.getControlNewText().length();

            // Handle backspace
            if (newLength < oldLength)
                return change;

            if (newLength > 3)
                change.setText("");
            else if (textfield.getText().length() > 1
                    && Integer.valueOf(textfield.getText().concat(change.getText())) > 255)
                change.setText("");
            return change;
        });

        return tf;

    }
}
