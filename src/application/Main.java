package application;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;



public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			
			Label prompt = new Label("Enter a floating point number for a step by step infographic on IEEE-754 Storage: ");
			TextField field = new TextField();
			Button calculate = new Button("Calculate");
			field.setMaxWidth(300);
			VBox vbox = new VBox(30, prompt, field, calculate);
			vbox.setAlignment(Pos.TOP_CENTER);
			vbox.setPadding(new Insets(30));
			
			Scene scene = new Scene(vbox,800,450);
			stage.setScene(scene);
			stage.setTitle("IEEE-754 Step By Step Converter");
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
