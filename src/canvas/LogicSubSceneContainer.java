package canvas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import canvas.components.FunctionalCanvasComponent;
import canvas.components.LogicComponent;
import canvas.components.ExternalComponents.ExternalComponent;
import canvas.components.StandardComponents.HexInput;
import canvas.components.StandardComponents.Input;
import canvas.components.StandardComponents.Output;
import canvas.components.StandardComponents.SevenSegmentDisplay;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import canvas.components.StandardComponents.LogicComponents.NANDGate;
import canvas.components.StandardComponents.LogicComponents.NORGate;
import canvas.components.StandardComponents.LogicComponents.NOTGate;
import canvas.components.StandardComponents.LogicComponents.ORGate;
import canvas.components.StandardComponents.LogicComponents.XNORGate;
import canvas.components.StandardComponents.LogicComponents.XORGate;
import canvas.components.StandardComponents.MemoryComponents.Register;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import net.lingala.zip4j.ZipFile;
import util.IllegalInputOutputExeption;
import util.OcupationExeption;

public class LogicSubSceneContainer extends SubScene {

	public static boolean BLACK = false;
	public static boolean WHITE = true;
	
	protected static ComponentGroupings master_grouping;
	protected static ComponentGroupings slave_grouping;
	
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
	public SubScene component_chooser_master;
	public SubScene component_chooser_slave;
	public VBox chooser_root;
	public SubScene bEIGHT_chooser;
	private FunctionalCanvasComponent adding_component;
	private Group root;

	public LogicSubSceneContainer(int width, int height, Group Mainroot, ComponentGroupings groupings, int multiplier) {
		//Initializing for the Education unit
		super(Mainroot, width, height);
		this.root = Mainroot;
		this.width = width;
		this.height = height;
		this.grouping = groupings;
		// Adding LogicScene
		logic_subscene = LogicSubScene.init(LogicSubScene.getNearesDot((int) (width * 0.80)), LogicSubScene.getNearesDot((int) (height * 0.9)), multiplier);

		logic_subscene.setFill(LogicSubScene.black_grey);
		
		logic_subscene.setName("Main bEIGHT");

		logic_subscene.addY((int) (height * 0.01));
		
		
		root.getChildren().add(logic_subscene);

		addChooser();
		addListener();
	}
	
	public LogicSubSceneContainer(int width, int height, Group Mainroot, int multiplier) {
		super(Mainroot, width, height);
		this.root = Mainroot;
		this.width = width;
		this.height = height;
		
		if(master_grouping == null) {
			addGroupings();
		}
		
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
				addLogicSubScene(scene, false);
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
		
		logic_subscene.setName("Master beight");

		logic_subscene.addX((int) (width * 0.1));
		logic_subscene.addY((int) (height * 0.01));
		
		addLogicSubScene(logic_subscene, true);
		
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
		
		return new LogicSubSceneContainer(width, height, new Group(), 4);
	}
	
	public static void addGroupings() {
		//Add all elements to the master grouping you can choose from on the master beight. E.g. you cannot use a Hex-Input in a slave beight
		master_grouping = new ComponentGroupings();
		ComponentGroup group = new ComponentGroup();
		try {

			group.add(ANDGate.getANDGATE(LogicComponent.SIZE_MIDDLE, 2));
			group.add(NANDGate.getNANDGATE(LogicComponent.SIZE_MIDDLE, 2));
			group.add(ORGate.getORGATE(LogicComponent.SIZE_MIDDLE, 2));
			group.add(NORGate.getNORGATE(LogicComponent.SIZE_MIDDLE, 2));
			group.add(XORGate.getXORGate(LogicComponent.SIZE_MIDDLE, 2));
			group.add(XNORGate.getXNORGate(LogicComponent.SIZE_MIDDLE, 2));
			group.add(NOTGate.getNOTGATE(LogicComponent.SIZE_MIDDLE));
		} catch (IllegalArgumentException iae) {
		}
		ComponentGroup group_1 = new ComponentGroup();
		group_1.add(Input.getInput(FunctionalCanvasComponent.SIZE_BIG));
		group_1.add(Output.getOutput(FunctionalCanvasComponent.SIZE_BIG));
		ComponentGroup group_2 = new ComponentGroup();
		ZipFile file = new ZipFile("dlatch.cmp");
		ExternalComponent comp = ExternalComponent.init(FunctionalCanvasComponent.SIZE_MIDDLE, file);
		System.out.println("DLatch added");
		//ExternalComponent fulladder = ExternalComponent.init(ExternalComponent.SIZE_MIDDLE, new ZipFile("testfiles/FullAdder.cmp"));
		//System.out.println("FullAdder added");
		ExternalComponent twobitadder = ExternalComponent.init(FunctionalCanvasComponent.SIZE_MIDDLE, new ZipFile("testfiles/2BitAdder.cmp"));
		System.out.println("2BitAdder added");
		group_2.add(comp);
		//group_2.add(fulladder);
		group_2.add(twobitadder);
		ComponentGroup group_3 = new ComponentGroup();
		SevenSegmentDisplay ssd = new SevenSegmentDisplay((int)(LogicSubScene.cross_distance*2.5),(int) (LogicSubScene.cross_distance*4.5));
		HexInput hex = new HexInput((int)(LogicSubScene.cross_distance*2.5),(int) (LogicSubScene.cross_distance*4.5));
		//group_3.add(Register.getRegister(LogicComponent.SIZE_MIDDLE, 8));
		ssd.setNumber(8);
		group_3.add(ssd);
		group_3.add(hex);
		master_grouping.add(group);
		master_grouping.add(group_1);
		master_grouping.add(group_2);
		master_grouping.add(group_3);
		
		//Add all elements to the slave grouping you can choose from on the slave beight. E.g. you cannot use a Hex-Input in a slave beight
		slave_grouping= new ComponentGroupings();
		ComponentGroup sgroup = new ComponentGroup();
		try {

			sgroup.add(ANDGate.getANDGATE(LogicComponent.SIZE_MIDDLE, 2));
			sgroup.add(NANDGate.getNANDGATE(LogicComponent.SIZE_MIDDLE, 2));
			sgroup.add(ORGate.getORGATE(LogicComponent.SIZE_MIDDLE, 2));
			sgroup.add(NORGate.getNORGATE(LogicComponent.SIZE_MIDDLE, 2));
			sgroup.add(XORGate.getXORGate(LogicComponent.SIZE_MIDDLE, 2));
			sgroup.add(XNORGate.getXNORGate(LogicComponent.SIZE_MIDDLE, 2));
			sgroup.add(NOTGate.getNOTGATE(LogicComponent.SIZE_MIDDLE));
		} catch (IllegalArgumentException iae) {
		}
		ComponentGroup sgroup_1 = new ComponentGroup();
		sgroup_1.add(Input.getInput(FunctionalCanvasComponent.SIZE_BIG));
		sgroup_1.add(Output.getOutput(FunctionalCanvasComponent.SIZE_BIG));

		slave_grouping.add(sgroup);
		slave_grouping.add(sgroup_1);
	}

	public void triggerKeyEvent(KeyEvent ke) {
		logic_subscene.triggerKeyEvent(ke);
	}
	private void addChooser() {
		if(component_chooser_master != null) {
			root.getChildren().remove(component_chooser_master);
		}
		// Adding ComponentChooser and set the layout
		component_chooser_master = new ComponentChooser(logic_subscene, new ScrollPane(), width * 0.15, LogicSubScene.getNearesDot((int) (height * 0.9)), master_grouping);
		component_chooser_master.setFill(Color.WHITE);
		component_chooser_master.setLayoutX(width * 0.8);
		component_chooser_master.setLayoutY(height * 0.01);
		component_chooser_master.setFill(LogicSubScene.black_grey);
		
		component_chooser_slave = new ComponentChooser(logic_subscene, new ScrollPane(), width*0.15, LogicSubScene.getNearesDot((int) height), slave_grouping);
		component_chooser_slave.setFill(Color.WHITE);
		component_chooser_slave.setLayoutX(width * 0.8);
		component_chooser_slave.setLayoutY(height * 0.01);
		component_chooser_slave.setFill(LogicSubScene.black_grey);
		
		root.getChildren().add(component_chooser_master);		
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
				if (component_chooser_master.getLayoutX() < me.getX() && component_chooser_master.getLayoutY() < me.getY()) {
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
	
	public void addLogicSubScene(LogicSubScene scene, boolean ismaster) {
		logic_subscenes.add(scene);
		TextField label = new TextField(scene.getName());
		label.setBackground(new Background(new BackgroundFill(LogicSubScene.black_grey, null, null)));
		label.setEditable(false);
		label.setStyle("-fx-text-fill: white; -fx-border-color: black; -fx-border-width: 1 1 0 1;");
		EventHandler<MouseEvent> add_component_handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(scene.getInputs().size()>0&&scene.getOutputs().size()>0&&!ismaster) {
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
				if(me.getClickCount()>=2 && !ismaster) {
					label.setEditable(true);
					eddited_field = label;
				}else {
					root.getChildren().remove(logic_subscene);
					logic_subscene = scene;
					root.getChildren().add(logic_subscene);
					if(ismaster) {
						root.getChildren().removeAll(component_chooser_master, component_chooser_slave);
						root.getChildren().add(component_chooser_master);
					}else {
						root.getChildren().removeAll(component_chooser_master, component_chooser_slave);
						root.getChildren().add(component_chooser_slave);
					}
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
		
		addLogicSubScene(logic_subscene, false);
		
		root.getChildren().add(logic_subscene);
		location = file;
		addChooser();
		addListener();
		System.gc();
	}
	
	public void saveArduino(File file) {
		Stage pin_planer = new Stage();
		Group pin_root = new Group();
		Scene pin_scene = new Scene(pin_root);
		pin_planer.setScene(pin_scene);	
		
		VBox vbox = new VBox(50);
		ScrollPane pane = new ScrollPane(vbox);
		
		ImageView arduino_schematics = new ImageView(new Image("ArduinoUno.png"));
		vbox.getChildren().add(arduino_schematics);
		
		ComboBox<Integer> pin_chooser = new ComboBox<>();
		pin_chooser.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10,11,12,13);
		
		VBox input_chooser = new VBox();
		int[] input_pins = new int[logic_subscene.getInputs().size()];
		for(int i = 0; i<logic_subscene.getInputs().size(); i++) {
			HBox pin_input_chooser = new HBox();
			Label pin_label = new Label("Input "+(i+1)+":");
			ComboBox<Integer> pin_input = new ComboBox<>();
			pin_input.setItems(pin_chooser.getItems());
			final int number = i;
			pin_input.valueProperty().addListener(new ChangeListener<Integer>() {
				@Override
				public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
					input_pins[number] = newValue;
				}
			});
			pin_input.setValue(2);
			pin_input_chooser.getChildren().addAll(pin_label, pin_input);
			input_chooser.getChildren().add(pin_input_chooser);
		}
		int[] output_pins = new int[logic_subscene.getOutputs().size()];
		VBox output_chooser = new VBox();
		for(int i = 0; i<logic_subscene.getOutputs().size(); i++) {
			HBox pin_output_chooser = new HBox();
			Label pin_label = new Label("Output "+(i+1)+":");
			ComboBox<Integer> pin_output = new ComboBox<>();
			pin_output.setItems(pin_chooser.getItems());
			final int number = i;
			pin_output.valueProperty().addListener(new ChangeListener<Integer>() {
				@Override
				public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
					output_pins[number] = newValue;
				}
			});
			pin_output.setValue(3);
			pin_output_chooser.getChildren().addAll(pin_label, pin_output);
			output_chooser.getChildren().add(pin_output_chooser);
		}
		HBox pin_choosers = new HBox();
		pin_choosers.getChildren().addAll(input_chooser, output_chooser);
		HBox ButtonBox = new HBox();
		Button cancel = new Button("cancel");
		cancel.setFont(new Font(50));
		cancel.setOnAction(e->{
			pin_planer.close();
		});
		Button save = new Button("save");
		save.setFont(new Font(50));
		save.setOnAction(e->{
			try(FileWriter writer = new FileWriter(file)) {
				writer.write(logic_subscene.getArduino(input_pins, output_pins));
			} catch (IOException ie) {
				ie.printStackTrace();
			}catch(IllegalInputOutputExeption ie) {
				Stage stage = new Stage();
				Group scene_root = new Group();
				Scene scene = new Scene(scene_root);
				Label label = new Label("Not enough Inputs/Outputs.\nPlease add new Inputs/Outputs");
				scene_root.getChildren().add(label);
				stage.setScene(scene);
				stage.show();
			}
			pin_planer.close();
		});
		ButtonBox.getChildren().addAll(cancel, save);
		vbox.getChildren().addAll(pin_choosers, ButtonBox);
		
		pin_root.getChildren().add(pane);
		pin_planer.show();
		
	}
	
	public void uploadArduino() {
		saveArduino(new File("beighduino/beighduino.ino"));
		Stage output_chooser = new Stage();
		Group output_root = new Group();
		Scene output_scene = new Scene(output_root);
		output_chooser.setScene(output_scene);
		ComboBox<String> port = new ComboBox<String>();
		port.setValue("Port");
		ArrayList<String> boards = new ArrayList<String>();
		try {
			Process board_read = Runtime.getRuntime().exec("beighduino/arduino-cli.exe board list");
			BufferedReader reader = new BufferedReader(new InputStreamReader(board_read.getInputStream()));
			String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
        		try{
            		String[] line_segments = line.split(" ");
	            	String board_name = line_segments[0];
	            	board_name+=" "+line.substring(line.indexOf("Arduino"), line.indexOf("arduino")-1);
	            	port.getItems().add(board_name);
	            	boards.add(line);
	            	System.out.println(board_name);
        		}catch(StringIndexOutOfBoundsException e) {
        			
        		}
            }

            int exitCode = board_read.waitFor();
            System.out.println("Exited with code: " + exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		VBox box = new VBox();
		box.getChildren().add(port);
		HBox ButtonBox = new HBox();
		Button cancel = new Button("cancel");
		cancel.setFont(new Font(50));
		cancel.setOnAction(e->{
			output_chooser.close();
		});
		Button upload = new Button("upload");
		upload.setFont(new Font(50));
		upload.setOnAction(e->{
			if(port.getItems().contains(port.getValue())) {
				int number = port.getItems().indexOf(port.getValue());
				String board_line = boards.get(number);
				String board_name = board_line.substring(board_line.indexOf("arduino"), board_line.lastIndexOf("arduino")-1);
				String[] line_segment = port.getValue().split(" ");
				try {
					String compileCommand = "beighduino/arduino-cli.exe compile --fqbn "+board_name+" beighduino/beighduino.ino";
		            String uploadCommand = "beighduino/arduino-cli.exe upload -p "+line_segment[0]+" --fqbn "+board_name+" beighduino/beighduino.ino";

		            ProcessBuilder builder = new ProcessBuilder(compileCommand.split(" "));
		            builder.redirectErrorStream(true);
		            Process process = builder.start();
		            
		            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		            String line;
		            while ((line = reader.readLine()) != null) {
		                System.out.println(line);
		            }

		            // Wait for the command to finish
		            int exitCode = process.waitFor();
		            System.out.println("Compile command exited with code " + exitCode);

		            // Run the upload command
		            builder = new ProcessBuilder(uploadCommand.split(" "));
		            builder.redirectErrorStream(true);
		            process = builder.start();

		            // Read the output of the command
		            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		            while ((line = reader.readLine()) != null) {
		                System.out.println(line);
		            }

		            // Wait for the command to finish
		            exitCode = process.waitFor();
		            System.out.println("Upload command exited with code " + exitCode);
				}catch (IOException | InterruptedException ie) {
					ie.printStackTrace();
				}
				output_chooser.close();
			}
		});
		ButtonBox.getChildren().addAll(cancel, upload);
		box.getChildren().add(ButtonBox);
		output_root.getChildren().add(box);
		output_chooser.show();
	}
	
	public void saveArduinoShield(File file) {
		try(FileWriter writer = new FileWriter(file)){
			writer.write(logic_subscene.getBeighduinoshield());
		} catch (IOException | IllegalInputOutputExeption e) {
			e.printStackTrace();
		}
	}
	
	public void uploadArduinoShield() {
		saveArduinoShield(new File("beighduino-shield/beighduino-shield.ino"));
		Stage output_chooser = new Stage();
		Group output_root = new Group();
		Scene output_scene = new Scene(output_root);
		output_chooser.setScene(output_scene);
		ComboBox<String> port = new ComboBox<String>();
		port.setValue("Port");
		ArrayList<String> boards = new ArrayList<String>();
		try {
			Process board_read = Runtime.getRuntime().exec("beighduino/arduino-cli.exe board list");
			BufferedReader reader = new BufferedReader(new InputStreamReader(board_read.getInputStream()));
			String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
        		try{
            		String[] line_segments = line.split(" ");
	            	String board_name = line_segments[0];
	            	board_name+=" "+line.substring(line.indexOf("Arduino"), line.indexOf("arduino")-1);
	            	port.getItems().add(board_name);
	            	boards.add(line);
	            	System.out.println(board_name);
        		}catch(StringIndexOutOfBoundsException e) {
        			
        		}
            }

            int exitCode = board_read.waitFor();
            System.out.println("Exited with code: " + exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		VBox box = new VBox();
		box.getChildren().add(port);
		HBox ButtonBox = new HBox();
		Button cancel = new Button("cancel");
		cancel.setFont(new Font(50));
		cancel.setOnAction(e->{
			output_chooser.close();
		});
		Button upload = new Button("upload");
		upload.setFont(new Font(50));
		upload.setOnAction(e->{
			if(port.getItems().contains(port.getValue())) {
				int number = port.getItems().indexOf(port.getValue());
				String board_line = boards.get(number);
				String board_name = board_line.substring(board_line.indexOf("arduino"), board_line.lastIndexOf("arduino")-1);
				String[] line_segment = port.getValue().split(" ");
				try {
					String compileCommand = "beighduino/arduino-cli.exe compile --fqbn "+board_name+" beighduino-shield/beighduino-shield.ino";
		            String uploadCommand = "beighduino/arduino-cli.exe upload -p "+line_segment[0]+" --fqbn "+board_name+" beighduino-shield/beighduino-shield.ino";

		            ProcessBuilder builder = new ProcessBuilder(compileCommand.split(" "));
		            builder.redirectErrorStream(true);
		            Process process = builder.start();
		            
		            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		            String line;
		            while ((line = reader.readLine()) != null) {
		                System.out.println(line);
		            }

		            // Wait for the command to finish
		            int exitCode = process.waitFor();
		            System.out.println("Compile command exited with code " + exitCode);

		            // Run the upload command
		            builder = new ProcessBuilder(uploadCommand.split(" "));
		            builder.redirectErrorStream(true);
		            process = builder.start();

		            // Read the output of the command
		            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		            while ((line = reader.readLine()) != null) {
		                System.out.println(line);
		            }

		            // Wait for the command to finish
		            exitCode = process.waitFor();
		            System.out.println("Upload command exited with code " + exitCode);
				}catch (IOException | InterruptedException ie) {
					ie.printStackTrace();
				}
				output_chooser.close();
			}
		});
		ButtonBox.getChildren().addAll(cancel, upload);
		box.getChildren().add(ButtonBox);
		output_root.getChildren().add(box);
		output_chooser.show();
	}
	
	public void setColor(boolean color) {
		if(color != this.color) {
			this.color = color;
			if(color == WHITE) {
				logic_subscene.setFill(LogicSubScene.white_grey);
				component_chooser_master.setFill(LogicSubScene.white_grey);
				component_chooser_slave.setFill(LogicSubScene.white_grey);
			}else {
				logic_subscene.setFill(LogicSubScene.black_grey);
				component_chooser_master.setFill(LogicSubScene.black_grey);
				component_chooser_slave.setFill(LogicSubScene.black_grey);
			}
		}
	}
}
