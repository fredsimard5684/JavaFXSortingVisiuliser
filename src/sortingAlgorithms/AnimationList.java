package sortingAlgorithms;

public class AnimationList {
	private int firstValue, secondValue;
	private SortStatus sortStatus;
	
	public AnimationList(int firstValue, int secondValue, SortStatus sortStatus) {
		this.firstValue = firstValue;
		this.secondValue = secondValue;
		this.sortStatus = sortStatus;
	}
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
	
}
