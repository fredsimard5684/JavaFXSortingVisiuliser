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
            animationLists.add(new AnimationList(i, j, SortStatus.COMPARE));
            animationLists.add(new AnimationList(i, j, SortStatus.REMOVE_FOCUS));
            if (workSpace[i] <= workSpace[j]) {
                animationLists.add(new AnimationList(k, workSpace[i], SortStatus.SWAP));
                animationLists.add(new AnimationList(k, k, SortStatus.REMOVE_FOCUS));
                mainArray[k++] = workSpace[i++];
            } else {
                animationLists.add(new AnimationList(k, workSpace[j], SortStatus.SWAP));
                animationLists.add(new AnimationList(k, k, SortStatus.REMOVE_FOCUS));
                mainArray[k++] = workSpace[j++];
            }
        }
        //Adding the rest of the numbers

        while (i <= middle) {
            animationLists.add(new AnimationList(i, i, SortStatus.REMAINING_VALUE)); //Inserting the remaining value
            animationLists.add(new AnimationList(i, i, SortStatus.REMOVE_FOCUS));
            animationLists.add(new AnimationList(k, workSpace[i], SortStatus.SWAP));
            animationLists.add(new AnimationList(k, k, SortStatus.REMOVE_FOCUS));
            mainArray[k++] = workSpace[i++];
        }
        while (j <= high) {
            animationLists.add(new AnimationList(j, j, SortStatus.REMAINING_VALUE)); //Inserting the remaining value
            animationLists.add(new AnimationList(j, j, SortStatus.REMOVE_FOCUS));
            animationLists.add(new AnimationList(k, workSpace[j], SortStatus.SWAP));
            animationLists.add(new AnimationList(k, k, SortStatus.REMOVE_FOCUS));
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
        //TODO FIX THE RECTANGLE NAME (First rect)
        for (AnimationList animation : animationLists) {

            switch (animation.getSortStatus()) {
                case COMPARE:
                    Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animation.getFirstValue());
                    Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animation.getSecondValue());
                    Platform.runLater(() -> {
                        firstRect.setFill(Paint.valueOf("#58BC50"));
                        secondRect.setFill(Paint.valueOf("#58BC50"));
                    });
                    Thread.sleep(sleepTime);
                    break;
                case SWAP:
                    Rectangle mainArrayRect = (Rectangle) flowPane.getChildren().get(animation.getFirstValue());
                    Platform.runLater(() -> {
                        mainArrayRect.setFill(Paint.valueOf("#FFB3B8"));
                        mainArrayRect.setHeight(animation.getSecondValue());
                    });
                    Thread.sleep(sleepTime);
                    break;
                case REMOVE_FOCUS:
                    Rectangle rectangleOne = (Rectangle) flowPane.getChildren().get(animation.getFirstValue());
                    Rectangle rectangleTwo = (Rectangle) flowPane.getChildren().get(animation.getSecondValue());
                    Platform.runLater(() -> {
                        rectangleOne.setFill(Paint.valueOf("#305580"));
                        rectangleTwo.setFill(Paint.valueOf("#305580"));
                    });
                    break;
                case REMAINING_VALUE:
                    Rectangle rectOne = (Rectangle) flowPane.getChildren().get(animation.getFirstValue());
                    Rectangle rectTwo = (Rectangle) flowPane.getChildren().get(animation.getSecondValue());
                    Platform.runLater(() -> {
                        rectOne.setFill(Paint.valueOf("turquoise"));
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
            createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, "COMPARE", 4);
            createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 196, "OVERWRITE VAL", 5);
            createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 316, "SORTED", 6);
            createLabelColor(mainPane, "-fx-background-color:turquoise", 436, "ADD REMAIN VAL", 7);

            Main.setTimer(mainPane, false);
        });
        task.setOnCancelled(e -> {
            changeButtonStatus(false, task, titlePane, flowPane, mainPane);
            for (int i = 0; i < 4; i++)
                mainPane.getChildren().remove(4);
            Main.setTimer(mainPane, true);
            Main.getTimeElapse().setVisible(false);
        });
        task.setOnSucceeded(e -> {
            changeButtonStatus(false, task, titlePane, flowPane, mainPane);
            mainPane.getChildren().get(3).setVisible(false);
            for (int i = 0; i < 4; i++)
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
