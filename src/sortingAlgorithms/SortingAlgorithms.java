package sortingAlgorithms;

import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public abstract class SortingAlgorithms {
	private int[] arrayToSort;
	private ArrayList<AnimationList> animation;

	
	// Contructor
		public SortingAlgorithms(int[] array) {
			this.arrayToSort = array;
			animation = new ArrayList<AnimationList>();
		} // End of constructor
		
		public int[] getArrayToSort() {
			return arrayToSort;
		}

		public void setArrayToSort(int[] arrayToSort) {
			this.arrayToSort = arrayToSort;
		}

		public ArrayList<AnimationList> getAnimation() {
			return animation;
		}

		public void setAnimation(ArrayList<AnimationList> animation) {
			this.animation = animation;
		}

		public abstract void sort(FlowPane flowPane, AnchorPane anchorPane, AnchorPane mainPane, int sleepTime) throws InterruptedException;
		protected abstract void changeButtonStatus(boolean isDisable, Task<Void> task, AnchorPane titlePane, FlowPane flowPane,
				AnchorPane mainPane);
		protected abstract void createLabelColor(AnchorPane mainPane, String color, double layoutY, String text, int childPosition);
}
