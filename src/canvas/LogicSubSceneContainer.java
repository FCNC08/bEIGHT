package canvas;

import canvas.components.LogicComponent;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import canvas.components.StandardComponents.LogicComponents.ORGate;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class LogicSubSceneContainer extends HBox{
	
	public LogicSubScene logic_subscene;
	public SubScene component_chooser;
	
	public LogicSubSceneContainer(int width, int height) {
		//Adding LogicScene

		logic_subscene = LogicSubScene.init(LogicSubScene.getNearesDot((int)(width*0.75)), LogicSubScene.getNearesDot((int)(height*0.9)), 4); 

		logic_subscene.setFill(Color.WHITE);
		
		logic_subscene.addX((int)(width*0.05));
		logic_subscene.addY((int)(height*0.01));

		//Creates example ComponentChooser TODO Adding Filesystem
		ComponentGroupings grouping = new ComponentGroupings();
		ComponentGroup group = new ComponentGroup();
		group.add(ANDGate.getANDGATE(LogicComponent.SIZE_MIDDLE ,2, 1));
		group.add(ORGate.getORGATE(LogicComponent.SIZE_MIDDLE ,2, 1));
		group.add(ANDGate.getANDGATE(LogicComponent.SIZE_MIDDLE ,2, 1));
		group.add(ORGate.getORGATE(LogicComponent.SIZE_MIDDLE ,2, 1));
		ComponentGroup group_1 = new ComponentGroup();
		
		grouping.add(group);
		grouping.add(group_1);
		

		component_chooser = new ComponentChooser(logic_subscene, new Group(),width*0.15, LogicSubScene.getNearesDot((int)(height*0.9)), grouping);
		component_chooser.setLayoutX(width*0.8);
		component_chooser.setLayoutY(height*0.01);
		
		
		getChildren().add(logic_subscene);
		getChildren().add(component_chooser);
		EventHandler<MouseEvent> createNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//TODO method to look up clicked Element in component_chooser, cloning it and make ability to move it into LogicSubScene and adding it afterwards
			}
		};
	}
}
