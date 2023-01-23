module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ac.at.fhcampuswien to javafx.fxml;
    exports ac.at.fhcampuswien;
    exports ac.at.fhcampuswien.Models;
    opens ac.at.fhcampuswien.Models to javafx.fxml;
}