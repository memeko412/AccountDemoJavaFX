module com.example.accountdemo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.accountdemo to javafx.fxml;
    exports com.example.accountdemo;
}