module com.mycompany.emr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires kernel;
    requires layout;    
    requires org.slf4j;

    opens com.mycompany.emr to javafx.fxml;
    opens com.mycompany.emr.controllers to javafx.fxml;
    opens com.mycompany.emr.models to javafx.base;
    
    exports com.mycompany.emr;
    exports com.mycompany.emr.controllers;
    exports com.mycompany.emr.models;
}

