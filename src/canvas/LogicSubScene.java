package canvas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

import application.Main;
import canvas.components.CanvasComponent;
import canvas.components.Dot;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.LogicComponent;
import canvas.components.SingleCanvasComponent;
import canvas.components.State;
import canvas.components.StandardComponents.Wire;
import canvas.components.StandardComponents.WireDoublet;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import util.OcupationExeption;
import util.ShortPair;

public class LogicSubScene extends SubScene{
	
	public static int wire_height = 12;
	public static int dot_radius = 7;
	public static int maxed_dot_radius = 20;
	public static int cross_distance = wire_height*2;
	
	protected static Color black_grey = new Color(0.3,0.3,0.3, 1.0);
	protected static Color white_grey = new Color(0.85,0.85,0.85,1.0);
	protected static Color black = new Color(0.0,0.0,0.0,1.0);

	protected Color cross_color = black;
	
	public static Color focus_square_main = white_grey;
	public static Color focus_square_secondary = black_grey;
	
	public static boolean actual_set_state = true;
	
	protected int width; 
	protected int height;
	
	protected int multiplier;
	
	protected int Start_Width;
	protected int Start_Height;
	
	protected int max_zoom;
	
	protected double X = 0;
	protected double Y = 0;
	
	private int pressed_x;
	private int pressed_y;
	private double moves_x;
	private double moves_y;
	
	private short last_focused_component = 0;	
	
	protected Camera camera;
	protected Translate camera_position;
	
	
	private FunctionalCanvasComponent adding_CanvasComponent;
	private WireDoublet adding_WireDoublet;
	public HashMap<Short, FunctionalCanvasComponent> functional_canvas_component;
	public HashMap<Short, SingleCanvasComponent> single_canvas_components;
	protected ShortPair[][] used;
	
	private Group root;
	
	public static void setTheme() {
		
	}
	
	public LogicSubScene(Group Mainroot,int StartWidth, int StartHeight, int multiplier) {
		//initializing LogicSubScene with Height
		super(Mainroot, StartWidth, StartHeight);
		
		
		max_zoom = (int) ((StartWidth*-0.5*multiplier)+(cross_distance));
		
		this.Start_Width = StartWidth;
		this.Start_Height = StartHeight;
		this.multiplier = multiplier;
		
		int Newwidth = StartWidth*multiplier;
		int Newheight = StartHeight*multiplier;
		
		if(Newwidth%cross_distance != 0||Newheight%cross_distance!=0) {
			throw new IllegalArgumentException("The size have to fit with the distance between the crosses");
		}
		
		
		width = Newwidth;
		height = Newheight;
		
		//Array of ShortPairs
		//Declares the Componentsids in each point on the area
		used = new ShortPair[Newwidth/cross_distance][Newheight/cross_distance];
		
		for(int x = 0; x <used.length; x++) {
			for(int y = 0; y < used[0].length; y++) {
				used[x][y] = new ShortPair();
			}
		}
		
		WritableImage Test_Background = generateBackgroundImage();
		
		Mainroot.getChildren().add(new ImageView(Test_Background));
		
		//Adding camera and setting it to the middle of the area
		camera_position = new Translate(0,0,0);
		camera = new PerspectiveCamera();
		camera.getTransforms().add(camera_position);
		
		camera_position.setX(width/4-cross_distance/2);
		camera_position.setY(height/4-cross_distance/2);
		
		setCamera(camera);
		
		addZTranslate(multiplier);
		
		//Maps storing components to each ID simplifies the pointers. Each component only got one external Pointer and to free it just remove it from the map
		single_canvas_components = new HashMap<>();
		functional_canvas_component = new HashMap<>();
		
		root = Mainroot;
		EventHandler<MouseEvent> dragging_Event_Handler = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent me) {
		    	//Checking which mousebutton is down
		        if (me.isSecondaryButtonDown()) {
		            addXTranslate(moves_x - (me.getSceneX() - X));
		            addYTranslate(moves_y - (me.getSceneY() - Y - 25));
		            moves_x = me.getSceneX() - X;
		            moves_y = me.getSceneY() - Y - 25;
		 
		        } else if (me.isPrimaryButtonDown()) {
		        	//primary button down
		            int new_pressed_x = (int) (me.getSceneX() - X + getXTranslate());
		            int new_pressed_y = (int) (me.getSceneY() - Y - 25 + getYTranslate());

		            if (adding_WireDoublet != null) {
		            	//Checking if a WireDoublet already exists
		                try {
		                	//Trys to remove old WireDoublet(doesn't work) and adding new Wiredoublet with new coordinates
		                    removeTry(adding_WireDoublet);
		                    adding_WireDoublet = getWires(pressed_x, pressed_y, new_pressed_x, new_pressed_y);
		                    addTry(adding_WireDoublet);
		                    System.out.println("Modified Wire doublet");
		                } catch (Exception e) {
		                    // Handle exception, if needed
		                    e.printStackTrace();
		                }
		            } else {
		            	//No wiredoublet exists so it creates a new and adds it to the SubScene
		                adding_WireDoublet = getWires(pressed_x, pressed_y, new_pressed_x, new_pressed_y);
		                try {
							addTry(adding_WireDoublet);
						} catch (OcupationExeption e) {
							e.printStackTrace();
						}
		                System.out.println("Added Wire doublet");
		            }
		        }
		    }
		};
		EventHandler<MouseEvent> press_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//Checks which Mousebutton is pressed to figure out which action to perform( try to build new Wire/moves Object or moves scene)
				System.out.println("pressed");
				//Setting the coord to createWire/moveScene 
				if(me.isPrimaryButtonDown()) {
					pressed_x = (int) (me.getSceneX()-X+getXTranslate());
					pressed_y = (int) (me.getSceneY()-Y-25+getYTranslate());
				}else if(me.isSecondaryButtonDown()) {
					moves_x = me.getSceneX()-X;
					moves_y = me.getSceneY()-Y-25;
				}
				
			}
		};

		EventHandler<MouseEvent> released_Mouse_Handler = new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent me) {
		    	//Adding Wiredoublet finally
		        if (adding_WireDoublet != null) {
		        	removeTry(adding_WireDoublet);
		            try {
		            	System.out.println(adding_WireDoublet.getHorizontalWire());
		                add(adding_WireDoublet.getHorizontalWire());
		            } catch (Exception e) {
		                // Handle exception, if needed
		                e.printStackTrace();
		            }
		            try {
		                add(adding_WireDoublet.getVerticalWire());
		            } catch (Exception e) {
		                // Handle exception, if needed
		                e.printStackTrace();
		            }
		            adding_WireDoublet = null; // Reset WireDoublet on release
		        }

		        if (adding_CanvasComponent != null) {
		            adding_CanvasComponent = null;
		        }
		    }
		};
		EventHandler<MouseEvent> click_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//Used to highlight a component but not working 
				System.out.println("click");
				if(me.isPrimaryButtonDown()) {
					try {
						
					
						short id = used[roundToNextDot((int) (me.getSceneX()-X+getXTranslate()))/cross_distance][ roundToNextDot((int) (me.getSceneY()-Y-25+getYTranslate()))/cross_distance].pickRandom();
						if(last_focused_component != 0) {
							getCanvasComponent(last_focused_component).setFocus(false);
							last_focused_component= 0;
						}
						getCanvasComponent(id).setFocus(true);
						getCanvasComponent(id).printComponents();
						last_focused_component = id;
					}catch(IllegalArgumentException iae) {
					}
				}else if(me.isSecondaryButtonDown()) {
					if(me.isStillSincePress()) {
						System.out.println("Still");
						
						if(me.getTarget() instanceof ImageView) {
							Image img = ((ImageView) me.getTarget()).getImage();
							if(img instanceof CanvasComponent) {
								CanvasComponent component = (CanvasComponent) img;
								component.setRotation(!component.getRotation());
								System.out.println("ROTATE");
							}
						}
					}
				}
				
		}};
		
		addEventFilter(MouseEvent.MOUSE_PRESSED, press_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_RELEASED, released_Mouse_Handler);
		addEventFilter(MouseEvent.MOUSE_DRAGGED, dragging_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_CLICKED, click_Event_Handler);
		
		EventHandler<ScrollEvent> zoom_Event_Handler = new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent se) {
				//Adds Z-Translate to Camera(Zooms out/in) using the mousewheel
				addZTranslate(se.getDeltaY());
			}
		};
		
		addEventFilter(ScrollEvent.SCROLL, zoom_Event_Handler);
		
	}
	
	/*private FunctionalCanvasComponent getAddComponent() {
		System.out.println(adding_CanvasComponent);
		return adding_CanvasComponent;
	}
	
	public void addTry(FunctionalCanvasComponent component) {
		//Adding Try Component without dots
		//Probably replaced by LogicSubSceneContainer
		adding_CanvasComponent = component;
		//EventHandler to add component finally by clicking on its position in the SubScene
		addFinal_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				System.out.println(adding_CanvasComponent);
				removeTry(adding_CanvasComponent);
				adding_CanvasComponent.setXPoint(getNearesDot((int) (me.getSceneX()+getXTranslate())));
				adding_CanvasComponent.setYPoint(getNearesDot((int) (me.getSceneY()+getYTranslate())));

				try {
					addFinally(adding_CanvasComponent);
				} catch (OcupationExeption e) {
				}
				adding_CanvasComponent = null;
			}
		};
		move_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				adding_CanvasComponent.setXPoint(getNearesDot((int) (me.getSceneX()+getXTranslate()-adding_CanvasComponent.getWidth()/2)));
				adding_CanvasComponent.setYPoint(getNearesDot((int) (me.getSceneY()+getYTranslate()-adding_CanvasComponent.getHeight()/2)));
			}
		};
		addEventFilter(MouseEvent.MOUSE_MOVED, move_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_RELEASED, addFinal_Event_Handler);
		root.getChildren().add(component.getImageView());
	}
	public void removeTry(FunctionalCanvasComponent component){
		
		//removeEventFilter(MouseEvent.MOUSE_MOVED, move_Event_Handler);
		//removeEventFilter(MouseEvent.MOUSE_CLICKED, addFinal_Event_Handler); 
		//removes try component
		move_Event_Handler = null;
		addFinal_Event_Handler = null;
		root.getChildren().remove(component.getImageView());
	}*/
	public void add(FunctionalCanvasComponent component) throws OcupationExeption {
		short ID = generateRandomFunctionalComponent();
		System.out.println("ELEMENT: "+component.getX()+" "+component.getY());
		root.getChildren().add(component.getImageView());
		component.setStandardDotLocations();
		//Adding each Dot(Communication between wires and Components) 
		for(Dot d : component.inputs) {
			add(d);
			d.setState(State.getState(true, Main.random.nextBoolean()));
		
		}
		for(Dot d : component.outputs) {
			add(d);
		}
		//Adding component to the ID-System
		component.setLogicSubScene(this);
		component.setId(ID);
		functional_canvas_component.put(ID, component);
		//TODO blocking area where the gate is located
		System.out.println("Add Finally");
	}
	
	public void addTry(WireDoublet doublet) throws OcupationExeption {
		//Adding each Wire of a WireDoublet
		if(doublet.getHorizontalWire() != null) {
			root.getChildren().add(doublet.getHorizontalWire().getImageView());
			System.out.println(root.getChildren().contains(doublet.getHorizontalWire().getImageView()));
		}
		if(doublet.getVerticalWire() != null) {
			root.getChildren().add(doublet.getVerticalWire().getImageView());
		}
		System.out.println("Added try WireDoublet");
		
	}
	
	public void add(WireDoublet doublet) throws OcupationExeption {
		//Adding each Wire of a WireDoublet
		add(doublet.getHorizontalWire());
		add(doublet.getVerticalWire());
		System.out.println("Added WireDoublet");
		
	}
	public void add(SingleCanvasComponent component) throws OcupationExeption {
		if(component != null) {
				
			short ID = generateRandomSingleComponentID();		
			short loc_ID;
			
			System.out.println(ID);
			//Checking other elements blocking the Wire/connecting wires together
			if(component.rotation == CanvasComponent.HORIZONTAL) {
				//Checking for horizontal and adding the ID to the Horizonzal Short in ShortPair
				for(int x = component.getXPoint()+1; x < component.getXPoint()+component.getWidthPoint(); x++) {
					loc_ID = used[x][component.getYPoint()].HorizontalShort;
					if(loc_ID == 0) {
						used[x][component.getYPoint()].HorizontalShort = ID;
					}else if(loc_ID == 1){
						throw new OcupationExeption();
					}else {
						if(getCanvasComponent(loc_ID).checkEnd(x, component.getYPoint())) {
							getCanvasComponent(loc_ID).addComponent(ID);
							component.addComponent(loc_ID);
						}
					}
				}
				
				loc_ID = used[component.getXPoint()][component.getYPoint()].HorizontalShort;
				if(loc_ID==0) {
					used[component.getXPoint()][component.getYPoint()].HorizontalShort = ID;
				}else if(loc_ID==1) {
					throw new OcupationExeption();
				}else {
					getCanvasComponent(loc_ID).addComponent(ID);
					component.addComponent(loc_ID);
				}
				
				//Sets End/Start Point
				loc_ID = used[component.getXPoint()+component.getWidthPoint()][component.getYPoint()].HorizontalShort;
				System.out.println(loc_ID);
				if(loc_ID==0) {
					used[component.getXPoint()+component.getHeightPoint()][component.getYPoint()].HorizontalShort = ID;
				}else if(loc_ID==1) {
					throw new OcupationExeption();
				}else {
					System.out.println("test");
					getCanvasComponent(loc_ID).addComponent(ID);
					component.addComponent(loc_ID);
				}
			}else {
				//Same as for Horizontal with Vertical
				for(int y = component.getYPoint()+1; y < component.getYPoint()+component.getHeightPoint(); y++) {
					loc_ID = used[component.getXPoint()][y].VerticalShort;
					if(loc_ID == 0) {
						used[component.getXPoint()][y].VerticalShort = ID;
					}else if(loc_ID == 1){
						throw new OcupationExeption();
					}else {
						if(getCanvasComponent(loc_ID).checkEnd(component.getXPoint(), y)) {
							getCanvasComponent(loc_ID).addComponent(ID);
							component.addComponent(loc_ID);
						}
					}
				}
				
	
				//Sets End/Start Point
				loc_ID = used[component.getXPoint()][component.getYPoint()].VerticalShort;
				if(loc_ID==0) {
					used[component.getXPoint()][component.getYPoint()].VerticalShort = ID;
				}else if(loc_ID==1) {
					throw new OcupationExeption();
				}else {
					getCanvasComponent(loc_ID).addComponent(ID);
					component.addComponent(loc_ID);
				}
				
				loc_ID = used[component.getXPoint()][component.getYPoint()+component.getHeightPoint()].VerticalShort;
				if(loc_ID==0) {
					used[component.getXPoint()][component.getYPoint()+component.getHeightPoint()].VerticalShort = ID;
				}else if(loc_ID==1) {
					throw new OcupationExeption();
				}else {
					getCanvasComponent(loc_ID).addComponent(ID);
					component.addComponent(loc_ID);
				}
				
				
			}
			//Adding component to SubSCene
			component.setLogicSubScene(this);
			component.setId(ID);
			root.getChildren().add(component.getImageView());
			
			single_canvas_components.put(ID, component); 
			component.printComponents();
		}
	}
	public void add(Dot component) throws OcupationExeption {
		if(component != null) {
			short ID = generateRandomSingleComponentID();		
			ShortPair loc_ID;
			
			System.out.println(ID);
			
			//Checking Point where Dot is added and connecting it to other SingleCanvasComponents
			loc_ID = used[component.point_X][component.point_Y];
			
			if(loc_ID.Dot == 0) {
				used[component.point_X][component.point_Y].Dot = ID;
			}else if(loc_ID.Dot == 1) {
				throw new OcupationExeption();
			}else {
				getCanvasComponent(loc_ID.Dot).addComponent(ID);
				component.addComponent(loc_ID.Dot);
			}
			
			component.setLogicSubScene(this);
			component.setId(ID);
			root.getChildren().add(component.getImageView());
			
			single_canvas_components.put(ID, component); 
			//component.printComponents();
			//System.out.println("Line: 519");
		}
	}
	
	public void remove(FunctionalCanvasComponent component) {
		//Removing similar to adding in opposition
		for(Dot d : component.inputs) {
			remove(d);
		}
		for(Dot d : component.outputs) {
			remove(d);
		}
		functional_canvas_component.remove(component.getId());
		root.getChildren().remove(component.getImageView());
	}
	
	public void removeTry(WireDoublet doublet) {
		if(doublet.getHorizontalWire() != null) {
			root.getChildren().remove(doublet.getHorizontalWire().getImageView());
			System.out.println("Removed Horizontal");
		}
		if(doublet.getVerticalWire() != null) {
			root.getChildren().remove(doublet.getVerticalWire().getImageView());
			System.out.println("Removed Vertical");
		}
		System.out.println("Removed Try Wiredoublet");
	}
	public void remove(WireDoublet doublet) {
		remove(doublet.getHorizontalWire());
		remove(doublet.getVerticalWire());
	}
	
	public void remove(SingleCanvasComponent component) {
		//TODO Remove SingleCanvasComponent from used array, replace it at the end with connected 
		if(component != null) {
			if(component.rotation == CanvasComponent.HORIZONTAL) {
				//Removing Horizontal/Vertical ID from used 
				for(int x = component.getXPoint(); x<=component.getXPoint()+component.getWidthPoint(); x++) {
					used[x][component.getYPoint()].HorizontalShort = 0;
					component.printComponents();
				}
			}else {
				for(int y = component.getYPoint(); y <=component.getYPoint()+component.getWidthPoint(); y++) {
					used[component.getXPoint()][y].VerticalShort = 0;
				}
			}
			single_canvas_components.remove(component.getId());
			root.getChildren().remove(component.getImageView());
		}
	}
	
	
	public short generateRandomSingleComponentID() {
		//Generates ID for SingleComponents by generating them and checking its existence and generating a new
		short ID = (short) Main.random.nextInt(1 << 15);
		if(ID <= 1) {
			return generateRandomSingleComponentID();
		}
		if(single_canvas_components.getOrDefault(ID, null) != null) {
			return generateRandomSingleComponentID();
		}else {
			return ID;
		}
	}
	
	public short generateRandomFunctionalComponent() {
		//Same as for SingleComponents for FunctionalComponents
		short ID = (short) Main.random.nextInt(1 << 15);
		if(ID <= 1) {
			return generateRandomFunctionalComponent();
		}
		if(functional_canvas_component.getOrDefault(ID, null) != null) {
			return generateRandomFunctionalComponent();
		}else {
			return ID;
		}
	}
	
	public static LogicSubScene init(int start_width, int start_height, int multiplier) {
		//Initializing LogicSubScene with a new Group
		return new LogicSubScene(new Group(),start_width, start_height, multiplier);
	}
	
	protected WritableImage generateBackgroundImage() {
		//Generates BackgroundImage with crosses every cross_distance
		WritableImage image = new WritableImage(width, height);
		PixelWriter writer = image.getPixelWriter();
		for(int x = 0; x <=width-1; x+=cross_distance) {
			for(int y = 0; y <= height-1; y+=cross_distance) {
				writer.setColor(x+1, y, black);
				writer.setColor(x, y+1, black);
				writer.setColor(x, y, black);
				
				if(x>=1) {
				writer.setColor(x-1, y, black);
				}
				if(y>=1) {
					writer.setColor(x, y-1, black);
				}
			}
		}
		return image;
	}
	
	public static int roundToNextDot(int coordinat) throws IllegalArgumentException{
		//Check nearest dot but throwing error if it is too far away
		int overflow = coordinat % cross_distance;
		if(overflow<=cross_distance/3) {
			return coordinat-overflow;
		}else if(cross_distance-overflow<=cross_distance/3) {
			return coordinat+cross_distance-overflow;
		}else {
			throw new IllegalArgumentException();
		}
	}
	public static int getNearesDot(int coordinat) {
		//returning nearest dot
		int overflow = coordinat% cross_distance;
		if(overflow<=cross_distance/2) {
			return coordinat-overflow;
		}else {
			return coordinat+cross_distance-overflow;
		}
	}
	
	public WireDoublet getWires(int start_X, int start_Y, int end_X, int end_Y) throws IllegalArgumentException{
		//Generating WireDoublet with two Wires
		boolean error = false;
		int round_start_x;
		int round_start_y;
		try {
			round_start_x = roundToNextDot(start_X);
		}catch(Exception e) {
			round_start_x = getNearesDot(start_X);
			error = true;
		}
		try {
			round_start_y = roundToNextDot(start_Y);
		}catch(Exception e) {
			if(error) {
				throw new IllegalArgumentException();
			}else {
				round_start_y = getNearesDot(start_X);
			}
		}
		round_start_y = roundToNextDot(start_Y);
		int round_end_x = getNearesDot(end_X);
		int round_end_y = getNearesDot(end_Y);
		
		WireDoublet doublet = new WireDoublet();
		
		//Checking if there is a Horizontal Wire and generating it if yes
		if(round_start_x!=round_end_x) {
			Wire wire_horizontal = new Wire(Math.abs(round_start_x-round_end_x)+wire_height*3/4);
			
			//Location relative to point and minus half of 
			if(round_start_x >= round_end_x) {
				wire_horizontal.setX(round_end_x-wire_height/2);
			}else {
				wire_horizontal.setX(round_start_x-wire_height/2);
			}
			wire_horizontal.setY(round_start_y-wire_height/2);
			
			wire_horizontal.setState(CanvasComponent.UNSET);
			
			doublet.setHorizontalWire(wire_horizontal);
			wire_horizontal.setRotation(CanvasComponent.HORIZONTAL);
		}
		//Checking if there is a vertical Wire and generating it if yes
		if(round_start_y!=round_end_y) {
			Wire wire_vertical = new Wire(Math.abs(round_start_y-round_end_y)+wire_height*3/4);
			if(round_start_y>=round_end_y) {
				wire_vertical.setY(round_end_y-wire_height/2);
			}else {
				wire_vertical.setY(round_start_y-wire_height/2);
			}
			wire_vertical.setX(round_end_x-wire_height/2);
			
			wire_vertical.setState(CanvasComponent.UNSET);
			wire_vertical.setRotation(CanvasComponent.VERTICAL);
			
			doublet.setVerticalWire(wire_vertical);
		}
		return doublet;
	}
	
	//Returning SingleCanvasComponent accessible from outside
	public SingleCanvasComponent getCanvasComponent(short id) {
		return single_canvas_components.get(id); 
	}
	
	//Moving whole SubScene in X/Y direction
	public void addX(int ADD_X) {
		X = getLayoutX()+ADD_X;
		setLayoutX(X);
	}
	public void addY(int ADD_Y) {
		Y = getLayoutY()+ADD_Y;
		setLayoutY(Y);
	}
	//Getting current movement
	public double getX() {
		return X;
	}
	public double getY() {
		return Y;
	}
	//Setting Zoom to standard
	public void setStandardZoom() {
		camera_position.setZ(multiplier);
	}
	
	//Adding Camera Translate
	public void addZTranslate(double z) {
		double Z_Postion = camera_position.getZ()+z;
		if(Z_Postion <= max_zoom) {		
		}else {
			camera_position.setZ(Z_Postion);
		}
		checkXYTanslate();
	}
	public void addXTranslate(double x) {
		double X_Postion = camera_position.getX()+x;
			if(X_Postion<0||X_Postion>(width-Start_Width)) {
			}else {	
				camera_position.setX(X_Postion);
			}
	}
	public void addYTranslate(double y) {
		double Y_Position = camera_position.getY()+y;
		if(Y_Position<0||Y_Position>(height-Start_Height)) {
		}else {
			camera_position.setY(Y_Position);
		}
	}
	
	public void checkXYTanslate(){
		//TODO prevent CameraPosition to move outside depending on zoom
		if(getZTranslate()/-2 > getXTranslate()) {
			camera_position.setX(getZTranslate()/-2);
			System.out.println(getXTranslate()+"X-Coord");
		}
	}
	
	//Getting camera Translate
	public double getZTranslate() {
		return camera_position.getZ();
	}
	public double getXTranslate() {
		return camera_position.getX();
	}
	public double getYTranslate() {
		return camera_position.getY();
	}
	
	public void SaveAsPDF(String filepath) {
		//Creating PDF out of SubScene
		File temp_image_file = new File("temp.png");
		try{
			double start_Z = getZTranslate();
			
			//camera_position.setZ(max_zoom);
			
			WritableImage image = this.snapshot(null, null);
			BufferedImage buff_image = SwingFXUtils.fromFXImage(image, null);
			
			// Create a new PDF document
	        PDDocument document = new PDDocument();
	        PDPage page = new PDPage();
	        document.addPage(page);

	        // Create a content stream for writing to the page
	        PDPageContentStream contentStream = new PDPageContentStream(document, page);

	        // Draw the image on the page
	        contentStream.drawImage(
	                LosslessFactory.createFromImage(document, buff_image),
	                0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

	        // Close the content stream
	        contentStream.close();

	        // Save the document to the output file
	        document.save(filepath);

	        // Close the document
	        document.close();
			
	        camera_position.setZ(start_Z);
	        
		}catch(IOException e) {
			e.printStackTrace();
		}
		temp_image_file.delete();
	}
}
