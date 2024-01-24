package education;

import org.json.JSONObject;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import net.lingala.zip4j.ZipFile;

public class LogicSubSceneTest extends Pane{
	public LogicSubSceneTest(double width, double height, ZipFile file, EducationSubScene scene) {
		super();
		Button button_back = new Button("Back");
		button_back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				scene.setPrev();
			}
		});
		button_back.setLayoutY(height-button_back.getHeight()-150);
		button_back.setLayoutX(25);
		getChildren().add(button_back);
		
		Button button_next = new Button("Next");
		button_next.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				scene.setNext();
			}
		});
		button_next.setLayoutY(height-button_next.getHeight()-150);
		button_next.setLayoutX(width-button_next.getWidth()-50);
		getChildren().add(button_next);
	}
}
