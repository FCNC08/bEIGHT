/*package util;

import java.util.ArrayList;

import canvas.LogicSubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class Info extends HBox{
	
	private Label headline = new Label();
	private ArrayList<Label> menu_items = new ArrayList<Label>();
	
	public Info() {
		headline.setBackground(new Background(new BackgroundFill(LogicSubScene.focus_square_main, null, null)));
		headline.setFont(new Font(18));
		headline.setTextFill(LogicSubScene.focus_square_secondary);
		getChildren().add(headline);
	}
	
	public void setHeadline(String text) {
		headline.setText(text);
	}
	public void addText(Label text) {
		menu_items.add(text);
		getChildren().add(text);	
	}
	public void removeText(Label text) {
		menu_items.remove(text);
		getChildren().remove(text);
	}
}*/
