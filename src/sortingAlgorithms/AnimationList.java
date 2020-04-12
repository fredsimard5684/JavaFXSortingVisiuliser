package sortingAlgorithms;

public class AnimationList {
	//Instance variables
	private int firstValue, secondValue;
	private SortStatus sortStatus;
	
	//Constructor
	public AnimationList(int firstValue, int secondValue, SortStatus sortStatus) {
		this.firstValue = firstValue;
		this.secondValue = secondValue;
		this.sortStatus = sortStatus;
	}//End of constructor
	
	//Getters and setters
	public int getFirstValue() {
		return firstValue;
	}
	public void setFirstValue(int firstValue) {
		this.firstValue = firstValue;
	}
	public int getSecondValue() {
		return secondValue;
	}
	public void setSecondValue(int secondValue) {
		this.secondValue = secondValue;
	}
	public SortStatus getSortStatus() {
		return sortStatus;
	}
	public void setSortStatus(SortStatus sortStatus) {
		this.sortStatus = sortStatus;
	}
	
} //End of class
