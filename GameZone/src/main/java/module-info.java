module com.example.gamezone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gamezone to javafx.fxml;
    exports com.example.gamezone;
}