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
		
		if (array[left] > array[center]) {
			swap(array, left, center, animationLists);
			animationLists.add(new AnimationList(left, center, SortStatus.MEDIAN_SORT));
			animationLists.add(new AnimationList(left, center, SortStatus.REMOVE_FOCUS));
		}  else {
			
		}
		if (array[left] > array[right]) {
			swap(array, left, right, animationLists);
			animationLists.add(new AnimationList(left, right, SortStatus.MEDIAN_SORT));
			animationLists.add(new AnimationList(left, right, SortStatus.REMOVE_FOCUS));
		}
		if (array[center] > array[right]) {
			swap(array, center, right, animationLists);
			animationLists.add(new AnimationList(center, right, SortStatus.MEDIAN_SORT));
			animationLists.add(new AnimationList(center, right, SortStatus.REMOVE_FOCUS));
		}

		swap(array, center, right - 1, animationLists); // put the pivot at right -1
		animationLists.add(new AnimationList(center, right - 1, SortStatus.MEDIAN_SORT));
		animationLists.add(new AnimationList(center, right - 1, SortStatus.REMOVE_FOCUS));
		animationLists.add(new AnimationList(right - 1, right - 1, SortStatus.PIVOT));
		return right - 1;

	}

	// Do the partitioning of the array
	private int partition(int[] array, int left, int right, int pivot, ArrayList<AnimationList> animationLists) {
		int leftptr = left;
		int rightptr = right - 1; // At the pivot
		animationLists.add(new AnimationList(leftptr, rightptr - 1, SortStatus.COMPARE));

		while (leftptr < rightptr) {

			while (array[++leftptr] < array[pivot]) {
				animationLists.add(new AnimationList(leftptr - 1, leftptr - 1, SortStatus.REMOVE_FOCUS));
				animationLists.add(new AnimationList(leftptr, rightptr - 1, SortStatus.COMPARE));

			}
			if (array[leftptr] >= array[pivot]) {
				animationLists.add(new AnimationList(leftptr - 1, leftptr - 1, SortStatus.REMOVE_FOCUS));
				// if (leftptr != rightptr)
				// animationLists.add(new AnimationList(leftptr, rightptr - 1,
				// SortStatus.COMPARE));
			}

			while (array[--rightptr] > array[pivot]) {
				if (rightptr + 1 != pivot)
					animationLists.add(new AnimationList(rightptr + 1, rightptr + 1, SortStatus.REMOVE_FOCUS));
				animationLists.add(new AnimationList(leftptr, rightptr, SortStatus.COMPARE));
			}

			if (array[rightptr] <= array[pivot]) {
				if (rightptr + 1 != pivot)
					animationLists.add(new AnimationList(rightptr + 1, rightptr + 1, SortStatus.REMOVE_FOCUS));
				// if (leftptr != rightptr)
				// animationLists.add(new AnimationList(leftptr, rightptr, SortStatus.COMPARE));
			}

			if (leftptr < rightptr) {
				animationLists.add(new AnimationList(leftptr, rightptr, SortStatus.COMPARE)); // Only if the values to
																								// compare are not the
																								// one that will be swap
				swap(array, leftptr, rightptr, animationLists);
				animationLists.add(new AnimationList(leftptr, rightptr, SortStatus.SWAP));
				animationLists.add(new AnimationList(leftptr, rightptr, SortStatus.REMOVE_FOCUS));
				animationLists.add(new AnimationList(leftptr, rightptr - 1, SortStatus.COMPARE));
			}
		}
		swap(array, leftptr, right - 1, animationLists); // swap the pivot value with leftptr
		animationLists.add(new AnimationList(leftptr, right - 1, SortStatus.SWAP));
		animationLists.add(new AnimationList(leftptr, right - 1, SortStatus.REMOVE_FOCUS));
		animationLists.add(new AnimationList(leftptr, leftptr, SortStatus.SORTED));
		return leftptr; // new pivot
	} // End of method

	// Do a manual sort which allowed to save time if the partition is relatively
	// small
	private void manualSort(int[] array, int left, int right, int size, ArrayList<AnimationList> animationLists) {
		if (size <= 1) {
			animationLists.add(new AnimationList(left, left, SortStatus.MANUAL_SORT));
			animationLists.add(new AnimationList(left, left, SortStatus.SORTED));
			return;
		} //End of if
		
		if (size <= 2) {
			if(array[left] > array[right]) {
			swap(array, left, right, animationLists);
			animationLists.add(new AnimationList(left, right, SortStatus.MANUAL_SORT));
			animationLists.add(new AnimationList(left, right, SortStatus.SORTED));
			} else { //Prevent repeated execution
				animationLists.add(new AnimationList(left, right, SortStatus.MANUAL_SORT));
				animationLists.add(new AnimationList(left, right, SortStatus.SORTED));
			} //End of else
		} else { //If size is 3
			//First compare
			if (array[left] > array[right - 1]) { //right - 1 is the center
				swap(array, left, right -1, animationLists);
			} 
			//Second compare
			if (array[left] > array[right])
				swap(array, left, right, animationLists);
			animationLists.add(new AnimationList(left, left, SortStatus.SORTED));
			
			//Third compare
			if (array[right - 1] > array[right])
				swap(array, right - 1, right, animationLists);
			animationLists.add(new AnimationList(right - 1, right, SortStatus.SORTED));
		} //End of else
	} // End of method

	@Override
	public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime)
			throws InterruptedException {
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

	@Override
	protected void setTaskStatus(Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {
		task.setOnRunning(e -> {
			changeButtonStatus(true, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(true); // the cancel button is the fourth child of the main anchor
															// pane
			createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, "COMPARE MIN", 4);
			createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 196, "SWAP", 5);
			createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 316, "SORTED", 6);
			createLabelColor(mainPane, "-fx-background-color:#B34B89", 436, "MIN VALUE", 7);
			Main.setTimer(mainPane, false);
		});
		task.setOnCancelled(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			for (int i = 0; i < 4; i++)
				mainPane.getChildren().remove(4);
			Main.setTimer(mainPane, true);
			Main.getTimeElapse().setVisible(false);
		});
		task.setOnSucceeded(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(false);
			for (int i = 0; i < 4; i++)
				mainPane.getChildren().remove(4);
			Main.setTimer(mainPane, true);
		});

	} // End of method

	private void doAnimation(ArrayList<AnimationList> animationLists, FlowPane flowPane, int sleepTime)
			throws InterruptedException {

		for (AnimationList animation : animationLists) {
			Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animation.getFirstValue());
			Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animation.getSecondValue());
			switch (animation.getSortStatus()) {
			case COMPARE:
				if (!firstRect.getFill().toString().equals("0x58bc50ff")
						&& !secondRect.getFill().toString().equals("0x58bc50ff")) {
					Platform.runLater(() -> {
						firstRect.setFill(Paint.valueOf("#58BC50"));
						secondRect.setFill(Paint.valueOf("#58BC50"));
					});
					Thread.sleep(sleepTime);
				}
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
				Platform.runLater(() -> firstRect.setFill(Paint.valueOf("yellow")));
				break;
			case LEFT_POINTER:
				Platform.runLater(() -> firstRect.setFill(Paint.valueOf("#4B807A")));
				Thread.sleep(sleepTime);
				break;
			case RIGHT_POINTER:
				Platform.runLater(() -> firstRect.setFill(Paint.valueOf("#3BCCBE")));
				Thread.sleep(sleepTime);
				break;
			default:
				break;
			} // End of switch
		}
	}

	@Override
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
		label.setPrefWidth(100);
		label.setPrefHeight(24);
		label.setTextFill(Paint.valueOf("white"));
		label.setAlignment(Pos.CENTER);
		label.setText(text);
		AnchorPane.setTopAnchor(label, 87.0);
		mainPane.getChildren().add(childPosition, label);

	} // End of method

} // End of class
