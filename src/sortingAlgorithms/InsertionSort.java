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
	private void doInsertionSort(int[] arrayToSort, ArrayList<AnimationList> animationList) {
		int i, j;
		for (i = 1; i < arrayToSort.length; i++) {
			int temp = arrayToSort[i];
			j = i;
			
			animationList.add(new AnimationList(i, i - 1, SortStatus.TEMP)); // there's two j variables because we
																				// don't
																				// actually know where the temp value is
			// Try to find a value that is inferior to the temp value
			while (j > 0 && arrayToSort[j - 1] >= temp) {
				arrayToSort[j] = arrayToSort[j - 1];

				animationList.add(new AnimationList(j, j - 1, SortStatus.SHIFT));
				animationList.add(new AnimationList(j, j, SortStatus.REMOVE_FOCUS));

				--j;
				
				if (j > 0)
					animationList.add(new AnimationList(j, j - 1, SortStatus.COMPARE));

			} // End of while loop
			arrayToSort[j] = temp;

			animationList.add(new AnimationList(j, j, SortStatus.INSERT_VAL));
			if (j > 0)
				animationList.add(new AnimationList(j, j - 1, SortStatus.REMOVE_FOCUS));
			else 
				animationList.add(new AnimationList(0, 0, SortStatus.REMOVE_FOCUS));
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
		doInsertionSort(getArrayToSort(), getAnimation());

		// Creating temp rectangle and text to show what the temp value is
		Rectangle tempRectangle = createTempRectangle(700, Paint.valueOf("#58BC50"));
		Text tempText = createTempText();

		// Adding the temp values to the scene
		mainPane.getChildren().add(tempRectangle);
		mainPane.getChildren().add(tempText);

		// Create a new task with a new thread that will update the GUI while running
		// the loop
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				doAnimation(getAnimation(), flowPane, sleepTime, tempRectangle);
				return null;
			} // End of method call
		}; // End of Task
		setTaskStatus(task, titlePane, flowPane, mainPane);

		new Thread(task).start();

	} // End of method

	private void doAnimation(ArrayList<AnimationList> animationList, FlowPane flowPane, int sleepTime, Rectangle tempRectangle) throws InterruptedException {
		// Loop through all the animation where the bubble sort algorithm is doing
		// something
		for (AnimationList animation : animationList) {
			Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animation.getFirstValue());
			Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animation.getSecondValue());

			switch (animation.getSortStatus()) {
			case COMPARE:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("turquoise"));
					secondRect.setFill(Paint.valueOf("#58BC50"));
				});
				Thread.sleep(sleepTime);
				break;
			case INSERT_VAL:
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("#FFB3B8"));
//					tempRectangle.setFill(Paint.valueOf("#FFB3B8"));
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
				Platform.runLater(() -> {
					firstRect.setFill(Paint.valueOf("turquoise"));
					secondRect.setFill(Paint.valueOf("#58BC50"));
					tempRectangle.setHeight(firstRect.getHeight());
				});
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
	private void setTaskStatus(Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {
		task.setOnRunning(e -> {
			changeButtonStatus(true, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(true); // the cancel button is the fourth child of the main anchor
															// pane
			createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, 100, "COMPARE TEMP", 6);
			createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 196, 100, "INSERT TEMP", 7);
			createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 316, 100, "SORTED", 8);
			createLabelColor(mainPane, "-fx-background-color:#ABB37B", 436, 100, "SHIFT", 9);
			createLabelColor(mainPane, "-fx-background-color:turquoise", 556,100, "CURRENT VALUE", 10);
			Main.setTimer(mainPane, false);
		});

		task.setOnCancelled(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			for (int i = 0; i < 7; i++)
				mainPane.getChildren().remove(4);
			Main.setTimer(mainPane, true);
			Main.getTimeElapse().setVisible(false);
		});

		task.setOnSucceeded(e -> {
			changeButtonStatus(false, task, titlePane, flowPane, mainPane);
			mainPane.getChildren().get(3).setVisible(false);
			for (int i = 0; i < 7; i++)
				mainPane.getChildren().remove(4);
			Main.setTimer(mainPane, true);
		});
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
