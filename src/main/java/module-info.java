module JavaFXDesktopApplication {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;

    requires com.jfoenix;

    opens application;
}