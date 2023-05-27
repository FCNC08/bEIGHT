package canvas;

import canvas.components.FunctionalCanvasComponent;
import canvas.components.LogicComponent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ComponentChooser extends SubScene{

	protected double width;
	protected double height;
	
	protected int view_width;
	protected int view_height;
	
	protected LogicSubScene logic_Scene;
	protected ComponentGroupings grouping;
	protected Group MainRoot;
	
	private EventHandler<MouseEvent> press_Event_Handler;
	
	public ComponentChooser(LogicSubScene parent_logicScene,Group root, double Width, double Height, ComponentGroupings component_param) {
		super(root, Width, Height);
		this.width = Width;
		this.height = Height;
		this.logic_Scene = parent_logicScene;
		setFill(Color.BLUEVIOLET);
		grouping = component_param;
		MainRoot = root;
		view_width = (int) (Width/2);
		view_height = view_width;
		
		EventHandler<MouseEvent> press_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(me.getSource() instanceof ImageView) {
					ImageView view = (ImageView) me.getSource();
					if(view.getImage() instanceof FunctionalCanvasComponent) {
						FunctionalCanvasComponent component = (FunctionalCanvasComponent) view.getImage();
						if(me.isPrimaryButtonDown()) {
							logic_Scene.add(component.getClone(FunctionalCanvasComponent.SIZE_MIDDLE));
						}else if(me.isSecondaryButtonDown()) {
							logic_Scene.add(component.getClone(FunctionalCanvasComponent.SIZE_SMALL));
						}
					}
				}
			}
		};
		
		this.press_Event_Handler = press_Event_Handler;
		
		EventHandler<MouseEvent> release_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
			
			}
		};
		
		EventHandler<MouseEvent> move_Event_Handler = new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent me) {
				// TODO Auto-generated method stub
				
			}
		};
		
		addEventFilter(MouseEvent.MOUSE_RELEASED, release_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_MOVED, move_Event_Handler);
		
		reloadDesign();
	}
	
	public void reloadDesign() {
		int count = 0;
		double height = 0;
		for(ComponentGroup group : grouping) {
			for(FunctionalCanvasComponent ImageComponent : group) {
				ImageView view = ImageComponent.getImageView();
				view.setFitHeight(view_height);
				view.setFitWidth(view_width);
				view.setLayoutX(width*(count%2)*0.5);
				view.setLayoutY(height);
				view.addEventFilter(MouseEvent.MOUSE_CLICKED, press_Event_Handler);
				height= height+(count%2)*view_height;
				count++;
				MainRoot.getChildren().add(view);
			}
			 
			Rectangle Seperator = new Rectangle(width, 20);
			Seperator.setLayoutY(height);
			MainRoot.getChildren().add(Seperator);
			System.out.println(height);
			height = height+20;
			count = 0;
		}
	}

}
