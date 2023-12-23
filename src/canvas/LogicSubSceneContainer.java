package canvas;

import canvas.components.FunctionalCanvasComponent;
import canvas.components.LogicComponent;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import canvas.components.StandardComponents.LogicComponents.ORGate;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class LogicSubSceneContainer extends HBox{
	
	public LogicSubScene logic_subscene;
	public SubScene component_chooser;
	private FunctionalCanvasComponent adding_component;
	private double last_x = 0;
	private double last_y = 0;
	public LogicSubSceneContainer(int width, int height) {
		//Adding LogicScene

		logic_subscene = LogicSubScene.init(LogicSubScene.getNearesDot((int)(width*0.75)), LogicSubScene.getNearesDot((int)(height*0.9)), 4); 

		logic_subscene.setFill(Color.WHITE);
		
		
		logic_subscene.addX((int) (width*0.05));
		logic_subscene.addY((int) (height*0.01));

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
		
		System.out.println(component_chooser.getBoundsInParent());
		component_chooser.setLayoutX(width*0.8);
		component_chooser.setLayoutY(height*0.01);
		
		
		getChildren().add(logic_subscene);
		getChildren().add(component_chooser);
		EventHandler<MouseEvent> createNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//Checks bounds of component chooser. If in bound of component chooser it clones the FunctionalComponent
				if(component_chooser.getLayoutX() < me.getX()&&component_chooser.getLayoutY() < me.getY()) {
					if(me.getTarget() instanceof ImageView) {
						ImageView view = (ImageView) me.getTarget();
						if(view.getImage() instanceof FunctionalCanvasComponent) {
							FunctionalCanvasComponent component = (FunctionalCanvasComponent) view.getImage();
							if(me.getButton() == MouseButton.PRIMARY){
								System.out.println("Middle");
								adding_component = component.getClone(FunctionalCanvasComponent.SIZE_MIDDLE);
							}else if(me.getButton() == MouseButton.SECONDARY){
								System.out.println("Small");
								adding_component = component.getClone(FunctionalCanvasComponent.SIZE_SMALL);
							}else {
								System.out.println("Big");
								adding_component = component.getClone(FunctionalCanvasComponent.SIZE_BIG);
							}
							adding_component.setX(0);
							adding_component.setY(0);
							getChildren().add(adding_component.getImageView());
							last_x = me.getX();
							last_y = me.getY();
							adding_component.addX((int) me.getX());
							adding_component.addY((int) me.getY());
						}
					}
				}
			}
		};
		EventHandler<MouseEvent> moveNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(adding_component != null) {
					adding_component.addX((int) (me.getX()-last_x));
					adding_component.addY((int) (me.getY()-last_y));
					last_x = me.getX();
					last_y = me.getY();
					System.out.println("Adding: "+adding_component.getX()+"  "+adding_component.getY());
					System.out.println("ImageView: "+adding_component.getImageView().getLayoutX()+"  "+adding_component.getImageView().getLayoutY());
				}
				
			}
		};
		EventHandler<MouseEvent> addNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(logic_subscene.getLayoutX() < me.getX()&&(logic_subscene.getLayoutX()+logic_subscene.getWidth())>me.getX()&&logic_subscene.getLayoutY()<me.getY()&&(logic_subscene.getLayoutY()+logic_subscene.getHeight())>me.getY()) {
					System.out.println(adding_component);
					if(adding_component != null) {
						System.out.println(adding_component.getX()+"  "+adding_component.getY());
						adding_component = null;
					}
				}
					
			}
		};
		addEventFilter(MouseEvent.MOUSE_PRESSED, createNewLogicComponent);
		addEventFilter(MouseEvent.MOUSE_RELEASED, addNewLogicComponent);
		addEventFilter(MouseEvent.MOUSE_DRAGGED, moveNewLogicComponent);
	}
}
