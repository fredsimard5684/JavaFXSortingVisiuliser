package sortingAlgorithms;

import java.util.ArrayList;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class InsertionSort extends SortingAlgorithms {

	public InsertionSort(int[] array) {
		super(array);
		// TODO Auto-generated constructor stub
	}

	// Do the insertion sort algorithm
	private void doInsertionSort() {
		int i, j;
		for (i = 1; i < getArrayToSort().length; i++) {
			int temp = getArrayToSort()[i];
			j = i;
			getAnimation().add(new AnimationList(i, i, SortStatus.TEMP));
			getAnimation().add(new AnimationList(j, j, SortStatus.COMPARE)); // there's two j variables because we don't
																				// actually know where the temp value is
			// Try to find a value that is inferior to the temp value
			while (j > 0 && getArrayToSort()[j - 1] >= temp) {
				getArrayToSort()[j] = getArrayToSort()[j - 1];

				getAnimation().add(new AnimationList(j, j - 1, SortStatus.SHIFT));
				getAnimation().add(new AnimationList(j, j, SortStatus.REMOVE_FOCUS));

				--j;

				getAnimation().add(new AnimationList(j, j, SortStatus.COMPARE));
			} // End of while loop
			getArrayToSort()[j] = temp;

			getAnimation().add(new AnimationList(j, j, SortStatus.SWAP));
			getAnimation().add(new AnimationList(j, j, SortStatus.REMOVE_FOCUS));
		} // End of for loop
	} // End of method

	@Override
	public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime)
			throws InterruptedException {
		if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
			Main.alertDialogIllegal(
					"There are no values to do the sort, please enter or generate a new array before pressing the button!");
			return;
		}
		// Call the insertion sort algorithm so that the animation can be displayed
		doInsertionSort();

		// Creating temp rectangle and text to show what the temp value is
		Rectangle tempRectangle = createTempRectangle(700, Paint.valueOf("red"));
		Text tempText = createTempText();

		// Adding the temp values to the scene
		mainPane.getChildren().add(tempRectangle);
		mainPane.getChildren().add(tempText);

		// Create a new task with a new thread that will update the GUI while running the loop
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				doAnimation(flowPane, sleepTime, tempRectangle);
				return null;
			} // End of method call
		}; // End of Task
		setTaskStatus(task, titlePane, flowPane, mainPane);

		new Thread(task).start();

	} // End of method

	private void doAnimation(FlowPane flowPane, int sleepTime, Rectangle tempRectangle) throws InterruptedException {
		// Loop through all the animation where the bubble sort algorithm is doing
		// something
		for (AnimationList animationList : getAnimation()) {
			Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animationList.getFirstValue());
			Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animationList.getSecondValue());

			switch (animationList.getSortStatus()) {
			case COMPARE:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("#58BC50"));
					;
					tempRectangle.setFill(Paint.valueOf("#58BC50"));
				});
				Thread.sleep(sleepTime);
				break;
			case SWAP:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("#FFB3B8"));
					tempRectangle.setFill(Paint.valueOf("#FFB3B8"));
					firstRect.setHeight(tempRectangle.getHeight());
				});
				Thread.sleep(sleepTime);
				break;
			case REMOVE_FOCUS:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("#305580"));
					secondRect.setFill(Paint.valueOf("#305580"));
				});
				break;
			case TEMP:
				Platform.runLater(() -> tempRectangle.setHeight(firstRect.getHeight()));
				Thread.sleep(sleepTime);
				break;
			case SHIFT:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("#ABB37B"));
					firstRect.setHeight(secondRect.getHeight());
				});
				Thread.sleep(sleepTime);
				break;
			default:
				break;
			} // End of switch
		} // End of for
			// Set another color to show that the algorithm is sorted!
		Platform.runLater(() -> {
			for (int i = 0; i < getArrayToSort().length; i++) {
				Rectangle rectangle = ((Rectangle) flowPane.getChildren().get(i));
				rectangle.setFill(Paint.valueOf("#8BA9CC"));
			} // End of for loop
		});
	}

	// Make the code more cleaner
	protected void setTaskStatus(Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {
		task.setOnRunning(e -> {
			changeButtonStatus(true, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(true); // the cancel button is the fourth child of the main anchor
															// pane
			createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, "COMPARE", 6);
			createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 176, "SWAP", 7);
			createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 276, "SORTED", 8);
			createLabelColor(mainPane, "-fx-background-color:#ABB37B", 376, "SHIFT", 9);
			Main.setTimer(mainPane, false);
		});

		task.setOnCancelled(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			for (int i = 0; i < 6; i++)
				mainPane.getChildren().remove(4);
			Main.setTimer(mainPane, true);
			Main.getTimeElapse().setVisible(false);
		});

		task.setOnSucceeded(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(false);
			for (int i = 0; i < 6; i++)
				mainPane.getChildren().remove(4);
			Main.setTimer(mainPane, true);
		});
	} // End of method

	@Override
	// Disable the buttons if needed
	protected void changeButtonStatus(boolean isDisable, Task<Void> task, AnchorPane titlePane, FlowPane flowPane,
			AnchorPane mainPane) {
		for (int i = 0; i < titlePane.getChildren().size(); i++)
			titlePane.getChildren().get(i).setDisable(isDisable);
		((Button) mainPane.getChildren().get(3)).setOnAction(e -> {
			task.cancel();
			flowPane.getChildren().clear();
			mainPane.getChildren().get(3).setVisible(false);
		});

	} // End of method

	@Override
	// Create the labels that indicates wich color does what
	protected void createLabelColor(AnchorPane mainPane, String color, double layoutX, String text, int childPosition) {
		Label label = new Label();
		label.setLayoutX(layoutX);
		label.setStyle(color);
		label.setPrefWidth(80);
		label.setPrefHeight(24);
		label.setTextFill(Paint.valueOf("white"));
		label.setAlignment(Pos.CENTER);
		label.setText(text);
		AnchorPane.setTopAnchor(label, 87.0);
		mainPane.getChildren().add(childPosition, label);

	} // End of method

	private Rectangle createTempRectangle(int height, Paint color) {
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(29);
		rectangle.setHeight(height);
		rectangle.setFill(color);
		AnchorPane.setRightAnchor(rectangle, 18.0);
		AnchorPane.setBottomAnchor(rectangle, 79.0);
		return rectangle;
	} // End of method

	private Text createTempText() {
		Text text = new Text();
		text.setText("Temp value");
		text.setRotate(-90);
		text.setFont(Font.font("valera", 18));
		AnchorPane.setRightAnchor(text, 44.0);
		AnchorPane.setBottomAnchor(text, 120.0);
		return text;
	} // End of method
}
