package canvas;

import javafx.scene.SubScene;
import javafx.scene.layout.HBox;

public class LogicSubSceneContainer extends HBox{
	
	public LogicSubScene logic_subscene;
	protected SubScene component_chooser;
	
	public LogicSubSceneContainer(int Startwidth, int Startheight, int multiplier) {
		
		component_chooser = getComponentChooser();
		logic_subscene = LogicSubScene.init(Startwidth, Startheight, multiplier);
		
		//getChildren().add(component_chooser);
		//getChildren().add(logic_subscene);
	}
	
	
	public SubScene getComponentChooser() {
		
		return null;
	}
}
