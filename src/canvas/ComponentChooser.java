package canvas;

import canvas.components.FunctionalCanvasComponent;
import javafx.scene.SubScene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ComponentChooser extends SubScene {

	protected double width;
	protected double height;

	protected int view_width;
	protected int view_height;

	protected LogicSubScene logic_Scene;
	protected ComponentGroupings grouping;
	protected ScrollPane scrollPane;
	protected Pane MainRoot;

	protected FunctionalCanvasComponent adding_component;

	public ComponentChooser(LogicSubScene parent_logicScene, ScrollPane root, double Width, double Height, ComponentGroupings component_param) {
		// Initializing Component Chooser with bounds
		super(root, Width, Height);
		this.width = Width;
		this.height = Height;
		this.logic_Scene = parent_logicScene;
		setFill(Color.BLACK);
		grouping = component_param;
		scrollPane = root;
		MainRoot = new Pane();
		view_width = (int) (Width / 2);
		view_height = view_width;
		reloadDesign();
		scrollPane.setContent(MainRoot);
		scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
	}

	public void reloadDesign() {
		// Calculating position for each choosable Component in Chooser
		int count = 0;
		double height = 0;
		for (ComponentGroup group : grouping) {
			for (FunctionalCanvasComponent ImageComponent : group) {
				ImageView view = ImageComponent.getImageView();
				view.setFitHeight(view_height);
				view.setFitWidth(view_width);
				view.setLayoutX(width * (count % 2) * 0.5);
				view.setLayoutY(height);
				height += (count % 2) * view_height;
				count++;
				MainRoot.getChildren().add(view);
			}
			// Starting new Line in ComponentChooser later used with pop-uo-menues
			height += (count % 2) * view_height;
			Rectangle Seperator = new Rectangle(width, 20);
			Seperator.setFill(Color.GREY);
			Seperator.setLayoutY(height);
			MainRoot.getChildren().add(Seperator);
			height = height + 20;
			count = 0;
		}
	}

}
