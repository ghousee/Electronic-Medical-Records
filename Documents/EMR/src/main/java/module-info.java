module com.mycompany.emr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    requires java.sql;

    opens com.mycompany.emr.controllers to javafx.fxml;
    exports com.mycompany.emr;
}
