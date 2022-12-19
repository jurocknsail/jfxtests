module com.jurocknsail.jfxtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires transitive javafx.graphics;
    requires org.controlsfx.controls;

    opens com.jurocknsail.jfxtest.table to javafx.fxml;
    exports com.jurocknsail.jfxtest.table;

    opens com.jurocknsail.jfxtest.textfieldslider to javafx.fxml;
    exports com.jurocknsail.jfxtest.textfieldslider;
}
