package com.jurocknsail.jfxtest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class PrimaryController implements Initializable {

    @FXML
    TableView<Person> table;

    @FXML
    Button btn;

    @FXML
    Button btn1;

    String updatedItemField;
    Object updatedItemOldValue;
    private boolean externalUpdate = false;

    private ObservableList<Person> persons = FXCollections.observableArrayList(new Callback<Person, Observable[]>() {
        @Override
        public Observable[] call(Person person) {

            Observable[] obs = new Observable[] {
                    person.nameProperty(),
                    person.genderProperty(),
                    person.aliveProperty()
            };

            person.aliveProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                    updatedItemOldValue = arg1;
                    updatedItemField = "alive";
                }
            });
            person.nameProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                    updatedItemOldValue = arg1;
                    updatedItemField = "name";
                }
            });
            person.genderProperty().addListener(new ChangeListener<Gender>() {
                @Override
                public void changed(ObservableValue<? extends Gender> arg0, Gender arg1, Gender arg2) {
                    updatedItemOldValue = arg1;
                    updatedItemField = "gender";
                }
            });

            return obs;
        }
    });

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        btn.setOnAction(click -> {
            System.out.println("All data : " + persons);
            System.out.println("Last updated item field : " + updatedItemField);
            System.out.println("Last update item field value : " + updatedItemOldValue);
        });

        btn1.setOnAction(click -> {
            externalUpdate = true;
            persons.get(0).setName("Updated");
            persons.get(0).setGender(Gender.male);
            persons.get(2).setName("Updated 2");
            externalUpdate = false;
        });

        table.setEditable(true);

        TableColumn<Person, String> name = new TableColumn<>("Name");
        name.setCellFactory(cell -> {
            return new TextFieldTableCell<>(new StringConverter<String>() {
                @Override
                public String fromString(String arg0) {
                    return arg0.toString();
                }

                @Override
                public String toString(String arg0) {
                    return arg0.toString();
                }
            });
        });
        name.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        name.setPrefWidth(150);
        name.setSortable(true);

        TableColumn<Person, Gender> gender = new TableColumn<>("Gender");
        gender.setCellFactory(cell -> {
            return new ComboBoxTableCell<Person, Gender>(new StringConverter<Gender>() {
                @Override
                public Gender fromString(String arg0) {
                    return Gender.valueOf(arg0);
                }

                @Override
                public String toString(Gender arg0) {
                    return arg0.toString();
                }
            }, Gender.male, Gender.female, Gender.other);
        });
        gender.setCellValueFactory(new PropertyValueFactory<Person, Gender>("gender"));
        gender.setPrefWidth(150);

        TableColumn<Person, Boolean> alive = new TableColumn<>("Alive");
        alive.setCellFactory(cell -> {
            return new CheckBoxTableCell<>();
        });
        alive.setCellValueFactory(new PropertyValueFactory<Person, Boolean>("alive"));
        alive.setPrefWidth(50);

        table.getColumns().add(name);
        table.getColumns().add(gender);
        table.getColumns().add(alive);

        persons.addListener((ListChangeListener.Change<? extends Person> c) -> {
            while (c.next()) {
                if (c.wasUpdated()) {

                    if (!externalUpdate) {

                        // Using Task instead of Plateform.runLater() :
                        Task<Void> notify = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                
                                System.out.println(Thread.currentThread().getName() +" / "+ Thread.currentThread().getId());
                                System.out.println("UPDATED Row : " + c.getFrom());
                                System.out.println("UPDATED Item field : " + updatedItemField);
                                System.out.println("UPDATED Item old value : " + updatedItemOldValue);
                                Object object = c.getList().get(c.getFrom());
                                System.out.println("UPDATED Item new value : " + runGetter(updatedItemField, object.getClass(), object));
                                System.out.println("UPDATED Item : " + c.getList().get(c.getFrom()));

                                return null;
                            }
                        };
                        new Thread(notify).start();

                    }
                }
            }
        });

        for (int i = 0; i < 5; i++) {
            persons.add(new Person("Nom " + i, Gender.other, false));
        }
        table.setItems(persons);

    }

    public static Object runGetter(String field, Class<? extends Object> clazz, Object object) {
        // Find the correct method
        for (Method method : clazz.getMethods()) {
            if ((method.getName().startsWith("get")) && (method.getName().length() == (field.length() + 3))) {
                if (method.getName().toLowerCase().endsWith(field.toLowerCase())) {
                    // Method found, run it
                    try {
                        return method.invoke(object);
                    } catch (IllegalAccessException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    } catch (InvocationTargetException e) {
                        System.out.println("Could not determine method: " + method.getName());
                    }

                }
            }
        }

        return null;
    }
}
