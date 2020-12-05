package sortingAlgorithms;

public class AnimationList {
	//Instance variables
	private int firstValue, secondValue, thirdValue;
	private SortStatus sortStatus;
	
	//1st Constructor
	public AnimationList(int firstValue, int secondValue, SortStatus sortStatus) {
		this.firstValue = firstValue;
		this.secondValue = secondValue;
		this.sortStatus = sortStatus;
	}//End of constructor

	//2nd constructor
	public AnimationList(int firstValue, int secondValue, int thirdValue, SortStatus sortStatus) {
		this.firstValue = firstValue;
		this.secondValue = secondValue;
		this.thirdValue = thirdValue;
		this.sortStatus = sortStatus;
	}
	
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

	public int getThirdValue() {
		return thirdValue;
	}

	public void setThirdValue(int thirdValue) {
		this.thirdValue = thirdValue;
	}
} //End of class
