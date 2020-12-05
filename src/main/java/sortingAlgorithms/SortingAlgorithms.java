package sortingAlgorithms;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class SortingAlgorithms {
    private int[] arrayToSort;
    private ArrayList<AnimationList> animation;
    private static Timer timer;
    private static Text timeElapse;


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

    //Setting a timer
    public static void setTimer(AnchorPane mainPane, boolean isFinished) {
        if (isFinished) {
            timer.cancel();
            timer.purge();
            return;
        }
        timer = new Timer();
        createTextElapse(mainPane);
        //Create a timer
        //Create a timer task
        final TimerTask timerTask = new TimerTask() {
            int interval = 0;
            @Override
            public void run() {
                Platform.runLater(() -> timeElapse.setText(String.format("Time: %d ms", interval)));
                interval++;
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1);
    }

    public static void createTextElapse(AnchorPane mainPane) {
        if (((AnchorPane) mainPane.getChildren().get(1)).getChildren().isEmpty()) {
            //Create a timer text
            timeElapse = new Text();
            timeElapse.setFill(Paint.valueOf("white"));
            timeElapse.setFont(Font.font("valera"));
            timeElapse.setFont(Font.font(24));
            timeElapse.setVisible(false);
            AnchorPane.setBottomAnchor(timeElapse, 23.9765625);
            AnchorPane.setLeftAnchor(timeElapse, 14.0);
            ((AnchorPane)mainPane.getChildren().get(1)).getChildren().add(timeElapse);
        }
        if(!timeElapse.isVisible()) timeElapse.setVisible(true);
    } //End of method

    public static Text getTimeElapse() {
        return timeElapse;
    }

    public abstract void sort(FlowPane flowPane, AnchorPane anchorPane, AnchorPane mainPane, int sleepTime) throws InterruptedException;

}
