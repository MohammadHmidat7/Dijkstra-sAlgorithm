module com.example.algo3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.algo3 to javafx.fxml;
    exports com.example.algo3;
}