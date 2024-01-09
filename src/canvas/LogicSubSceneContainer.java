package canvas;

import canvas.components.FunctionalCanvasComponent;
import canvas.components.LogicComponent;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import canvas.components.StandardComponents.LogicComponents.NANDGate;
import canvas.components.StandardComponents.LogicComponents.NORGate;
import canvas.components.StandardComponents.LogicComponents.NOTGate;
import canvas.components.StandardComponents.LogicComponents.ORGate;
import canvas.components.StandardComponents.LogicComponents.XNORGate;
import canvas.components.StandardComponents.LogicComponents.XORGate;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import util.OcupationExeption;

public class LogicSubSceneContainer extends SubScene{
	
	public LogicSubScene logic_subscene;
	public SubScene component_chooser;
	private FunctionalCanvasComponent adding_component;
	private Group root;
	public LogicSubSceneContainer(int width, int height, Group Mainroot) {
		super(Mainroot, width, height);
		this.root = Mainroot;
		//Adding LogicScene
		logic_subscene = LogicSubScene.init(LogicSubScene.getNearesDot((int)(width*0.75)), LogicSubScene.getNearesDot((int)(height*0.9)), 4); 

		logic_subscene.setFill(Color.WHITE);
				
		logic_subscene.addX((int) (width*0.05));
		logic_subscene.addY((int) (height*0.01));

		//Creates example ComponentChooser TODO Adding Filesystem
		ComponentGroupings grouping = new ComponentGroupings();
		ComponentGroup group = new ComponentGroup();
		group.add(ANDGate.getSolidANDGATE(LogicComponent.SIZE_MIDDLE ,2));
		group.add(NANDGate.getSolidNANDGATE(LogicComponent.SIZE_MIDDLE ,2));
		group.add(ORGate.getSolidORGATE(LogicComponent.SIZE_MIDDLE, 2));
		group.add(NORGate.getSolidNORGATE(LogicComponent.SIZE_MIDDLE ,2));
		group.add(XORGate.getSolidXORGATE(LogicComponent.SIZE_MIDDLE ,2));
		group.add(XNORGate.getSolidXNORGATE(LogicComponent.SIZE_MIDDLE, 2));
		group.add(NOTGate.getSolidNOTGATE(LogicComponent.SIZE_MIDDLE));
		ComponentGroup group_1 = new ComponentGroup();
		
		grouping.add(group);
		grouping.add(group_1);
		
		//Adding ComponentChooser and set the layout
		component_chooser = new ComponentChooser(logic_subscene, new Group(),width*0.15, LogicSubScene.getNearesDot((int)(height*0.9)), grouping);
		
		System.out.println(component_chooser.getBoundsInParent());
		component_chooser.setLayoutX(width*0.8);
		component_chooser.setLayoutY(height*0.01);
		
		
		root.getChildren().add(logic_subscene);
		root.getChildren().add(component_chooser);
		
		//pressing EventHandler to create a clone of the pressed Component and adding it to LogicSubSceneContainer
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
							adding_component.setX((int) (me.getX()-logic_subscene.getX()));
							adding_component.setY((int) (me.getY()-logic_subscene.getY()));
							root.getChildren().add(adding_component.getImageView());
							
						}
					}
				}
			}
		};
		EventHandler<MouseEvent> moveNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//Moving Component to mouseposition while dragging it
				if(adding_component != null) {
					adding_component.setX((int) (me.getX()-logic_subscene.getX()));
					adding_component.setY((int) (me.getY()-logic_subscene.getY()));
				}
				
			}
		};
		EventHandler<MouseEvent> addNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(adding_component != null) {
					root.getChildren().remove(adding_component.getImageView());
					//Adding it to the LogicSubScene if it is dragged into the SubScene and adding all Offset in the Subscene
					if(logic_subscene.getLayoutX() < me.getX()&&(logic_subscene.getLayoutX()+logic_subscene.getWidth())>me.getX()&&logic_subscene.getLayoutY()<me.getY()&&(logic_subscene.getLayoutY()+logic_subscene.getHeight())>me.getY()) {
						System.out.println(adding_component);
						adding_component.setX((int) (me.getX()-logic_subscene.getX()+logic_subscene.getXTranslate())-adding_component.width/2);
						adding_component.setY((int) (me.getY()-logic_subscene.getY()+logic_subscene.getYTranslate()));
						try {
							logic_subscene.add(adding_component);
						} catch (OcupationExeption e) {
							e.printStackTrace();
						}
					}
					adding_component = null;
				}
				
					
			}
		};
		addEventFilter(MouseEvent.MOUSE_PRESSED, createNewLogicComponent);
		addEventFilter(MouseEvent.MOUSE_RELEASED, addNewLogicComponent);
		addEventFilter(MouseEvent.MOUSE_DRAGGED, moveNewLogicComponent);
	}
	
	public static LogicSubSceneContainer init(int width, int height) {
		//Initializing Container with new Group
		return new LogicSubSceneContainer(width, height, new Group());
	}
}
