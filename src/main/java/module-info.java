module com.jurocknsail.jfxtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.jurocknsail.jfxtest to javafx.fxml;
    exports com.jurocknsail.jfxtest;
}
