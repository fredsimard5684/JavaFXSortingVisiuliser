package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class Controller implements Initializable {
	
	@FXML
	private AnchorPane mainPane;
	@FXML
	private AnchorPane titlePane;
	@FXML
	private ImageView titleImage;
	@FXML
	private JFXComboBox<Label> jfxComboBox = new JFXComboBox<Label>();
	@FXML
	private JFXButton sortButton;
	// Text menu
	private Text text;
	private SequentialTransition sq;
	@FXML
	private HBox backButtonBox;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Center the imageview
		mainPane.widthProperty().addListener(e -> {
			titleImage.setLayoutX((mainPane.getWidth() / 2) - 14);
		});
		// add the items for the selection
		jfxComboBox.getItems().add(new Label("Bubble sort"));
		jfxComboBox.getItems().add(new Label("Selection sort"));
		jfxComboBox.getItems().add(new Label("Insertion sort"));
		jfxComboBox.getItems().add(new Label("Merge sort"));
		jfxComboBox.getItems().add(new Label("Quicksort"));
		jfxComboBox.getItems().add(new Label("Heap sort"));
		sortButton.setDisable(true);
		//When changing the text field of the selection menu, enable the sortButton to be pressed and set an animation
		jfxComboBox.valueProperty().addListener(e -> {
			if (sortButton.isDisable()) {
				sortButton.setDisable(false);
				translationOfSortButton();
			}
		});
		//Adding rippler effects
		JFXRippler rippler = new JFXRippler(backButtonBox);
		rippler.setRipplerFill(Paint.valueOf("#FFD9D6"));
		rippler.setRipplerRadius(100);
		rippler.setLayoutX(backButtonBox.getLayoutX());
		rippler.setLayoutY(backButtonBox.getLayoutY());
		rippler.setMaskType(JFXRippler.RipplerMask.CIRCLE);
		titlePane.getChildren().add(rippler);
	}

	// CSS doesnt work when hovering a text element
	public void hoverInTextMenu(MouseEvent e) {
		text = (Text) e.getSource();
		text.setFill(javafx.scene.paint.Paint.valueOf("#FFD9D6"));
	}

	public void hoverOutTextMenu(MouseEvent e) {
		text = (Text) e.getSource();
		text.setFill(javafx.scene.paint.Paint.valueOf("#ABB37B"));
	}
	//Set a bouncing animation on the sort button
	public void translationOfSortButton() {
		TranslateTransition firstBounce = new TranslateTransition(javafx.util.Duration.seconds(0.2), sortButton);
		firstBounce.setFromY(sortButton.getLayoutY() - 19);
		firstBounce.setToY(firstBounce.getFromY() - 19);
		firstBounce.setCycleCount(2);
		firstBounce.setAutoReverse(true);
		TranslateTransition secondTransition = new TranslateTransition(javafx.util.Duration.seconds(0.2), sortButton);
		secondTransition.setFromY(sortButton.getLayoutY() - 19);
		secondTransition.setToY(secondTransition.getFromY() - 10);
		secondTransition.setCycleCount(2);
		secondTransition.setAutoReverse(true);
		TranslateTransition thirdTranslation = new TranslateTransition(javafx.util.Duration.seconds(0.2), sortButton);
		thirdTranslation.setFromY(sortButton.getLayoutY() - 19);
		thirdTranslation.setToY(secondTransition.getFromY() - 5);
		thirdTranslation.setCycleCount(2);
		thirdTranslation.setAutoReverse(true);
		sq = new SequentialTransition();
		sq.getChildren().addAll(firstBounce, secondTransition, thirdTranslation);
		sq.setCycleCount(1);
		sq.setOnFinished(e -> {
			sq.setDelay(javafx.util.Duration.seconds(5));
			sq.play();
		});
		sq.play();
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
