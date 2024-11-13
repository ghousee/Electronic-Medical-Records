module com.mycompany.emr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.emr to javafx.fxml;
    exports com.mycompany.emr;
}
