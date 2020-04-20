module JavaFXDesktopApplication {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.base;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.fxml;

    requires com.jfoenix;

    opens application;
}