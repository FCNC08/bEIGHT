package canvas;

import javafx.event.EventHandler;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class LogicSubSceneContainer extends HBox{
	
	public LogicSubScene logic_subscene;
	protected SubScene component_chooser;
	
	public LogicSubSceneContainer(int Startwidth, int Startheight, int multiplier) {
		
		component_chooser = getComponentChooser();
		logic_subscene = LogicSubScene.init(Startwidth, Startheight, multiplier);
		
		getChildren().add(component_chooser);
		getChildren().add(logic_subscene);
		EventHandler<MouseEvent> createNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//TODO method to look up clicked Element in component_chooser, cloning it and make ability to move it into LogicSubScene and adding it afterwards
			}
		};
	}
	
	
	public SubScene getComponentChooser() {
		
		return null;
	}
}
