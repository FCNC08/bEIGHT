package canvas;

import java.util.HashMap;
import java.util.Random;

import canvas.components.CanvasComponent;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.SingleCanvasComponent;
import canvas.components.StandardComponents.WireDoublet;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;

public class LogicSubSceneNew extends Canvas{
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
	
	public LogicSubSceneNew(int StartWidth, int StartHeight, int multiplier) {
		super(StartWidth, StartHeight);
		this.Start_Width = StartWidth;
		this.Start_Height = StartHeight;
		this.multiplier = multiplier;
		
		this.max_zoom = (int) (StartWidth*-0.5*multiplier+cross_distance);
		
		int NewWidth = StartWidth*multiplier;
		int NewHeight = StartHeight*multiplier;
		
		if(NewWidth%cross_distance != 0 ||NewHeight%cross_distance !=0) {
			throw new IllegalArgumentException("The size have to fit with the distance between the crosses");
		}
		
		this.width = NewWidth;
		this.height = NewHeight;
		
		this.used = new short[width/cross_distance][height/cross_distance];
		
		WritableImage Test_Background = generateBackgroundImage();
		
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
}
