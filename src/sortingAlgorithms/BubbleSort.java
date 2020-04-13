package sortingAlgorithms;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class BubbleSort extends SortingAlgorithms {

	// Contructor
	public BubbleSort(int[] array) {
		super(array);
	} // End of constructor

	// This method do the bubble sort algorithm and add the animation at the proper
	// placement
	private void doBubbleSort() {
		int endOfInternalLoop = 0;
		for (int i = 0; i < arrayToSort.length; i++) {
			for (int j = 0; j < arrayToSort.length - 1 - i; j++) {
				endOfInternalLoop = arrayToSort.length - i - 1;
				animation.add(new AnimationList(j, j + 1, SortStatus.COMPARE));
				// Swap the values
				if (arrayToSort[j] > arrayToSort[j + 1]) {
					int temp = arrayToSort[j];
					arrayToSort[j] = arrayToSort[j + 1];
					arrayToSort[j + 1] = temp;
					animation.add(new AnimationList(j, j + 1, SortStatus.SWAP));
					animation.add(new AnimationList(j, j + 1, SortStatus.COMPARE));
				}
				animation.add(new AnimationList(j, j + 1, SortStatus.REMOVE_FOCUS));
			} // End of internal loop
			animation.add(new AnimationList(endOfInternalLoop, endOfInternalLoop, SortStatus.SORTED));
		} // End of external loop
		animation.add(new AnimationList(0, 0, SortStatus.SORTED));
	} // End of method

	// THis method will display the bubble sort algorithm in the GUI
	public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime) throws InterruptedException {
		if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
			Main.alertDialogIllegal(
					"There are no values to do the sort, please enter or generate a new array before pressing the button!");
			return;
		}
		// This will calculate the number of time that the Thread is going to sleep to
		// be able to display the animation properly in the GUI
		doBubbleSort(); // Call the bubble sort algorithm so that the animation can be displayed
		// Create a new task with a new thread that will update the GUI while running
		// the loop
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// Loop through all the animation where the bubble sort algorithm is doing
				// something
				for (AnimationList animationList : animation) {
					Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animationList.getFirstValue());
					Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animationList.getSecondValue());
					if (isCancelled())
						return null;
					switch (animationList.getSortStatus()) {
					case COMPARE:
						Platform.runLater(() -> {
							firstRect.setFill(Paint.valueOf("#58BC50"));
							;
							secondRect.setFill(Paint.valueOf("#58BC50"));
						});
						Thread.sleep(sleepTime);
						break;
					case SWAP:
						Platform.runLater(() -> {
							firstRect.setFill(Paint.valueOf("#FFB3B8"));
							;
							secondRect.setFill(Paint.valueOf("#FFB3B8"));
							double tempHeight = firstRect.getHeight();
							firstRect.setHeight(secondRect.getHeight());
							secondRect.setHeight(tempHeight);
						});
						Thread.sleep(sleepTime);
						break;
					case REMOVE_FOCUS:
						Platform.runLater(() -> {
							firstRect.setFill(Paint.valueOf("#305580"));
							secondRect.setFill(Paint.valueOf("#305580"));
						});
						break;
					case SORTED:
						Platform.runLater(() -> {
							firstRect.setFill(Paint.valueOf("#8BA9CC"));
							secondRect.setFill(Paint.valueOf("#8BA9CC"));
						});
						break;
					default:
						break;
					} // End of switch
				} // End of for
				return null;
			} // End of method call
		}; // End of Task
		task.setOnRunning(e -> {
			changeButtonStatus(true, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(true); //the cancel button is the fourth child of the main anchor pane
			createLabelColor(mainPane, "-fx-background-color:#58BC50", 116, "COMPARE", 4);
			createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 146, "SWAP", 5);
			createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 176, "SORTED", 6);
			Main.setTimer(mainPane, false);
		});
		task.setOnCancelled(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			for (int i = 0; i < 3; i++)
				mainPane.getChildren().remove(4);
			Main.setTimer(mainPane, true);
			Main.getTimeElapse().setVisible(false);
		});
		task.setOnSucceeded(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(false);
			for (int i = 0; i < 3; i++)
				mainPane.getChildren().remove(4);
			Main.setTimer(mainPane, true);
		});
		new Thread(task).start();

	} // End of method

	// Set the buttons to visible or not visible
	private void changeButtonStatus(boolean isDisable, Task<Void> task, AnchorPane titlePane, FlowPane flowPane,
			AnchorPane mainPane) {
		for (int i = 0; i < titlePane.getChildren().size(); i++)
			titlePane.getChildren().get(i).setDisable(isDisable);
		((Button) mainPane.getChildren().get(3)).setOnAction(e -> {
			task.cancel();
			flowPane.getChildren().clear();
			mainPane.getChildren().get(3).setVisible(false);
		});
	}

	private void createLabelColor(AnchorPane mainPane, String color, double layoutY, String text, int childPosition) {
		Label label = new Label();
		label.setLayoutY(layoutY);
		label.setStyle(color);
		label.setPrefWidth(70);
		label.setPrefHeight(24);
		label.setTextFill(Paint.valueOf("white"));
		label.setAlignment(Pos.CENTER);
		label.setText(text);
		AnchorPane.setRightAnchor(label, 10.0);
		mainPane.getChildren().add(childPosition, label);
		//((AnchorPane) mainPane.getChildren().get(1)).getChildren()
	}
	
	
} // End of class
