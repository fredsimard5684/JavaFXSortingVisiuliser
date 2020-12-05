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

public class SelectionSort extends SortingAlgorithms {

	public SelectionSort(int[] array) {
		super(array);
	}

	private void doSelectionSort(int[] arrayToSort, ArrayList<AnimationList> animationLists) {
		int firstValueOfLoop;
		int currentMinValue;
		for (int i = 0; i < arrayToSort.length; i++) {
			firstValueOfLoop = i;
			currentMinValue = firstValueOfLoop;
			for (int j = i + 1; j < arrayToSort.length; j++) {
				animationLists.add(new AnimationList(currentMinValue, j, SortStatus.COMPARE));
				if (arrayToSort[j] < arrayToSort[currentMinValue]) {
					animationLists.add(new AnimationList(currentMinValue, currentMinValue, SortStatus.REMOVE_FOCUS));
					currentMinValue = j;
				} // End of if
				animationLists.add(new AnimationList(j, j, SortStatus.REMOVE_FOCUS));
			} // End of internal loop
			int temp = arrayToSort[firstValueOfLoop];
			arrayToSort[firstValueOfLoop] = arrayToSort[currentMinValue];
			arrayToSort[currentMinValue] = temp;
			if (firstValueOfLoop != currentMinValue) {
				animationLists.add(new AnimationList(firstValueOfLoop, currentMinValue, SortStatus.SWAP));
				animationLists.add(new AnimationList(firstValueOfLoop, currentMinValue, SortStatus.COMPARE)); // makes
																												// the
																												// animation
																												// smoother,
																												// not
																												// necessary
			}
			animationLists.add(new AnimationList(firstValueOfLoop, currentMinValue, SortStatus.REMOVE_FOCUS));
			animationLists.add(new AnimationList(firstValueOfLoop, firstValueOfLoop, SortStatus.SORTED));
		} // End of external loop
	}

	@Override
	public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime)
			throws InterruptedException {
		if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
			Main.alertDialogIllegal(
					"There are no values to do the sort, please enter or generate a new array before pressing the button!");
			return;
		}
		doSelectionSort(getArrayToSort(), getAnimation()); // Call the selection sort algorithm
		// Create a new task with a new thread that will update the GUI while running
		// the loop
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				doAnimation(getAnimation(), flowPane, sleepTime);
				return null;
			} // End of method call
		}; // End of Task
		setTaskStatus(task, titlePane, flowPane, mainPane);
		new Thread(task).start();
	} // End of method

	private void doAnimation(ArrayList<AnimationList> animationLists, FlowPane flowPane, int sleepTime) throws InterruptedException {
		// Loop through all the animation where the bubble sort algorithm is doing
		// something
		for (AnimationList animationList : animationLists) {
			Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animationList.getFirstValue());
			Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animationList.getSecondValue());

			switch (animationList.getSortStatus()) {
			case COMPARE:
				String color = firstRect.getFill().toString().equals("0xffb3b8ff") ? "#58BC50": "#B34B89";
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf(color));
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
	private void setTaskStatus(Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {
		task.setOnRunning(e -> {
			changeButtonStatus(true, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(true); // the cancel button is the fourth child of the main anchor
															// pane
			createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, 100, "COMPARE MIN", 4);
			createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 196, 100, "SWAP", 5);
			createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 316, 100, "SORTED", 6);
			createLabelColor(mainPane, "-fx-background-color:#B34B89", 436, 100, "MIN VALUE", 7);
			SortingAlgorithms.setTimer(mainPane, false);
		});
		task.setOnCancelled(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			for (int i = 0; i < 4; i++)
				mainPane.getChildren().remove(4);
			SortingAlgorithms.setTimer(mainPane, true);
			SortingAlgorithms.getTimeElapse().setVisible(false);
		});
		task.setOnSucceeded(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(false);
			for (int i = 0; i < 4; i++)
				mainPane.getChildren().remove(4);
			SortingAlgorithms.setTimer(mainPane, true);
		});
	} // End of method
}
