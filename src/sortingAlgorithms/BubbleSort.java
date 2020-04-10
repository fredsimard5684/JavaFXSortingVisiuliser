package sortingAlgorithms;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class BubbleSort {
	private int[] arrayToSort;
	private ArrayList<AnimationList> animation;
	private FlowPane flowPane;
	
	public BubbleSort(int[] array, FlowPane flowPane) {
		this.arrayToSort = array;
		animation = new ArrayList<AnimationList>();
		this.flowPane = flowPane;
	}
	
	private void doBubbleSort() {
		int endOfInternalLoop = 0;
		for (int i = 0; i < arrayToSort.length; i++) {
			for (int j = 0; j < arrayToSort.length - 1 - i; j++) {
				endOfInternalLoop = arrayToSort.length - i - 1;
				animation.add(new AnimationList(j, j + 1, "COMPARE"));
				if (arrayToSort[j] > arrayToSort[j + 1]) {
					int temp =  arrayToSort[j];
					arrayToSort[j] = arrayToSort[j + 1];
					arrayToSort[j + 1] = temp;
					animation.add(new AnimationList(j, j + 1, "SWAP"));
				}
				animation.add(new AnimationList(j, j + 1, "REMOVE_FOCUS"));
			} //End of internal loop
			animation.add(new AnimationList(endOfInternalLoop, endOfInternalLoop, "SORTED"));
		} //End of external loop
		animation.add(new AnimationList(0, 0, "SORTED"));
	} //End of method
	public void sort() throws InterruptedException {
		doBubbleSort();
		for (AnimationList animationList : animation) {
			System.out.println(animationList.getSortStatus() + " " + animationList.getFirstValue() +  " " + animationList.getSecondValue());
		}
		Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				for (AnimationList animationList : animation) {
					Rectangle firstLabel = (Rectangle) flowPane.getChildren().get(animationList.getFirstValue());
					Rectangle secondLabel = (Rectangle) flowPane.getChildren().get(animationList.getSecondValue());
					if (animationList.getSortStatus().equals("COMPARE")) {
						Platform.runLater( ()-> {					
							firstLabel.setFill(Paint.valueOf("green"));;
							secondLabel.setFill(Paint.valueOf("green"));
						});
						Thread.sleep(1000);
					} else if (animationList.getSortStatus().equals("SWAP")) {
						Platform.runLater(() ->{
							firstLabel.setFill(Paint.valueOf("red"));;
							secondLabel.setFill(Paint.valueOf("red"));
							double tempHeight = firstLabel.getHeight();
							firstLabel.setHeight(secondLabel.getHeight());
							secondLabel.setHeight(tempHeight);
						});
						Thread.sleep(1000);
					} else if (animationList.getSortStatus().equals("REMOVE_FOCUS")) {
						Platform.runLater(() -> {
							firstLabel.setFill(Paint.valueOf("#305580"));
							secondLabel.setFill(Paint.valueOf("#305580"));				
						});
						Thread.sleep(1000);
					} else if (animationList.getSortStatus().equals("SORTED")) {
						Platform.runLater(() ->{
							firstLabel.setFill(Paint.valueOf("purple"));	
							secondLabel.setFill(Paint.valueOf("purple"));
						});
						Thread.sleep(1000);
					}
				}
				return null;
			}
		};
		new Thread(task).start();
	}
} //End of class
