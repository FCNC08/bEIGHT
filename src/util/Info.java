package util;

import java.util.ArrayList;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Info extends HBox{
	
	public double height;
	private Text headline = new Text();
	private ArrayList<Text> menu_items = new ArrayList<Text>();
	
	public Info() {
		
		getChildren().add(headline);
		setLayoutY(getLayoutY()-getHeight());
	}
	
	public void setHeadline(String text) {
		headline.setText(text);
	}
	public void addText(Text text) {
		menu_items.add(text);
		getChildren().add(text);
		setLayoutY(getLayoutY()-getHeight());		
	}
	public void removeText(Text text) {
		menu_items.remove(text);
		getChildren().remove(text);
		setLayoutY(getLayoutY()-getHeight());
	}
}
