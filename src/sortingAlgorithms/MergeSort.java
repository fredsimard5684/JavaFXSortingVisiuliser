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

import java.util.ArrayList;
import java.util.Arrays;

public class MergeSort extends SortingAlgorithms {
    private int[] workSpace;
    public MergeSort(int[] array) {
        super(array);
        workSpace = Arrays.copyOfRange(array, 0, array.length);
    }

    private void doMergeSort(int mainArray[], int low, int high, int[] workSpace, ArrayList<AnimationList> animationLists) {
        if (low == high) return;
        else {
            int middle = (low + high) / 2;
            //I put workSpace at the parameters of mainArray since when we're doing the merge, we're overwriting
            //the value of the mainarray so that it can be sort. In short, workSpace keep the orginial order of the main array
            //so that it can give the correct number to the mainArray, and sort it.
            doMergeSort(workSpace, low, middle, mainArray, animationLists);
            doMergeSort(workSpace, middle+ 1, high, mainArray, animationLists);
            merge(mainArray, low, middle, high, workSpace, animationLists);
        } //End of else
    } //End of method

    private void merge(int[] mainArray, int low, int middle, int high, int[] workSpace, ArrayList<AnimationList> animationLists) {
        int k = low; //Pointer in the main arrray
        int i = low; //Seperate the workspace
        int j = middle + 1; //Seperate the workspace

        while (i <= middle && j <= high) { //Add the lowest number to the workspace
            if (workSpace[i] <= workSpace[j]) {
                animationLists.add(new AnimationList(k, i, SortStatus.SWAP));
                mainArray[k++] = workSpace[i++];
            } else {
                animationLists.add(new AnimationList(k, j, SortStatus.SWAP));
                mainArray[k++] = workSpace[j++];
            }
        }
        //Adding the rest of the numbers
        while (i <= middle) {
            animationLists.add(new AnimationList(k, i, SortStatus.SWAP));
            mainArray[k++] = workSpace[i++];
        }
        while (j <= high) {
            animationLists.add(new AnimationList(k, j, SortStatus.SWAP));
            mainArray[k++] = workSpace[j++];
        }
    } //End of method

    @Override
    public void sort(FlowPane flowPane, AnchorPane titlepane, AnchorPane mainPane, int sleepTime) throws InterruptedException {
        if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
            Main.alertDialogIllegal(
                    "There are no values to do the sort, please enter or generate a new array before pressing the button!");
            return;
        }

        doMergeSort(getArrayToSort(), 0, getArrayToSort().length - 1, workSpace, getAnimation());


        // Create a new task with a new thread that will update the GUI while running
        // the loop
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                doAnimation(getAnimation(), flowPane, sleepTime);
                return null;
            } // End of method call
        }; // End of Task
        setTaskStatus(task, titlepane, flowPane, mainPane);
        new Thread(task).start();

    } // End of method

    private void doAnimation(ArrayList<AnimationList> animationLists, FlowPane flowPane, int sleepTime) throws InterruptedException {
        for (AnimationList animation : animationLists) {
            Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animation.getFirstValue());
            Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animation.getSecondValue());
            switch (animation.getSortStatus()) {
                case COMPARE:
                    Platform.runLater(() -> {
                        firstRect.setFill(Paint.valueOf("#58BC50"));
                        secondRect.setFill(Paint.valueOf("#58BC50"));
                    });
                    Thread.sleep(sleepTime);
                    break;
                case SWAP:
                    Platform.runLater(() -> {
                        firstRect.setFill(Paint.valueOf("#FFB3B8"));
                        secondRect.setFill(Paint.valueOf("#FFB3B8"));
                        firstRect.setHeight(workSpace[animation.getSecondValue()]);

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
                case PIVOT:
                    Platform.runLater(() -> firstRect.setFill(Paint.valueOf("#ABB37B")));
                    break;
                case MEDIAN_COMPARE:
                    Platform.runLater(() -> {
                        firstRect.setFill(Paint.valueOf("turquoise"));
                        secondRect.setFill(Paint.valueOf("turquoise"));
                    });
                    Thread.sleep(sleepTime);
                    break;
                case MANUAL_COMPARE:
                    Platform.runLater(() -> {
                        firstRect.setFill(Paint.valueOf("gray"));
                        secondRect.setFill(Paint.valueOf("gray"));
                    });
                    Thread.sleep(sleepTime);
                    break;
                default:
                    break;
            } // End of switch
        } // End of for

    }


    @Override
    protected void setTaskStatus(Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {
        task.setOnRunning(e -> {
            changeButtonStatus(true, task, titlePane, flowPane, mainPane);
            mainPane.getChildren().get(3).setVisible(true); // the cancel button is the fourth child of the main anchor
            // pane
            createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, "COMPARE PIVOT", 4);
            createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 196, "SWAP", 5);
            createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 316, "SORTED", 6);
            createLabelColor(mainPane, "-fx-background-color:turquoise", 436, "MEDIAN COMPARE", 7);
            createLabelColor(mainPane, "-fx-background-color:gray", 556, "MANUAL COMPARE", 8);
            createLabelColor(mainPane, "-fx-background-color:#ABB37B", 676, "PIVOT", 9);
            Main.setTimer(mainPane, false);
        });
        task.setOnCancelled(e -> {
            changeButtonStatus(false, task, titlePane, flowPane, mainPane);
            for (int i = 0; i < 6; i++)
                mainPane.getChildren().remove(4);
            Main.setTimer(mainPane, true);
            Main.getTimeElapse().setVisible(false);
        });
        task.setOnSucceeded(e -> {
            changeButtonStatus(false, task, titlePane, flowPane, mainPane);
            mainPane.getChildren().get(3).setVisible(false);
            for (int i = 0; i < 6; i++)
                mainPane.getChildren().remove(4);
            Main.setTimer(mainPane, true);
        });

    } //End of method

    @Override
    protected void changeButtonStatus(boolean isDisable, Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {

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
        label.setPrefWidth(110);
        label.setPrefHeight(24);
        label.setTextFill(Paint.valueOf("white"));
        label.setAlignment(Pos.CENTER);
        label.setText(text);
        AnchorPane.setTopAnchor(label, 87.0);
        mainPane.getChildren().add(childPosition, label);
    } //End of method
}
