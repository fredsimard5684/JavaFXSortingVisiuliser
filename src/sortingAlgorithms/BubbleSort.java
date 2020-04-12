package sortingAlgorithms;

import java.util.ArrayList;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class BubbleSort {
	// Instance variables
	private int[] arrayToSort;
	private ArrayList<AnimationList> animation;
	private FlowPane flowPane;
	private AnchorPane titlePane;
	private Button cancelButton;

	// Contructor
	public BubbleSort(int[] array, FlowPane flowPane, AnchorPane titlePane, Button cancelButton) {
		this.arrayToSort = array;
		animation = new ArrayList<AnimationList>();
		this.flowPane = flowPane;
		this.titlePane = titlePane;
		this.cancelButton = cancelButton;

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
	public void sort() throws InterruptedException {
		if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
			Main.alertDialogIllegal(
					"There are no values to do the sort, please enter or generate a new array before pressing the button!");
			return;
		}
		// This will calculate the number of time that the Thread is going to sleep to
		// be able to display the animation properly in the GUI
		final int SLEEP_TIME = (int) (10000 / (Math.pow(arrayToSort.length, 1.6)));
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
						Thread.sleep(SLEEP_TIME);
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
						Thread.sleep(SLEEP_TIME);
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
			changeButtonStatus(true, task);
			cancelButton.setVisible(true);
		});
		task.setOnCancelled(e -> {
			changeButtonStatus(false, task);
		});
		task.setOnSucceeded(e -> {
			changeButtonStatus(false, task);
			cancelButton.setVisible(false);
		});
		new Thread(task).start();

	} // End of method

	// Set the buttons to visible or not visible
	public void changeButtonStatus(boolean isDisable, Task<Void> task) {
		for (int i = 0; i < titlePane.getChildren().size(); i++)
			titlePane.getChildren().get(i).setDisable(isDisable);
		cancelButton.setOnAction(e -> {
			task.cancel();
			flowPane.getChildren().clear();
			cancelButton.setVisible(false);
		});
	}
} // End of class
