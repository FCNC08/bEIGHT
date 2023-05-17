package canvas;

import canvas.components.FunctionalCanvasComponent;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;

public class ComponentChooser extends SubScene{

	protected double width;
	protected double height;
	
	protected LogicSubScene logic_Scene;
	protected ComponentGroupings grouping;
	protected Group MainRoot;
	
	public ComponentChooser(LogicSubScene parent_logicScene,Group root, double Width, double Height, ComponentGroupings component_param) {
		super(root, Width, Height);
		this.width = Width;
		this.height = Height;
		grouping = component_param;
		MainRoot = root;
	}
	
	public void reloadDesign() {
		int count = 0;
		for(ComponentGroup group : grouping) {
			for(FunctionalCanvasComponent ImageComponent : group) {
				ImageView view = ImageComponent.getImageView();
				view.setLayoutX(width*(count%2)*0.5);
				view.setLayoutY();
			}
		}
	}

}
