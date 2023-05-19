package canvas;

import canvas.components.FunctionalCanvasComponent;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
		this.logic_Scene = parent_logicScene;
		setFill(Color.ALICEBLUE);
		grouping = component_param;
		MainRoot = root;
		reloadDesign();
	}
	
	public void reloadDesign() {
		int count = 0;
		double height = 0;
		for(ComponentGroup group : grouping) {
			for(FunctionalCanvasComponent ImageComponent : group) {
				ImageView view = ImageComponent.getImageView();
				view.setLayoutX(width*(count%2)*0.5);
				count++;
				height+=count%2*view.getFitHeight()+view.getFitHeight()/4;
				view.setLayoutY(height);
				MainRoot.getChildren().add(view);
			}
			
			Rectangle Seperator = new Rectangle(width, 20);
			height+=20;
			Seperator.setLayoutY(height);
			MainRoot.getChildren().add(Seperator);
			count = 0;
		}
	}

}
