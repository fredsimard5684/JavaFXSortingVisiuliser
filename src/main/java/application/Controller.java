package application;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import sortingAlgorithms.*;

public class Controller implements Initializable {
    // Instance variable
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
    private FlowPane diagramPane;
    private int[] copyArrayGenerated;
    private SequentialTransition sq;
    private Text textButtons;

    // Set the speed of the algorithm
    private int algorithmSpeed;

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

        // Generate on load a random array
        slider.setValue(10);
        initialiseRectangleBars(generateArray((int) slider.getValue()));
        System.out.println(11/6);
        System.out.println((int) Math.floor(11/6));
    } // End of method

    //Set the rectangles bar with the associated speed
    public void initialiseRectangleBars(int[] array) {
        diagramPane.getChildren().clear();
        copyArrayGenerated = array;
        createBars(copyArrayGenerated);
        algorithmSpeed = (int) (10000 / (Math.pow(copyArrayGenerated.length, 1.5)));
    }

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
        sq.setOnFinished(e -> { // Loop the animation and set a delay so that it will update the animation every
            // 5 seconds
            sq.setDelay(javafx.util.Duration.seconds(5));
            sq.play();
        });
        sq.play();
    } // End of method

    @FXML
    // When clicking on the generate a new array button
    public void handleClickArrayGeneration(MouseEvent e) {
        fadeAnimation(e);
        initialiseRectangleBars(generateArray((int) slider.getValue()));
    } // End of method

    @FXML
    // When clicking on the sort button
    public void handleSortButton(MouseEvent e) throws InterruptedException {
        String algorithmSelected = jfxComboBox.getValue().getText(); // Get the text value in the ComboBox
        switch (algorithmSelected) {
            case "Bubble sort":
                SortingAlgorithms bubbleSort = new BubbleSort(copyArrayGenerated);
                bubbleSort.sort(diagramPane, titlePane, mainPane, algorithmSpeed);
                break;
            case "Selection sort":
                SortingAlgorithms selectionSort = new SelectionSort(copyArrayGenerated);
                selectionSort.sort(diagramPane, titlePane, mainPane, algorithmSpeed);
                break;
            case "Insertion sort":
                SortingAlgorithms insertionSort = new InsertionSort(copyArrayGenerated);
                insertionSort.sort(diagramPane, titlePane, mainPane, algorithmSpeed);
                break;
            case "Merge sort":
                SortingAlgorithms mergeSort = new MergeSort(copyArrayGenerated);
                mergeSort.sort(diagramPane, titlePane, mainPane, algorithmSpeed);
                break;
            case "Quicksort":
                SortingAlgorithms quickSort = new QuickSort(copyArrayGenerated);
                quickSort.sort(diagramPane, titlePane, mainPane, algorithmSpeed);
                break;
            case "Heap sort":
                SortingAlgorithms heapSort = new HeapSort(copyArrayGenerated);
                heapSort.sort(diagramPane, titlePane, mainPane,algorithmSpeed);
                break;
            default:
                break;
        }
    }

    @FXML
    // When the user drag the bar. Everytime the value change, a new array is
    // generate depending on the pourcentage value of the slider
    public void handleSlidderDrag(MouseEvent e) {
        initialiseRectangleBars(generateArray((int) slider.getValue()));
    } // End of method

    //Do a little fading animation
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
    } // End of method

    // Allow to change the default speed of the algorithm
    @FXML
    public void handleClickOnSpeedButton(MouseEvent e) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(algorithmSpeed));
        dialog.setTitle("Change the algorithm speed");
        dialog.setHeaderText("Customize the algorithm speed");
        dialog.setContentText("Enter the algorithm speed in milliseconds(the lower the faster):\n"
                + "Note: This value will be ovewrite if you change the array size with the slider");
        Optional<String> result;
        boolean isWrongValue = true;
        while (isWrongValue) {
            result = dialog.showAndWait();
            try {
                result.ifPresent(speed -> algorithmSpeed = Integer.parseInt(speed));
                isWrongValue = (algorithmSpeed < 1 || algorithmSpeed > 10000 ? isWrongValue = true : false);
                // If the user enter non-numeric character, catch the error
            } catch (NumberFormatException exception) {
                isWrongValue = true;
            }
            if (isWrongValue)
                errorLabel(dialog);
        }
    } // End of method

    @FXML
    public void handleEnteringArray(MouseEvent e) throws FileNotFoundException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Enter your array");
        alert.setHeaderText("Array type selection!");
        alert.setContentText(
                "Choose your array input.\nPlease note that either selection can't have an array greater than 300 with numbers larger than 700. Also, a number can't be negative.");

        ButtonType buttonTypeOne = new ButtonType("Write your array");
        ButtonType buttonTypeTwo = new ButtonType("Select your array from a text file");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeOne) {
            initialiseRectangleBars(writeYourArraySelection(" "));
        } else if (result.get() == buttonTypeTwo) {
            FileChooser fileChooser = new FileChooser();
            Node source = (Node) e.getSource();
            File selectedFile = fileChooser.showOpenDialog(source.getScene().getWindow());
            if (selectedFile == null) {
                Main.alertDialogIllegal("You have choosen no file...");
                return;
            } // End of method
            if (!selectedFile.getName().endsWith(".txt")) {
                Main.alertDialogIllegal("Please choose a .txt file");
                return;
            }
            String valueFromFile = readFile(selectedFile);
            initialiseRectangleBars(createIntegerArray(valueFromFile, " "));
        } //End of else-if
    } // End of method


    // First button press
    public int[] writeYourArraySelection(String regex) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input your numbers");
        dialog.setHeaderText("Input your array!");
        dialog.setContentText("Please enter your numbers followed by a space:");
        Optional<String> result = dialog.showAndWait();
        String getNumbers = null;
        if (result.isPresent())
            getNumbers = result.get();
        return createIntegerArray(getNumbers, regex);
    } // End of method

    // Method that checks if a string can be convert to an integer
    public int[] createIntegerArray(String getNumbers, String regex) {
        String gatherNumbers[] = getNumbers.split(regex);
        int copyNumbers[] = new int[gatherNumbers.length];
        for (int i = 0; i < gatherNumbers.length; i++) {
            if (tryParseInt(gatherNumbers[i]) == false || Integer.parseInt(gatherNumbers[i]) < 1
                    || Integer.parseInt(gatherNumbers[i]) > 700) {
                Main.alertDialogIllegal(
                        "You've entered a character that is either not a number or is above than 700 or is beyond 1...");
                // Maybe change the exception
                throw new IllegalArgumentException();
            }
            copyNumbers[i] = Integer.parseInt(gatherNumbers[i]);
        }
        return copyNumbers;
    }

    // Method that read files
    public String readFile(File selectedFile) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(selectedFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        Scanner scanner = new Scanner(bis);
        StringBuilder sb = new StringBuilder();
        String readLine = "";
        while (scanner.hasNextLine()) {
            readLine = scanner.nextLine();
            sb.append(readLine + " ");
            if (readLine.length() == 0)
                continue;
        } // End of loop
        scanner.close();
        return sb.toString();
    } // End of method

    // Create the error if the user enter a wrong value
    public void errorLabel(TextInputDialog dialog) {
        Label label = new Label("Please enter a value between 1ms and 10000ms");
        label.setTextFill(Paint.valueOf("red"));
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        dialog.getDialogPane().setExpandableContent(expContent);
    } // End of method

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

    // Use the generated array to create the rectangle bars
    public void createBars(int[] array) {
        // Create a new label for each value in the array
        for (int i = 0; i < array.length; i++) {
            Rectangle rectangle = new Rectangle(950 / array.length, array[i]);
            rectangle.setFill(Paint.valueOf("#305580"));
            int marginRight = (array.length < 100 ? marginRight = 3 : array.length < 150 ? 2 : 1);
            FlowPane.setMargin(rectangle, new Insets(0, 0, 0, marginRight));
            diagramPane.getChildren().add(rectangle);
        } // End of loop
    } // End of method

    // Try to parse an Integer. Put in in static maybe??
    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    } // End of method
} // End of class

