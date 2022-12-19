module com.jurocknsail.jfxtest.table {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires transitive javafx.graphics;

    opens com.jurocknsail.jfxtest.table to javafx.fxml;
    exports com.jurocknsail.jfxtest.table;
}
