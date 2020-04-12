package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXSlider;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sortingAlgorithms.BubbleSort;

public class Controller implements Initializable {
	//Instance variable
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
	@FXML
	private Button cancelButton;
	// Text menu
	@FXML
	private Text genArray, enterArray;
	@FXML
	private HBox backButtonBox;
	@FXML
	private JFXSlider slider;
	@FXML
	protected FlowPane diagramPane;
	private int[] copyArrayGenerated;
	private SequentialTransition sq;
	private Text textButtons;

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
		// When changing the text field of the selection menu, enable the sortButton to
		// be pressed and set an animation
		jfxComboBox.valueProperty().addListener(e -> {
			if (sortButton.isDisable()) {
				sortButton.setDisable(false);
				translationOfSortButton();
			}
		});
		// Adding rippler effects
		JFXRippler rippler = new JFXRippler(backButtonBox);
		rippler.setRipplerFill(Paint.valueOf("#FFD9D6"));
		rippler.setRipplerRadius(100);
		rippler.setLayoutX(backButtonBox.getLayoutX());
		rippler.setLayoutY(backButtonBox.getLayoutY());
		rippler.setMaskType(JFXRippler.RipplerMask.CIRCLE);
		titlePane.getChildren().add(rippler);
		slider.setValue(10);
		copyArrayGenerated = generateArray((int) slider.getValue());
		createBars(copyArrayGenerated);
	} // End of method

	@FXML
	// CSS doesnt work when hovering a text element
	public void hoverInTextMenu(MouseEvent e) {
		textButtons = (Text) e.getSource();
		textButtons.setFill(javafx.scene.paint.Paint.valueOf("#FFD9D6"));
	} // End of method

	@FXML
	public void hoverOutTextMenu(MouseEvent e) {
		textButtons = (Text) e.getSource();
		textButtons.setFill(javafx.scene.paint.Paint.valueOf("#ABB37B"));
	} // End of method

	@FXML
	// Set a bouncing animation on the sort button every 5 seconds
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
		sq.setOnFinished(e -> { // Loop the animation and set a delay so that it will update the animation every 5 seconds
			sq.setDelay(javafx.util.Duration.seconds(5));
			sq.play();
		});
		sq.play();
	} // End of method

	@FXML
	// When clicking on the generate a new array button
	public void handleClickArrayGeneration(MouseEvent e) {
		fadeAnimation(e);
		diagramPane.getChildren().clear();
		copyArrayGenerated = generateArray((int) slider.getValue());
		createBars(copyArrayGenerated);
	} // End of method

	@FXML
	//When clicking on the sort button
	public void handleSortButton(MouseEvent e) throws InterruptedException {
		String algorithmSelected = jfxComboBox.getValue().getText(); //Get the text value in the ComboBox
		switch (algorithmSelected) {
		case "Bubble sort":
			BubbleSort bubbleSort = new BubbleSort(copyArrayGenerated, diagramPane, titlePane, cancelButton);
			bubbleSort.sort();
			break;
		case "Selection sort":
			break;
		case "Insertion sort":
			break;
		case "Merge sort":
			break;
		case "Quicksort":
			break;
		case "Heap sort":
			break;
		default:
			break;
		}
	}

	@FXML
	//When the user drag the bar. Everytime the value change, a new array is generate depending on the pourcentage value of the slider
	public void handleSlidderDrag(MouseEvent e) {
		diagramPane.getChildren().clear();
		copyArrayGenerated = generateArray((int) slider.getValue());
		createBars(copyArrayGenerated);
	} // End of method

	@FXML
	public void fadeAnimation(MouseEvent e) {
		textButtons = (Text) e.getSource();
		FadeTransition transition = new FadeTransition();
		transition.setNode(textButtons);
		transition.setDuration(Duration.seconds(0.5));
		transition.setFromValue(1);
		transition.setToValue(0);
		transition.setCycleCount(2);
		transition.setAutoReverse(true);
		transition.play();
	} // ENd of method

	// Method that generates a new array
	public int[] generateArray(int size) {
		int sizeOfTheArray = size * 3 + 4;
		int[] arrayGenerate = new int[sizeOfTheArray];
		System.out.println(arrayGenerate.length); // Debug
		for (int i = 0; i < arrayGenerate.length; i++) {
			int integerGenerator = (int) (Math.random() * 700 + 1);
			arrayGenerate[i] = integerGenerator;
		}
		return arrayGenerate;
	} // End of method

	//Use the generated array to create the rectangle bars
	public void createBars(int[] array) {
		// Create a new label for each value in the array
		for (int i = 0; i < array.length; i++) {
			Rectangle rectangle = new Rectangle(950 / array.length, array[i]);
			rectangle.setFill(Paint.valueOf("#305580"));
			int marginRight = (array.length < 100 ? marginRight = 3 : array.length < 200 ? 2 : 1);
			FlowPane.setMargin(rectangle, new Insets(0, 0, 0, marginRight));
			diagramPane.getChildren().add(rectangle);
		} // End of loop
	} // End of method

} // End of class
