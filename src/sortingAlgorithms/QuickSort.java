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

public class QuickSort extends SortingAlgorithms {

	public QuickSort(int[] array) {
		super(array);
		// TODO Auto-generated constructor stub
	} // End of constructor

	private void doQuickSort(int[] array, int left, int right, ArrayList<AnimationList> animationLists) {
		int size = right - left + 1;
		if (size <= 3) { // This will allow to save time if the array of the partition is small enough
			manualSort(array, left, right, size, animationLists);
		} else {
			int median = doMedianOf3(array, left, right, animationLists);
			int index = partition(array, left, right, median, animationLists);
			doQuickSort(array, left, index - 1, animationLists);
			doQuickSort(array, index + 1, right, animationLists);
		} // End of else
	} // End of method

	// Do the swap
	private void swap(int[] array, int num1, int num2, ArrayList<AnimationList> animationLists) {
		int temp = array[num1];
		array[num1] = array[num2];
		array[num2] = temp;

		animationLists.add(new AnimationList(num1, num2, SortStatus.SWAP));
		animationLists.add(new AnimationList(num1, num2, SortStatus.REMOVE_FOCUS));
		// MAYBE COMPARE TODO
	}

	private int doMedianOf3(int[] array, int left, int right, ArrayList<AnimationList> animationLists) {
		int center = (left + right) / 2;

		// First compare
		animationLists.add(new AnimationList(left, center, SortStatus.MEDIAN_COMPARE));
		if (array[left] > array[center])
			swap(array, left, center, animationLists);
		else
			animationLists.add(new AnimationList(left, center, SortStatus.REMOVE_FOCUS));

		// Second compare
		animationLists.add(new AnimationList(left, right, SortStatus.MEDIAN_COMPARE));
		if (array[left] > array[right])
			swap(array, left, right, animationLists);
		else
			animationLists.add(new AnimationList(left, right, SortStatus.REMOVE_FOCUS));

		// Third compare
		animationLists.add(new AnimationList(center, right, SortStatus.MEDIAN_COMPARE));
		if (array[center] > array[right])
			swap(array, center, right, animationLists);
		else
			animationLists.add(new AnimationList(center, right, SortStatus.REMOVE_FOCUS));

		// Setting the pivot
		swap(array, center, right - 1, animationLists); // put the pivot at right -1
		animationLists.add(new AnimationList(right - 1, right - 1, SortStatus.PIVOT));
		return right - 1;

	}

	// Do the partitioning of the array
	private int partition(int[] array, int left, int right, int pivot, ArrayList<AnimationList> animationLists) {
		int leftptr = left;
		int rightptr = right - 1; // At the pivot
		animationLists.add(new AnimationList(leftptr, rightptr - 1, SortStatus.COMPARE));

		while (leftptr < rightptr) { // While left position isn't superior or equals to right position

			while (array[++leftptr] < array[pivot]) {
				animationLists.add(new AnimationList(leftptr - 1, leftptr - 1, SortStatus.REMOVE_FOCUS));
				animationLists.add(new AnimationList(leftptr, rightptr - 1, SortStatus.COMPARE));

			} // End of leftptr while

			if (array[leftptr] >= array[pivot]) // Make sure to remove the focus
				animationLists.add(new AnimationList(leftptr - 1, leftptr - 1, SortStatus.REMOVE_FOCUS));

			while (array[--rightptr] > array[pivot]) {
				if (rightptr + 1 != pivot) // Make sure that the color of the pivot doesn't get overwrite
					animationLists.add(new AnimationList(rightptr + 1, rightptr + 1, SortStatus.REMOVE_FOCUS));
				animationLists.add(new AnimationList(leftptr, rightptr, SortStatus.COMPARE));
			} // End of rightptr

			if (array[rightptr] <= array[pivot]) // Make sure to remove the focus unless it is the pivot
				if (rightptr + 1 != pivot)
					animationLists.add(new AnimationList(rightptr + 1, rightptr + 1, SortStatus.REMOVE_FOCUS));

			if (leftptr < rightptr) {
				animationLists.add(new AnimationList(leftptr, rightptr, SortStatus.COMPARE)); // Only if the values to
																								// compare are not the
																								// one that will be swap
				swap(array, leftptr, rightptr, animationLists);
				animationLists.add(new AnimationList(leftptr, rightptr - 1, SortStatus.COMPARE));
			}
		} // End of external while

		swap(array, leftptr, right - 1, animationLists); // swap the pivot value with leftptr
		animationLists.add(new AnimationList(leftptr, leftptr, SortStatus.SORTED)); // We are sure that left value is
																					// sorted
		return leftptr; // new pivot
	} // End of method

	// Do a manual sort which allowed to save time if the partition is relatively
	// small
	private void manualSort(int[] array, int left, int right, int size, ArrayList<AnimationList> animationLists) {
		if (size <= 1) {
			animationLists.add(new AnimationList(left, left, SortStatus.MANUAL_COMPARE));
			animationLists.add(new AnimationList(left, left, SortStatus.SORTED));
			return;
		} // End of if

		if (size <= 2) {
			animationLists.add(new AnimationList(left, right, SortStatus.MANUAL_COMPARE));
			if (array[left] > array[right])
				swap(array, left, right, animationLists);
			animationLists.add(new AnimationList(left, right, SortStatus.SORTED)); // We are sure that it is sorted

		} else { // If size is 3

			// First compare
			animationLists.add(new AnimationList(left, right, SortStatus.MANUAL_COMPARE));
			if (array[left] > array[right - 1]) // right - 1 is the center
				swap(array, left, right - 1, animationLists);
			else
				animationLists.add(new AnimationList(left, right, SortStatus.REMOVE_FOCUS)); // Can't be sure that left
																								// or right is sorted

			// Second compare
			animationLists.add(new AnimationList(left, right, SortStatus.MANUAL_COMPARE));
			if (array[left] > array[right])
				swap(array, left, right, animationLists);
			else
				animationLists.add(new AnimationList(left, right, SortStatus.REMOVE_FOCUS)); // Can't be sure that right
																								// is sorted
			animationLists.add(new AnimationList(left, left, SortStatus.SORTED)); // Left is sorted for sure

			// Third compare
			animationLists.add(new AnimationList(right - 1, right, SortStatus.MANUAL_COMPARE));
			if (array[right - 1] > array[right])
				swap(array, right - 1, right, animationLists);
			animationLists.add(new AnimationList(right - 1, right, SortStatus.SORTED)); // Center and right is sorted
		} // End of else
	} // End of method

	@Override
	// Do the sort with the animation
	public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime)
			throws InterruptedException {
		if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
			Main.alertDialogIllegal(
					"There are no values to do the sort, please enter or generate a new array before pressing the button!");
			return;
		}
		doQuickSort(getArrayToSort(), 0, getArrayToSort().length - 1, getAnimation());

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

	// Set the task on finished, on running and on cancelled
	private void setTaskStatus(Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {
		task.setOnRunning(e -> {
			changeButtonStatus(true, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(true); // the cancel button is the fourth child of the main anchor
															// pane
			createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, 110, "COMPARE PIVOT", 4);
			createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 196, 110, "SWAP", 5);
			createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 316, 110, "SORTED", 6);
			createLabelColor(mainPane, "-fx-background-color:turquoise", 436, 110, "MEDIAN COMPARE", 7);
			createLabelColor(mainPane, "-fx-background-color:gray", 556, 110, "MANUAL COMPARE", 8);
			createLabelColor(mainPane, "-fx-background-color:#ABB37B", 676, 110, "PIVOT", 9);
			SortingAlgorithms.setTimer(mainPane, false);
		});
		task.setOnCancelled(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			for (int i = 0; i < 6; i++)
				mainPane.getChildren().remove(4);
			SortingAlgorithms.setTimer(mainPane, true);
			SortingAlgorithms.getTimeElapse().setVisible(false);
		});
		task.setOnSucceeded(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(false);
			for (int i = 0; i < 6; i++)
				mainPane.getChildren().remove(4);
			SortingAlgorithms.setTimer(mainPane, true);
		});

	} // End of method

	// Method that selects and do the animation
	private void doAnimation(ArrayList<AnimationList> animationLists, FlowPane flowPane, int sleepTime)
			throws InterruptedException {

		for (AnimationList animation : animationLists) {
			Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animation.getFirstValue());
			Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animation.getSecondValue());
			switch (animation.getSortStatus()) {
			case COMPARE:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("#58BC50"));
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
			case PIVOT:
				Platform.runLater(() -> firstRect.setFill(Paint.valueOf("#ABB37B")));
				break;
			case MEDIAN_COMPARE:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("turquoise"));
					secondRect.setFill(Paint.valueOf("turquoise"));
				});
				Thread.sleep(sleepTime);
				break;
			case MANUAL_COMPARE:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("gray"));
					secondRect.setFill(Paint.valueOf("gray"));
				});
				Thread.sleep(sleepTime);
				break;
			default:
				break;
			} // End of switch
		} // End of for
	} // End of method
} // End of class
