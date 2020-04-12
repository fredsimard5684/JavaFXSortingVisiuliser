package sortingAlgorithms;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public abstract class SortingAlgorithms {
	protected int[] arrayToSort;
	protected ArrayList<AnimationList> animation;

	
	// Contructor
		public SortingAlgorithms(int[] array) {
			this.arrayToSort = array;
			animation = new ArrayList<AnimationList>();
		} // End of constructor
		
		public abstract void sort(FlowPane flowPane, AnchorPane anchorPane, Button cancelButton) throws InterruptedException;
}
