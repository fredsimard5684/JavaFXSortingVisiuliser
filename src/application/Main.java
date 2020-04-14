package application;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application {
	private static Text timeElapse;
	private static Timer timer;
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene.widthProperty().addListener(e -> {
				System.out.println(scene.getWidth() + "Widht"); // Debug line
			});
			scene.heightProperty().addListener(e -> {
				System.out.println(scene.getHeight()); // Debug line
			});
			primaryStage.setMinWidth(1370);
			primaryStage.setMinHeight(900);
			primaryStage.setOnCloseRequest(e -> {
				Platform.exit(); // Exit the UI
				System.exit(0); // Exit the JVM
			});
			primaryStage.setTitle("Sorting Visulizer");
			primaryStage.setScene(scene);
			
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	} // End of method

	// Display an alert that is accessible on every class
	public static void alertDialogIllegal(String contentText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText("Oops, an error occured...");
		alert.setContentText(contentText);
		Exception ex = new IllegalAccessException(contentText);
		// From internet!!!
		// Create expandable exception
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	} // End of method

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
					Platform.runLater(() -> timeElapse.setText(String.format("Time: %d deciseconds", interval)));
					interval++;
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, 100);
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
	}
	
	public static Text getTimeElapse() {
		return timeElapse;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
