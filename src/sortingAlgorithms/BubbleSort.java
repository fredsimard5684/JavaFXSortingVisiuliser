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
		for (int i = 0; i < getArrayToSort().length; i++) {
			for (int j = 0; j < getArrayToSort().length - 1 - i; j++) {
				endOfInternalLoop = getArrayToSort().length - i - 1;
				getAnimation().add(new AnimationList(j, j + 1, SortStatus.COMPARE));
				// Swap the values
				if (getArrayToSort()[j] > getArrayToSort()[j + 1]) {
					int temp = getArrayToSort()[j];
					getArrayToSort()[j] = getArrayToSort()[j + 1];
					getArrayToSort()[j + 1] = temp;
					getAnimation().add(new AnimationList(j, j + 1, SortStatus.SWAP));
					getAnimation().add(new AnimationList(j, j + 1, SortStatus.COMPARE));
				}
				getAnimation().add(new AnimationList(j, j + 1, SortStatus.REMOVE_FOCUS));
			} // End of internal loop
			getAnimation().add(new AnimationList(endOfInternalLoop, endOfInternalLoop, SortStatus.SORTED));
		} // End of external loop
		getAnimation().add(new AnimationList(0, 0, SortStatus.SORTED));
	} // End of method

	// This method will display the bubble sort algorithm in the GUI
	public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime)
			throws InterruptedException {
		if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
			Main.alertDialogIllegal(
					"There are no values to do the sort, please enter or generate a new array before pressing the button!");
			return;
		}
		doBubbleSort(); // Call the bubble sort algorithm so that the animation can be displayed
		// Create a new task with a new thread that will update the GUI while running
		// the loop
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				doAnimation(flowPane, sleepTime);
				return null;
			} // End of method call
		}; // End of Task
		setTaskStatus(task, titlePane, flowPane, mainPane);
		new Thread(task).start();

	} // End of method

	private void doAnimation(FlowPane flowPane, int sleepTime) throws InterruptedException {
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
	}

	// Make the code more cleaner
	protected void setTaskStatus(Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {
		task.setOnRunning(e -> {
			changeButtonStatus(true, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(true); // the cancel button is the fourth child of the main anchor
															// pane
			createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, "COMPARE", 4);
			createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 176, "SWAP", 5);
			createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 276, "SORTED", 6);
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
	} // End of method

	// Set the buttons to visible or not visible
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

} // End of class
