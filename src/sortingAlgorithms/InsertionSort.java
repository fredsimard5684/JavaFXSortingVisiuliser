package sortingAlgorithms;

import java.util.ArrayList;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
	
	private void doInsertionSort() {
		int i, j;
		ArrayList<Integer> list = new ArrayList<>();
		for (i = 1; i < getArrayToSort().length; i++) {
			int temp = getArrayToSort()[i];
			getAnimation().add(new AnimationList(i, i, SortStatus.TEMP));
			j = i;
			getAnimation().add(new AnimationList(j, j, SortStatus.COMPARE)); //there's two j variables because we don't actually know where the temp value is
			while (j > 0 && getArrayToSort()[j-1] >= temp) {
				list.add(j);
				getAnimation().add(new AnimationList(j, j-1, SortStatus.SHIFT));
				getArrayToSort()[j] = getArrayToSort()[j-1];
				--j;
				getAnimation().add(new AnimationList(j, j, SortStatus.COMPARE));
			} //End of while loop
			//Remove the color on the values that have been switched
//
			if (!list.isEmpty())
				for (Integer integer : list) 
					getAnimation().add(new AnimationList(integer, integer, SortStatus.REMOVE_SHIFT));
			getAnimation().add(new AnimationList(j, j, SortStatus.SWAP));
			getAnimation().add(new AnimationList(j, j, SortStatus.REMOVE_FOCUS));
			getArrayToSort()[j] = temp;
		} //End of for loop
	}  //End of method

	@Override
	public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime)
			throws InterruptedException {
		if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
			Main.alertDialogIllegal(
					"There are no values to do the sort, please enter or generate a new array before pressing the button!");
			return;
		}
		doInsertionSort(); // Call the insertion sort algorithm so that the animation can be displayed
		Rectangle tempRectangle = createTempRectangle(700, Paint.valueOf("red"));
		Text tempText = createTempText();
		//Adding the temp values to the scene
		mainPane.getChildren().add(tempRectangle);
		mainPane.getChildren().add(tempText);
		// Create a new task with a new thread that will update the GUI while running
		// the loop
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// Loop through all the animation where the bubble sort algorithm is doing
				// something
				for (AnimationList animationList : getAnimation()) {
					Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animationList.getFirstValue());
					Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animationList.getSecondValue());
					if (isCancelled())
						return null;
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
							;
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
						Platform.runLater(() -> {
							tempRectangle.setHeight(firstRect.getHeight());	
						});
						break;
					case SHIFT:
						Platform.runLater(() -> {
							firstRect.setFill(Paint.valueOf("#ABB37B"));
							firstRect.setHeight(secondRect.getHeight());							
						});
						Thread.sleep(sleepTime);
						break;
					case REMOVE_SHIFT:
						Platform.runLater(() -> {
							firstRect.setFill(Paint.valueOf("#305580"));							
						});
						break;
					default:
						break;
					} // End of switch
				} // End of for
				//I put the animation here to prevent memory overflow
				Platform.runLater(() -> {
				for (int i = 0; i < getArrayToSort().length; i++) {
						Rectangle rectangle = ((Rectangle) flowPane.getChildren().get(i));
						rectangle.setFill(Paint.valueOf("#8BA9CC"));
					}
				});
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

		
	}
	
	private Rectangle createTempRectangle(int height, Paint color) {
		Rectangle rectangle = new Rectangle();
		rectangle.setWidth(29);
		rectangle.setHeight(height);
		rectangle.setFill(color);
		AnchorPane.setRightAnchor(rectangle, 18.0);
		AnchorPane.setBottomAnchor(rectangle, 79.0);
		return rectangle;
	}
	
	private Text createTempText() {
		Text text = new Text();
		text.setText("Temp value");
		text.setRotate(-90);
		text.setFont(Font.font("valera", 18));
		AnchorPane.setRightAnchor(text, 44.0);
		AnchorPane.setBottomAnchor(text, 120.0);
		return text;
	}

	@Override
	protected void changeButtonStatus(boolean isDisable, Task<Void> task, AnchorPane titlePane, FlowPane flowPane,
			AnchorPane mainPane) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createLabelColor(AnchorPane mainPane, String color, double layoutY, String text, int childPosition) {
		// TODO Auto-generated method stub
		
	}

}
