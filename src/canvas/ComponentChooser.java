package canvas;


import canvas.components.FunctionalCanvasComponent;
import canvas.components.LogicComponent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import util.OcupationExeption;

public class ComponentChooser extends SubScene{

	protected double width;
	protected double height;
	
	protected int view_width;
	protected int view_height;
	
	protected LogicSubScene logic_Scene;
	protected ComponentGroupings grouping;
	protected Group MainRoot;
	
	protected FunctionalCanvasComponent adding_component;
	
	public ComponentChooser(LogicSubScene parent_logicScene,Group root, double Width, double Height, ComponentGroupings component_param) {
		//Initializing Component Chooser with bounds
		super(root, Width, Height);
		this.width = Width;
		this.height = Height;
		this.logic_Scene = parent_logicScene;
		setFill(Color.BLUEVIOLET);
		grouping = component_param;
		MainRoot = root;
		view_width = (int) (Width/2);
		view_height = view_width;
		
		//press Event Handler used to clone LogicComponents now replaced by the dragging system of LogicSubSceneContainer
		EventHandler<MouseEvent> press_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//Getting Component out of the clicked Imageview
				if(me.getTarget() instanceof ImageView) {
					ImageView view = (ImageView) me.getTarget();
					if(view.getImage() instanceof FunctionalCanvasComponent) {
						FunctionalCanvasComponent component = (FunctionalCanvasComponent) view.getImage();
						if(me.getButton() == MouseButton.PRIMARY){
							setAddComponent(component.getClone(FunctionalCanvasComponent.SIZE_MIDDLE));
							System.out.println("Middle");
						}else if(me.getButton() == MouseButton.SECONDARY){
							setAddComponent(component.getClone(FunctionalCanvasComponent.SIZE_SMALL));
							System.out.println("Small");
						}else {
							setAddComponent(component.getClone(FunctionalCanvasComponent.SIZE_BIG));
							System.out.println("Big");
						}
						System.out.println(adding_component);
					}
				}
			}
		};
		
		//Exit EventHandler used to add Component to LogicSubScene but now replaced by LogicSubSceneContainer
		EventHandler<MouseEvent> exit_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				System.out.println("Leaving");
				if(adding_component != null) {
					adding_component.setX((int) (logic_Scene.getXTranslate()+logic_Scene.Start_Width-adding_component.getHeight()));
					adding_component.setY((int) (logic_Scene.getYTranslate()+me.getSceneY()-adding_component.getHeight()));
					logic_Scene.addTry(adding_component);
					adding_component = null;
				}
			}
		};
		
		//addEventFilter(MouseEvent.MOUSE_PRESSED, press_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_EXITED, exit_Event_Handler);
		
		
		reloadDesign();
	}
	
	private void setAddComponent(FunctionalCanvasComponent component) {
		adding_component = component;
	}
	private FunctionalCanvasComponent getAddComponent() {
		return adding_component;
	}
	
	public void reloadDesign() {
		//Calculating position for each choosable Component in Chooser
		int count = 0;
		double height = 0;
		for(ComponentGroup group : grouping) {
			for(FunctionalCanvasComponent ImageComponent : group) {
				ImageView view = ImageComponent.getImageView();
				view.setFitHeight(view_height);
				view.setFitWidth(view_width);
				view.setLayoutX(width*(count%2)*0.5);
				view.setLayoutY(height);
				height= height+(count%2)*view_height;
				count++;
				MainRoot.getChildren().add(view);
			}
			//Starting new Line in ComponentChooser later used with pop-uo-menues	
			 
			Rectangle Seperator = new Rectangle(width, 20);
			Seperator.setLayoutY(height);
			MainRoot.getChildren().add(Seperator);
			System.out.println(height);
			height = height+20;
			count = 0;
		}
	}

}
