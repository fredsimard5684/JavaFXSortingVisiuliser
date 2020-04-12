package sortingAlgorithms;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public abstract class SortingAlgorithms {
	protected int[] arrayToSort;
	protected ArrayList<AnimationList> animation;
	protected FlowPane flowPane;
	protected AnchorPane titlePane;
	protected Button cancelButton;
	
	// Contructor
		public SortingAlgorithms(int[] array, FlowPane flowPane, AnchorPane titlePane, Button cancelButton) {
			this.arrayToSort = array;
			animation = new ArrayList<AnimationList>();
			this.flowPane = flowPane;
			this.titlePane = titlePane;
			this.cancelButton = cancelButton;
		} // End of constructor
		
		public abstract void sort() throws InterruptedException;
}
