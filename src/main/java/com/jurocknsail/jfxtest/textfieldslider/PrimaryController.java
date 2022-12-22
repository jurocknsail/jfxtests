package com.jurocknsail.jfxtest.textfieldslider;

import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    private boolean changeMarker = false;
    private SimpleBooleanProperty anyTextFieldChanged = new SimpleBooleanProperty();
    private SimpleIntegerProperty tf1IntProp = new SimpleIntegerProperty();
    private SimpleIntegerProperty tf2IntProp = new SimpleIntegerProperty();
    private SimpleIntegerProperty tf3IntProp = new SimpleIntegerProperty();

    private Map <TextField, SimpleIntegerProperty> textFieldsIntegerProperties = new HashMap<>();
    private Map <PopOver, TextField> popOverTextFields = new HashMap<>();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Slider sl1 = new Slider(0, 255, 0);
        sl1.setOrientation(Orientation.VERTICAL);
        sl1.setShowTickMarks(true);
        sl1.setShowTickLabels(true);
        sl1.valueProperty().addListener((obs, oldval, newVal) -> sl1.setValue(Math.round(newVal.doubleValue())));

        Slider sl2 = new Slider(0, 255, 0);
        sl2.setOrientation(Orientation.VERTICAL);
        sl2.setShowTickMarks(true);
        sl2.setShowTickLabels(true);
        sl2.valueProperty().addListener((obs, oldval, newVal) -> sl2.setValue(Math.round(newVal.doubleValue())));


        Slider sl3 = new Slider(0, 255, 0);
        sl3.setOrientation(Orientation.VERTICAL);
        sl3.setShowTickMarks(true);
        sl3.setShowTickLabels(true);
        sl3.valueProperty().addListener((obs, oldval, newVal) -> sl3.setValue(Math.round(newVal.doubleValue())));


        // -------------

        textFieldsIntegerProperties.put(tf1, tf1IntProp);
        textFieldsIntegerProperties.put(tf2, tf2IntProp);
        textFieldsIntegerProperties.put(tf3, tf3IntProp);

        anyTextFieldChanged.addListener((a,b,c) -> {
            System.out.println( " REAL CHANGE DETECTED");
        });

        // -------------

        PopOver popover1 = new PopOver(sl1);
        PopOver popover2 = new PopOver(sl2);
        PopOver popover3 = new PopOver(sl3);

        popOverTextFields.put(popover1, tf1);
        popOverTextFields.put(popover2, tf2);
        popOverTextFields.put(popover3, tf3);

        // Set focus on parent HBOX to avoid default TF selected
        Platform.runLater(() -> hbox.requestFocus());

        // -----------

        popover1.setArrowLocation(ArrowLocation.TOP_CENTER);
        popover1.focusedProperty().addListener((change, oldVal, newVal) -> {
            if (!newVal && !popover2.isFocused() && !popover3.isFocused())
                Platform.runLater(() -> hbox.requestFocus());
        });
        popover1.showingProperty().addListener((a,b,c) -> {
            if(!c) {
                TextField tf = popOverTextFields.get(popover1);
                textFieldsIntegerProperties.get(tf).set(Integer.valueOf(tf.getText()));
            }
        });

        tf1.setOnMouseClicked(clicked -> {
            popover1.show(tf1, -10);
            tf1.requestFocus();
        });
        setTextChangeListener(tf1);
        tf1.textProperty().bindBidirectional(sl1.valueProperty(), NumberFormat.getNumberInstance());

        // -------------

        popover2.setArrowLocation(ArrowLocation.TOP_CENTER);
        popover2.focusedProperty().addListener((change, oldVal, newVal) -> {
            if (!newVal && !popover1.isFocused() && !popover3.isFocused())
                Platform.runLater(() -> hbox.requestFocus());
        });
        popover2.showingProperty().addListener((a,b,c) -> {
            if(!c) {
                TextField tf = popOverTextFields.get(popover2);
                textFieldsIntegerProperties.get(tf).set(Integer.valueOf(tf.getText()));
            }
        });

        tf2.setOnMouseClicked(clicked -> {
            popover2.show(tf2, -10);
            tf2.requestFocus();
        });
        setTextChangeListener(tf2);
        tf2.textProperty().bindBidirectional(sl2.valueProperty(), NumberFormat.getNumberInstance());

        // --------------

        popover3.setArrowLocation(ArrowLocation.TOP_CENTER);
        popover3.focusedProperty().addListener((change, oldVal, newVal) -> {
            if (!newVal && !popover2.isFocused() && !popover1.isFocused())
                Platform.runLater(() -> hbox.requestFocus());
        });
        popover3.showingProperty().addListener((a,b,c) -> {
            if(!c) {
                TextField tf = popOverTextFields.get(popover3);
                textFieldsIntegerProperties.get(tf).set(Integer.valueOf(tf.getText()));
            }
        });

        tf3.setOnMouseClicked(clicked -> {
            popover3.show(tf3, -10);
            tf3.requestFocus();
        });
        setTextChangeListener(tf3);
        tf3.textProperty().bindBidirectional(sl3.valueProperty(), NumberFormat.getNumberInstance() );

        // --------------

        
    }

    private void setTextChangeListener (TextField textField) {
        // force the field to be numeric only
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {

                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                } else if (newValue.equals("")){
                    textField.setText("0");
                } else if (newValue.startsWith("0") && newValue.length() > 1) {
                    textField.setText(newValue.substring(1));
                } 
            }
        });

        textFieldsIntegerProperties.get(textField).addListener((a,b,c)-> {
            if(!b.equals(c)){
                changeMarker = !changeMarker;
                anyTextFieldChanged.set(changeMarker);
            }

        });

        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    
                    textFieldsIntegerProperties.get(textField).set(Integer.valueOf(textField.getText()));
                }
            }
        });

        
    }
}
