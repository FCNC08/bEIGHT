package canvas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

import canvas.components.CanvasComponent;
import canvas.components.Dot;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.SingleCanvasComponent;
import canvas.components.StandardComponents.Wire;
import canvas.components.StandardComponents.WireDoublet;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

public class LogicSubScene extends SubScene{
	
	public static Random random = new Random();
	
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
	
	private boolean adding;
	private boolean primary;
	private boolean secondary;
	
	protected Camera camera;
	protected Translate camera_position;
	
	
	private CanvasComponent adding_CanvasComponent;
	private WireDoublet adding_WireDoublet;
	public HashMap<Short, FunctionalCanvasComponent> functional_canvas_component;
	public HashMap<Short, SingleCanvasComponent> single_canvas_components;
	protected short[][] used;
	
	private Group root;
	
	public static void setTheme() {
		
	}
	
	public LogicSubScene(Group Mainroot,int StartWidth, int StartHeight, int multiplier) {
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
		
		used = new short[Newwidth/cross_distance][Newheight/cross_distance];
		
		
		WritableImage Test_Background = generateBackgroundImage();
		
		Mainroot.getChildren().add(new ImageView(Test_Background));
		
		camera_position = new Translate(0,0,0);
		camera = new PerspectiveCamera();
		camera.getTransforms().add(camera_position);
		
		camera_position.setX(width/4-cross_distance/2);
		camera_position.setY(height/4-cross_distance/2);
		
		setCamera(camera);
		
		addZTranslate(multiplier);
		
		
		single_canvas_components = new HashMap<>();
		functional_canvas_component = new HashMap<>();
		
		root = Mainroot;
		
		EventHandler<MouseEvent> dragging_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(me.isSecondaryButtonDown())
				{
					addXTranslate(moves_x-(me.getSceneX()-X));
					addYTranslate(moves_y-(me.getSceneY()-Y-25));
					//setCursor(javafx.scene.Cursor.NONE);
					moves_x = me.getSceneX()-X;
					moves_y = me.getSceneY()-Y-25;
				}
			}
		};		
		EventHandler<MouseEvent> press_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//Checks which Mousebutton is pressed to figure out which action to perform( try to build new Wire/moves Object or moves scene)
				primary = me.isPrimaryButtonDown();
				secondary = me.isSecondaryButtonDown();
				
				if(primary) {
					adding = true;
					pressed_x = (int) (me.getSceneX()-X+getXTranslate());
					pressed_y = (int) (me.getSceneY()-Y-25+getYTranslate());
					if(last_focused_component != 0) {
						getCanvasComponent(last_focused_component).setFocus(false);
						last_focused_component= 0;
					}
					adding_WireDoublet = new WireDoublet();
				}else if(secondary) {
					moves_x = me.getSceneX()-X;
					moves_y = me.getSceneY()-Y-25;
				}
				
			}
		};	
		EventHandler<MouseEvent> move_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(adding_CanvasComponent != null) {
					
				}
				
				if(adding_WireDoublet != null) {
					int new_pressed_x = (int) (me.getSceneX()-X+getXTranslate());
					int new_pressed_y = (int) (me.getSceneY()-Y-25+getYTranslate());
					try {
						adding_WireDoublet = getWires(pressed_x, pressed_y, new_pressed_x, new_pressed_y);
						add(adding_WireDoublet.getHorizontalWire());
						add(adding_WireDoublet.getVerticalWire());
					}catch (Exception e) {
						adding_WireDoublet = null;
					}
					
				}
			}
		};
		EventHandler<MouseEvent> released_Mouse_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(adding) {
					try {
						short id = used[roundToNextDot((int) (me.getSceneX()-X+getXTranslate()))/cross_distance][ roundToNextDot((int) (me.getSceneY()-Y-25+getYTranslate()))/cross_distance];
						
						int new_pressed_x = (int) (me.getSceneX()-X+getXTranslate());
						int new_pressed_y = (int) (me.getSceneY()-Y-25+getYTranslate());
						
						if(pressed_x == new_pressed_x && pressed_y == new_pressed_y) {
							getCanvasComponent(id).setFocus(true);
							getCanvasComponent(id).printComponents();
							last_focused_component = id;
						}else {
							if(adding_WireDoublet != null) {
								try{
								
									add(adding_WireDoublet.getHorizontalWire());
								}catch (Exception e) {}
								try {
									add(adding_WireDoublet.getVerticalWire());
								}catch (Exception e) {}
								adding_WireDoublet = null;
							}
							
							if(adding_CanvasComponent != null) {
								
								adding_CanvasComponent = null;
							}
						}
						} catch (Exception e) {}
					adding = false;
					primary = false;
				}else if(primary) {
					//setCursor(javafx.scene.Cursor.OPEN_HAND);
				}
			}
		};
		
		addEventFilter(MouseEvent.MOUSE_DRAGGED, dragging_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_PRESSED, press_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_RELEASED, released_Mouse_Handler);
		addEventFilter(MouseEvent.MOUSE_MOVED, move_Event_Handler);
		
		EventHandler<ScrollEvent> zoom_Event_Handler = new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent se) {
				addZTranslate(se.getDeltaY());
			}
		};
		
		addEventFilter(ScrollEvent.SCROLL, zoom_Event_Handler);
		
		
	}
	
	public void add(FunctionalCanvasComponent component) {
		short ID = generateRandomFunctionalComponent();
		try {
			for(Dot d : component.inputs) {
				add(d);
			}
			for(Dot d : component.outputs) {
				add(d);
			}
			//for()
			
			component.setLogicSubScene(this);
			functional_canvas_component.put(ID, component);
			root.getChildren().add(component.getImageView());
		}catch(OcupationExeption oe) {
			
		}
	}
	
	public void add(SingleCanvasComponent component) throws OcupationExeption {
		if(component != null) {
			
		
			short ID = generateRandomSingleComponentID();		
			short loc_ID;
			
			System.out.println(ID);
			
			if(component.rotation == CanvasComponent.HORIZONTAL) {
				for(int x = component.getXPoint()+1; x < component.getXPoint()+component.getWidthPoint(); x++) {
					loc_ID = used[x][component.getYPoint()];
					if(loc_ID == 0) {
						used[x][component.getYPoint()] = ID;
					}else if(loc_ID == 1){
						throw new OcupationExeption();
					}else {
						if(getCanvasComponent(loc_ID).checkEnd(x, component.getYPoint())) {
							getCanvasComponent(loc_ID).addComponent(ID);
							component.addComponent(loc_ID);
						}
					}
				}
				
				loc_ID = used[component.getXPoint()][component.getYPoint()];
				if(loc_ID==0) {
					used[component.getXPoint()][component.getYPoint()] = ID;
				}else if(loc_ID==1) {
					throw new OcupationExeption();
				}else {
					getCanvasComponent(loc_ID).addComponent(ID);
					component.addComponent(loc_ID);
				}
				
				//Sets End/Start Point
				loc_ID = used[component.getXPoint()+component.getHeightPoint()][component.getYPoint()];
				if(loc_ID==0) {
					used[component.getXPoint()+component.getHeightPoint()][component.getYPoint()] = ID;
				}else if(loc_ID==1) {
					throw new OcupationExeption();
				}else {
					getCanvasComponent(loc_ID).addComponent(ID);
					component.addComponent(loc_ID);
				}
			}else {
				for(int y = component.getYPoint()+1; y < component.getYPoint()+component.getHeightPoint(); y++) {
					loc_ID = used[component.getXPoint()][y];
					if(loc_ID == 0) {
						used[component.getXPoint()][y] = ID;
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
				loc_ID = used[component.getXPoint()][component.getYPoint()];
				if(loc_ID==0) {
					used[component.getXPoint()][component.getYPoint()] = ID;
				}else if(loc_ID==1) {
					throw new OcupationExeption();
				}else {
					getCanvasComponent(loc_ID).addComponent(ID);
					component.addComponent(loc_ID);
				}
				
				loc_ID = used[component.getXPoint()][component.getYPoint()+component.getHeightPoint()];
				if(loc_ID==0) {
					used[component.getXPoint()][component.getYPoint()+component.getHeightPoint()] = ID;
				}else if(loc_ID==1) {
					throw new OcupationExeption();
				}else {
					getCanvasComponent(loc_ID).addComponent(ID);
					component.addComponent(loc_ID);
				}
				
				
			}
			component.setLogicSubScene(this);
			component.setId(ID);
			root.getChildren().add(component.getImageView());
			
			single_canvas_components.put(ID, component); 
			component.printComponents();
		}
	}
	
	public short generateRandomSingleComponentID() {
		short ID = (short) random.nextInt(1 << 15);
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
		short ID = (short) random.nextInt(1 << 15);
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
		return new LogicSubScene(new Group(),start_width, start_height, multiplier);
	}
	
	protected WritableImage generateBackgroundImage() {
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
		int overflow = coordinat% cross_distance;
		if(overflow<=cross_distance/2) {
			return coordinat-overflow;
		}else {
			return coordinat+cross_distance-overflow;
		}
	}
	
	public WireDoublet getWires(int start_X, int start_Y, int end_X, int end_Y) throws IllegalArgumentException{
		int round_start_x = roundToNextDot(start_X);
		int round_start_y = roundToNextDot(start_Y);
		int round_end_x = getNearesDot(end_X);
		int round_end_y = getNearesDot(end_Y);
		
		WireDoublet doublet = new WireDoublet();

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
		}
		if(round_start_y!=round_end_y) {
			Wire wire_vertical = new Wire(Math.abs(round_start_y-round_end_y)+wire_height*3/4);
			wire_vertical.setRotation(CanvasComponent.VERTICAL);
			if(round_start_y>=round_end_y) {
				wire_vertical.setY(round_end_y-wire_height/2);
			}else {
				wire_vertical.setY(round_start_y-wire_height/2);
			}
			wire_vertical.setX(round_end_x-wire_height/2);
			
			wire_vertical.setState(CanvasComponent.UNSET);
			
			doublet.setVerticalWire(wire_vertical);
		}
		return doublet;
	}
	
	public SingleCanvasComponent getCanvasComponent(short id) {
		return single_canvas_components.get(id); 
	}
	
	public void addX(int ADD_X) {
		X = getLayoutX()+ADD_X;
		setLayoutX(X);
	}
	public void addY(int ADD_Y) {
		Y = getLayoutY()+ADD_Y;
		setLayoutY(Y);
	}
	
	public void setStandardZoom() {
		camera_position.setZ(multiplier);
	}
	
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
