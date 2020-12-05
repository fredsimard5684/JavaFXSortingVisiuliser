package sortingAlgorithms;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class HeapSort extends SortingAlgorithms {
    public HeapSort(int[] array) {
        super(array);
    }

    private void heapify(int[] array, int size, int index, ArrayList<AnimationList> animationLists) {
        int largest = index;
        int left = (2 * index) + 1; //Directly after the parent
        int right = (2 * index) + 2; // After the left child

        addingCorrectAnimation(largest, left, right, size, animationLists, SortStatus.COMPARE);

        //if the left child is greater than the root
        if (left < size && array[left] > array[largest]) {
            largest = left;
        }

        //if the right child is greater than the root
        if (right < size && array[right] > array[largest]) {
            largest = right;
        }

        if (largest != index) { //If left or right is the largest
            //Swap
            int temp = array[index];
            array[index] = array[largest];
            array[largest] = temp;

            animationLists.add(new AnimationList(index, largest, largest, SortStatus.SWAP));

            addingCorrectAnimation(index, left, right, size, animationLists, SortStatus.REMOVE_FOCUS);
            heapify(array, size, largest, animationLists); //Recursively call heapify with largest value. This will assure that the subs-trees are always sorted
        }
        //If the method doesnt get recall
        addingCorrectAnimation(largest, left, right, size, animationLists, SortStatus.REMOVE_FOCUS);
    } //End of method

    private void doHeapsort(int[] array, ArrayList<AnimationList> animationLists) {
        int size = array.length;

        //Constructing the max heap so that we can easily sort it after
        for (int i = size/2 - 1; i >= 0; i--) { // n/2 -1 takes the first non leaf node
            heapify(array, size, i, animationLists);
        }

        for (int i = size - 1; i > 0; i--) {
            //Swap last leaf with root
            int temp = array[0]; //root node
            array[0] = array[i];
            array[i] = temp; //Root is now last leaf

            //Swapping first element(0) with last
            animationLists.add(new AnimationList(0, i, i, SortStatus.SWAP));
            animationLists.add(new AnimationList(0, 0, 0, SortStatus.REMOVE_FOCUS));
            //Last element is now sorted
            animationLists.add(new AnimationList(i, i, i, SortStatus.SORTED));

            //By calling heapify, this will sort our array
            heapify(array, i, 0, animationLists); //Since we swaped the root, we need to rebuild our max heap. Since we always go
                                        // left or right < n, we never go to the max value. This is the process that allows us
                                        //to sort everything correctly
        } //End of for
        //Highlight the last sorted value as sorted
        animationLists.add(new AnimationList(0,0,0, SortStatus.SORTED));
    } //End of method

    //This allow to chek if right or left is a rectangle bar and add the animation accordingly
    private void addingCorrectAnimation(int parent, int left, int right, int size, ArrayList<AnimationList> animationLists, SortStatus sortStatus) {
        if (left >= size) animationLists.add(new AnimationList(parent, parent, parent, sortStatus));
        else if (right >= size) animationLists.add(new AnimationList(parent, left, left, sortStatus));
        else animationLists.add(new AnimationList(parent, left, right, sortStatus));
    } //End of method

    @Override
    public void sort(FlowPane flowPane, AnchorPane titlePane, AnchorPane mainPane, int sleepTime) throws InterruptedException {
        if (flowPane.getChildren().isEmpty()) { // If there are no rectangles in the GUI
            Main.alertDialogIllegal(
                    "There are no values to do the sort, please enter or generate a new array before pressing the button!");
            return;
        }
        doHeapsort(getArrayToSort(), getAnimation());

        // Create a new task with a new thread that will update the GUI while running
        // the loop
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                doAnimation(getAnimation(), flowPane, sleepTime);
                return null;
            } // End of method call
        }; // End of Task
        setTaskStatus(task, titlePane, flowPane, mainPane);
        new Thread(task).start();
    }

    // Set the task on finished, on running and on cancelled
    private void setTaskStatus(Task<Void> task, AnchorPane titlePane, FlowPane flowPane, AnchorPane mainPane) {
        task.setOnRunning(e -> {
            changeButtonStatus(true, task, titlePane, flowPane, mainPane);
            mainPane.getChildren().get(3).setVisible(true); // the cancel button is the fourth child of the main anchor
            // pane
            createLabelColor(mainPane, "-fx-background-color:#58BC50", 76, 135, "COMPARE/CHILD NODE", 4);
            createLabelColor(mainPane, "-fx-background-color:#FFB3B8", 221, 110, "SWAP", 5);
            createLabelColor(mainPane, "-fx-background-color:#8BA9CC", 341, 110, "SORTED", 6);
            createLabelColor(mainPane, "-fx-background-color:turquoise", 461, 110, "PARENT NODE", 7);
            SortingAlgorithms.setTimer(mainPane, false);
        });
        task.setOnCancelled(e -> {
            changeButtonStatus(false, task, titlePane, flowPane, mainPane);
            for (int i = 0; i < 4; i++)
                mainPane.getChildren().remove(4);
            SortingAlgorithms.setTimer(mainPane, true);
            SortingAlgorithms.getTimeElapse().setVisible(false);
        });
        task.setOnSucceeded(e -> {
            changeButtonStatus(false, task, titlePane, flowPane, mainPane);
            mainPane.getChildren().get(3).setVisible(false);
            for (int i = 0; i < 4; i++)
                mainPane.getChildren().remove(4);
            SortingAlgorithms.setTimer(mainPane, true);
        });

    } // End of method

    private void doAnimation(ArrayList<AnimationList> animationLists, FlowPane flowPane, int sleepTime) throws InterruptedException {
        // Loop through all the animation where the bubble sort algorithm is doing
        // something
        for (AnimationList animationList : animationLists) {
            Rectangle firstRect = (Rectangle) flowPane.getChildren().get(animationList.getFirstValue());
            Rectangle secondRect = (Rectangle) flowPane.getChildren().get(animationList.getSecondValue());
            Rectangle thirdRect = (Rectangle) flowPane.getChildren().get(animationList.getThirdValue());

            switch (animationList.getSortStatus()) {
                case COMPARE:
                    Platform.runLater(() -> {
                        //If there is only a parent node
                        if (animationList.getFirstValue() == animationList.getSecondValue()) {
                            firstRect.setFill(Paint.valueOf("turquoise"));
                        }
                        else {
                            firstRect.setFill(Paint.valueOf("turquoise"));
                            secondRect.setFill(Paint.valueOf("#58BC50"));
                            thirdRect.setFill(Paint.valueOf("#58BC50"));
                        }
                    });
                    Thread.sleep(sleepTime);
                    break;
                case SWAP:
                    Platform.runLater(() -> {
                        firstRect.setFill(Paint.valueOf("#FFB3B8"));
                        secondRect.setFill(Paint.valueOf("#FFB3B8"));
                        double tempHeight = firstRect.getHeight();
                        firstRect.setHeight(secondRect.getHeight());
                        secondRect.setHeight(tempHeight);
                    });
                    Thread.sleep(sleepTime);
                    break;
                case REMOVE_FOCUS:
                    Platform.runLater(() -> {
                        firstRect.setFill(Paint.valueOf("#305580"));
                        secondRect.setFill(Paint.valueOf("#305580"));
                        thirdRect.setFill(Paint.valueOf("#305580"));
                    });
                    break;
                case SORTED:
                    Platform.runLater(() -> firstRect.setFill(Paint.valueOf("#8BA9CC")));
                    break;
                default:
                    break;
            } // End of switch
        } // End of for
    }
} //End of class
