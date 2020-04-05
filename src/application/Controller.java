package application;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class Controller implements Initializable {
	@FXML
	private AnchorPane mainPane;
	@FXML
	private ImageView titleImage;
	@FXML
	private JFXComboBox<Label> jfxComboBox = new JFXComboBox<Label>();
	@FXML
	private JFXButton sortButton;
	//Text menu
	private Text text;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Center the imageview
		mainPane.widthProperty().addListener(e -> {
			titleImage.setLayoutX((mainPane.getWidth() / 2) - 14);
		});
		//add the items for the selection
		jfxComboBox.getItems().add(new Label("Bubble sort"));
		jfxComboBox.getItems().add(new Label("Selection sort"));
		jfxComboBox.getItems().add(new Label("Insertion sort"));
		jfxComboBox.getItems().add(new Label("Merge sort"));
		jfxComboBox.getItems().add(new Label("Quicksort"));
		jfxComboBox.getItems().add(new Label("Heap sort"));
		sortButton.setDisable(true);
		
	}
	//CSS doesnt work when hovering a text element
	public void hoverInTextMenu(MouseEvent e) {
		text = (Text) e.getSource();
		text.setFill(javafx.scene.paint.Paint.valueOf("#FFD9D6"));
	}
	
	public void hoverOutTextMenu(MouseEvent e) {
		text = (Text) e.getSource();
		text.setFill(javafx.scene.paint.Paint.valueOf("#ABB37B"));
	}
	public void fadeAnimation(MouseEvent e) {
		FadeTransition ft = new FadeTransition(javafx.util.Duration.seconds(0.5), text);
		ft.setFromValue(1.0);
		ft.setToValue(0.1);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);
		ft.play();
	}
}
