module com.example.pacmangame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pacmangame to javafx.fxml;
    exports com.example.pacmangame;
}