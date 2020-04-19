package sortingAlgorithms;

import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;

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

    protected void changeButtonStatus(boolean isDisable, Task<Void> task, AnchorPane titlePane, FlowPane flowPane,
                                      AnchorPane mainPane) {
        for (int i = 0; i < titlePane.getChildren().size(); i++)
            titlePane.getChildren().get(i).setDisable(isDisable);
        ((Button) mainPane.getChildren().get(3)).setOnAction(e -> {
            task.cancel();
            flowPane.getChildren().clear();
            mainPane.getChildren().get(3).setVisible(false);
        });
    }

	protected void createLabelColor(AnchorPane mainPane, String color, double layoutX, int width, String text, int childPosition) {
		Label label = new Label();
		label.setLayoutX(layoutX);
		label.setStyle(color);
		label.setPrefWidth(width);
		label.setPrefHeight(24);
		label.setTextFill(Paint.valueOf("white"));
		label.setAlignment(Pos.CENTER);
		label.setText(text);
		AnchorPane.setTopAnchor(label, 87.0);
		mainPane.getChildren().add(childPosition, label);
	}

    public abstract void sort(FlowPane flowPane, AnchorPane anchorPane, AnchorPane mainPane, int sleepTime) throws InterruptedException;

}
