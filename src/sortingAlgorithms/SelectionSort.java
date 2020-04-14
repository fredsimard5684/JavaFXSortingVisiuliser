package sortingAlgorithms;

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
	
	private void doSelectionSort() {
		int firstValueOfLoop;
		int currentMinValue;
		for (int i = 0; i < getArrayToSort().length; i++) {
			firstValueOfLoop = i;
			currentMinValue = firstValueOfLoop;
			for (int j = i + 1; j < getArrayToSort().length; j++) {
				getAnimation().add(new AnimationList(currentMinValue, j, SortStatus.COMPARE));
				if (getArrayToSort()[j] < getArrayToSort()[currentMinValue]) {
					getAnimation().add(new AnimationList(currentMinValue, currentMinValue, SortStatus.REMOVE_FOCUS));
					getAnimation().add(new AnimationList(firstValueOfLoop, firstValueOfLoop, SortStatus.FIRST_VALUE));
					currentMinValue = j;
				} //End of if
				getAnimation().add(new AnimationList(j, j, SortStatus.REMOVE_FOCUS));
			}//End of internal loop
			int temp = getArrayToSort()[firstValueOfLoop];
			getArrayToSort()[firstValueOfLoop] = getArrayToSort()[currentMinValue];
			getArrayToSort()[currentMinValue] = temp;
			if (firstValueOfLoop != currentMinValue) {
				getAnimation().add(new AnimationList(firstValueOfLoop, currentMinValue, SortStatus.SWAP));
				getAnimation().add(new AnimationList(firstValueOfLoop, currentMinValue, SortStatus.COMPARE)); //makes the animation smoother, not necessary				
			}
			getAnimation().add(new AnimationList(firstValueOfLoop, currentMinValue, SortStatus.REMOVE_FOCUS));
			getAnimation().add(new AnimationList(firstValueOfLoop, firstValueOfLoop, SortStatus.SORTED));
		} //End of external loop
	}

	@Override
	public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime)
			throws InterruptedException {
		if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
			Main.alertDialogIllegal(
					"There are no values to do the sort, please enter or generate a new array before pressing the button!");
			return;
		}
		doSelectionSort(); //Call the selection sort algorithm
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
									firstRect.setFill(Paint.valueOf("#B34B89"));
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
							case FIRST_VALUE:
								Platform.runLater(() -> {
									firstRect.setFill(Paint.valueOf("#ABB37B"));
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
					createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, "COMPARE", 4);
					createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 176, "SWAP", 5);
					createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 276, "SORTED", 6);
					createLabelColor(mainPane, "-fx-background-color:#ABB37B", 376, "FIRST VALUE", 7);
					createLabelColor(mainPane, "-fx-background-color:#B34B89", 476, "MIN VALUE", 8);
					Main.setTimer(mainPane, false);
				});
				task.setOnCancelled(e -> {
					changeButtonStatus(false, task, titlePane, flowPane, mainPane);
					for (int i = 0; i < 5; i++)
						mainPane.getChildren().remove(4);
					Main.setTimer(mainPane, true);
					Main.getTimeElapse().setVisible(false);
				});
				task.setOnSucceeded(e -> {
					changeButtonStatus(false, task, titlePane, flowPane, mainPane);
					mainPane.getChildren().get(3).setVisible(false);
					for (int i = 0; i < 5; i++)
						mainPane.getChildren().remove(4);
					Main.setTimer(mainPane, true);
				});
				new Thread(task).start();
	} //End of method

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
	} //End of method

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
		
	} //End of method

}
