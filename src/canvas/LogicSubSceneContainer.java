package canvas;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import canvas.components.FunctionalCanvasComponent;
import canvas.components.LayerCanvasComponent;
import canvas.components.LogicComponent;
import canvas.components.StandardComponents.Input;
import canvas.components.StandardComponents.Output;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import util.OcupationExeption;

public class LogicSubSceneContainer extends SubScene {

	public static boolean BLACK = false;
	public static boolean WHITE = true;
	
	protected File location;
	protected boolean color = BLACK;
	
	protected int width;
	protected int height;
	
	protected ComponentGroupings grouping;
	
	EventHandler<MouseEvent> createNewLogicComponent;
	EventHandler<MouseEvent> moveNewLogicComponent;
	EventHandler<MouseEvent> addNewLogicComponent;
	
	public ArrayList<LogicSubScene> logic_subscenes = new ArrayList<>();
	protected ArrayList<TextField> bEIGHT_labels = new ArrayList<>();
	protected TextField eddited_field;
	
	public LogicSubScene logic_subscene;
	public SubScene component_chooser;
	public VBox chooser_root;
	public SubScene bEIGHT_chooser;
	private FunctionalCanvasComponent adding_component;
	private Group root;

	public LogicSubSceneContainer(int width, int height, Group Mainroot, ComponentGroupings groupings, int multiplier) {
		super(Mainroot, width, height);
		this.root = Mainroot;
		this.width = width;
		this.height = height;
		this.grouping = groupings;
		chooser_root = new VBox();
		chooser_root.setBackground(new Background(new BackgroundFill(LogicSubScene.black_grey, null, null)));
		bEIGHT_chooser= new SubScene(chooser_root, width*0.0975, height);
		Label addnew = new Label("+ Add new bEIGHT");
		addnew.setFont(new Font(height*0.02));
		addnew.setTextFill(Color.WHITE);
		addnew.backgroundProperty().set(new Background(new BackgroundFill(LogicSubScene.black_grey, null, null)));
		EventHandler<MouseEvent> create_new_bEIGHT = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				LogicSubScene scene = LogicSubScene.init(LogicSubScene.getNearesDot((int) (width * 0.70)), LogicSubScene.getNearesDot((int) (height * 0.9)), multiplier);
				scene.setFill(LogicSubScene.black_grey);
				scene.addX((int)(width*0.1));
				scene.addY((int) (height*0.01));
				addLogicSubScene(scene);
			}
		};
		addnew.addEventFilter(MouseEvent.MOUSE_CLICKED, create_new_bEIGHT);
		Rectangle border_1 = new Rectangle(width*0.0025, height);
		border_1.setFill(Color.BLACK);
		border_1.setLayoutX(width*0.0975);
		Rectangle border_2 = new Rectangle(width*0.0975, height*0.009);
		border_2.setFill(Color.BLACK);
		chooser_root.getChildren().add(addnew);
		chooser_root.getChildren().remove(border_2);
		root.getChildren().add(bEIGHT_chooser);
		root.getChildren().add(border_1);
		// Adding LogicScene
		logic_subscene = LogicSubScene.init(LogicSubScene.getNearesDot((int) (width * 0.70)), LogicSubScene.getNearesDot((int) (height * 0.9)), multiplier);

		logic_subscene.setFill(LogicSubScene.black_grey);
		
		logic_subscene.setName("Main bEIGHT");

		logic_subscene.addX((int) (width * 0.1));
		logic_subscene.addY((int) (height * 0.01));
		
		addLogicSubScene(logic_subscene);
		
		root.getChildren().add(logic_subscene);

		addChooser();
		addListener();
	}

	public static LogicSubSceneContainer init(int width, int height, String file) {
		ComponentGroupings grouping = null;
		// Initializing Container with new Group
		return new LogicSubSceneContainer(width, height, new Group(), grouping, 4);
	}
	
	public static LogicSubSceneContainer init(int width, int height) {
		// Creates example ComponentChooser TODO Adding Filesystem
		ComponentGroupings grouping = new ComponentGroupings();
		ComponentGroup group = new ComponentGroup();
		try {

			group.add(ANDGate.getANDGATE(LogicComponent.SIZE_MIDDLE, 2));
			group.add(NANDGate.getNANDGATE(LogicComponent.SIZE_MIDDLE, 2));
			group.add(ORGate.getORGATE(LogicComponent.SIZE_MIDDLE, 2));
			group.add(NORGate.getNORGATE(LogicComponent.SIZE_MIDDLE, 2));
			group.add(XORGate.getXORGate(LogicComponent.SIZE_MIDDLE, 2));
			group.add(XNORGate.getXNORGate(LogicComponent.SIZE_MIDDLE, 2));
			group.add(NOTGate.getNOTGATE(LogicComponent.SIZE_MIDDLE));
			//group.add(RAM.getRAM(MemoryCanvasComponent.SIZE_MIDDLE, 8));
		} catch (IllegalArgumentException iae) {
		}
		ComponentGroup group_1 = new ComponentGroup();
		group_1.add(Input.getInput(FunctionalCanvasComponent.SIZE_BIG));
		group_1.add(Output.getOutput(FunctionalCanvasComponent.SIZE_BIG));
		grouping.add(group);
		grouping.add(group_1);
		// Initializing Container with new Group
		return new LogicSubSceneContainer(width, height, new Group(), grouping, 4);
	}

	public void triggerKeyEvent(KeyEvent ke) {
		logic_subscene.triggerKeyEvent(ke);
	}
	private void addChooser() {
		if(component_chooser != null) {
			root.getChildren().remove(component_chooser);
		}
		// Adding ComponentChooser and set the layout
		component_chooser = new ComponentChooser(logic_subscene, new Group(), width * 0.15, LogicSubScene.getNearesDot((int) (height * 0.9)), grouping);
		component_chooser.setFill(Color.WHITE);
		component_chooser.setLayoutX(width * 0.8);
		component_chooser.setLayoutY(height * 0.01);
		component_chooser.setFill(LogicSubScene.black_grey);
		root.getChildren().add(component_chooser);		
	}
	private void addListener() {
		if(createNewLogicComponent != null) {
			removeEventFilter(MouseEvent.MOUSE_PRESSED, createNewLogicComponent);
		}
		if(moveNewLogicComponent != null) {
			removeEventFilter(MouseEvent.MOUSE_DRAGGED, moveNewLogicComponent);
		}
		if(addNewLogicComponent != null) {
			removeEventFilter(MouseEvent.MOUSE_RELEASED, addNewLogicComponent);
		}
		createNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(eddited_field != null) {
					eddited_field.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED,"", "", KeyCode.ENTER, false, false, false, false));
				}
				// Checks bounds of component chooser. If in bound of component chooser it
				// clones the FunctionalComponent
				if (component_chooser.getLayoutX() < me.getX() && component_chooser.getLayoutY() < me.getY()) {
					if (me.getTarget() instanceof ImageView) {
						ImageView view = (ImageView) me.getTarget();
						if (view.getImage() instanceof FunctionalCanvasComponent) {
							FunctionalCanvasComponent component = (FunctionalCanvasComponent) view.getImage();
							if (me.getButton() == MouseButton.PRIMARY) {
								System.out.println("Middle");
								adding_component = component.getClone(FunctionalCanvasComponent.SIZE_MIDDLE);
							} else if (me.getButton() == MouseButton.SECONDARY) {
								System.out.println("Small");
								adding_component = component.getClone(FunctionalCanvasComponent.SIZE_SMALL);
							} else {
								System.out.println("Big");
								adding_component = component.getClone(FunctionalCanvasComponent.SIZE_BIG);
							}
							adding_component.setX((int) me.getX());
							adding_component.setY((int) (me.getY()));
							root.getChildren().add(adding_component.getImageView());

						}
					}
				}
			}
		};
		
		// pressing EventHandler to create a clone of the pressed Component and adding
		// it to LogicSubSceneContainer
		moveNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				// Moving Component to mouseposition while dragging it
				if (adding_component != null) {
					adding_component.setX((int) (me.getX()));
					adding_component.setY((int) (me.getY()));
				}

			}
		};
		addNewLogicComponent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(eddited_field != null) {
					eddited_field.fireEvent(new KeyEvent(KeyEvent.KEY_PRESSED,"", "", KeyCode.ENTER, false, false, false, false));
				}
				if (adding_component != null) {
					root.getChildren().remove(adding_component.getImageView());
					// Adding it to the LogicSubScene if it is dragged into the SubScene and adding
					// all Offset in the Subscene
					if (logic_subscene.getLayoutX() < me.getX() && (logic_subscene.getLayoutX() + logic_subscene.getWidth()) > me.getX() && logic_subscene.getLayoutY() < me.getY()
							&& (logic_subscene.getLayoutY() + logic_subscene.getHeight()) > me.getY()) {
						adding_component.setX((int) (me.getX() - logic_subscene.getX() + logic_subscene.getXTranslate()));
						adding_component.setY((int) (me.getY() - logic_subscene.getY() + logic_subscene.getYTranslate()));
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
	
	public void addLogicSubScene(LogicSubScene scene) {
		logic_subscenes.add(scene);
		TextField label = new TextField(scene.getName());
		label.setBackground(new Background(new BackgroundFill(LogicSubScene.black_grey, null, null)));
		label.setEditable(false);
		label.setStyle("-fx-text-fill: white; -fx-border-color: black; -fx-border-width: 1 1 0 1;");
		EventHandler<MouseEvent> add_component_handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(scene.getInputs().size()>0&&scene.getOutputs().size()>0) {
					if (me.getButton() == MouseButton.PRIMARY) {
						System.out.println("Middle");
						adding_component = scene.getLayerCanvasComponent(FunctionalCanvasComponent.SIZE_MIDDLE);
					} else if (me.getButton() == MouseButton.SECONDARY) {
						System.out.println("Small");
						adding_component = scene.getLayerCanvasComponent(FunctionalCanvasComponent.SIZE_SMALL);
					} else {
						System.out.println("Big");
						adding_component = scene.getLayerCanvasComponent(FunctionalCanvasComponent.SIZE_BIG);
					}
					adding_component.setX((int) (me.getX()));
					adding_component.setY((int) (me.getY()));
					root.getChildren().add(adding_component.getImageView());
				}
				
			}
		};
		label.addEventFilter(MouseEvent.MOUSE_PRESSED, add_component_handler);
		EventHandler<MouseEvent> rename_event_handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(me.getClickCount()>=2) {
					label.setEditable(true);
					eddited_field = label;
				}else {
					root.getChildren().remove(logic_subscene);
					logic_subscene = scene;
					root.getChildren().add(logic_subscene);
				}
			}
		};
		label.addEventFilter(MouseEvent.MOUSE_CLICKED, rename_event_handler);
		label.setOnAction(me->{
			label.setEditable(false);
			scene.setName(label.getText());
			eddited_field = null;
		});
		bEIGHT_labels.add(label);
		chooser_root.getChildren().add(label);
	}
	
	public void removeLogicSubScene(LogicSubScene scene) {
		int number = logic_subscenes.indexOf(scene);
		removeLogicSubScene(number);
	}
	public void removeLogicSubScene(int index) {
		logic_subscenes.remove(index);
		chooser_root.getChildren().remove(bEIGHT_labels.get(index));
		bEIGHT_labels.remove(index);
		root.getChildren().remove(index);
	}
	
	public void addX(int X) {
		logic_subscene.addX(X);
		setLayoutX(getLayoutX()+X);
	}
	public void addY(int Y) {
		logic_subscene.addX(Y);
		setLayoutY(getLayoutY()+Y);
	}
	
	public void saveas() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Save as");
		ExtensionFilter extFilter = new ExtensionFilter(".beight files (*.beight)", "*.beight");
        fc.getExtensionFilters().add(extFilter);
		var selected_file = fc.showSaveDialog(new Stage());
		if(selected_file != null) {
	        if(location == null) {
				location = selected_file;
				save();
			}else {
				try(FileWriter writer = new FileWriter(selected_file)){
					writer.write(logic_subscene.getJSON().toString(4));
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
        }
		
	}
	public void save() {
		if(location == null) {
			saveas();
		}else {
			try(FileWriter writer = new FileWriter(location)){
				writer.write(logic_subscene.getJSON().toString(4));
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void open(File file) {
		save();
		root.getChildren().remove(logic_subscene);
		for(int i = logic_subscenes.size()-1; i>=0; i--) {
			removeLogicSubScene(i);
		}
		logic_subscene = LogicSubScene.init(file, LogicSubScene.getNearesDot((int) (width * 0.7)), LogicSubScene.getNearesDot((int) (height * 0.9)));
		logic_subscene.setFill(color==WHITE?LogicSubScene.white_grey:LogicSubScene.black_grey);
		logic_subscene.setName("Main bEIGHT");

		logic_subscene.addX((int) (width * 0.1));
		logic_subscene.addY((int) (height * 0.01));
		
		addLogicSubScene(logic_subscene);
		
		root.getChildren().add(logic_subscene);
		location = file;
		addChooser();
		addListener();
		System.gc();
	}
	
	public void setColor(boolean color) {
		if(color != this.color) {
			this.color = color;
			if(color == WHITE) {
				logic_subscene.setFill(LogicSubScene.white_grey);
				component_chooser.setFill(LogicSubScene.white_grey);
			}else {
				logic_subscene.setFill(LogicSubScene.black_grey);
				component_chooser.setFill(LogicSubScene.black_grey);
			}
		}
	}
}
