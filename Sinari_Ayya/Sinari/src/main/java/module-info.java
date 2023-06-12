module com.example.sinari {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sinari to javafx.fxml;
    exports com.example.sinari;
}