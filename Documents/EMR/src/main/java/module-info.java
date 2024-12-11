module com.mycompany.emr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
<<<<<<< HEAD
    requires kernel;
    requires layout;
=======
>>>>>>> f43c1cb7dcbfb59f21bfb92452c9c1ab7922fd0a

    opens com.mycompany.emr to javafx.fxml;
    opens com.mycompany.emr.controllers to javafx.fxml;
    opens com.mycompany.emr.models to javafx.base;
    
    exports com.mycompany.emr;
    exports com.mycompany.emr.controllers;
    exports com.mycompany.emr.models;
<<<<<<< HEAD

=======
>>>>>>> f43c1cb7dcbfb59f21bfb92452c9c1ab7922fd0a
}

