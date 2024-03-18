package canvas;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import canvas.components.FunctionalCanvasComponent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import util.OcupationExeption;

public class LogicSubSceneManager extends SubScene{
	private SubScene chooser;
	private ArrayList<LogicSubScene> subscenes = new ArrayList<>();
	public LogicSubScene actual_sub_scene;
	private Group root;
	private int width;
	@SuppressWarnings("unused")
	private int height;
	@SuppressWarnings("unused")
	private int multiplier;
	private int X = 0;
	private int Y = 0;
	public LogicSubSceneManager(Group root, int width, int height, int multiplier) {
		super(root, width, height);
		
		this.root = root;
		this.width = width;
		this.height = height;
		this.multiplier = multiplier;
		
		LogicSubScene start_scene = LogicSubScene.init(LogicSubScene.getNearesDot((int) (width * 0.9)), LogicSubScene.getNearesDot(height), multiplier);
		start_scene.setName("Start bEIGHT");
		actual_sub_scene = start_scene;
		Group chooser_root = new Group();
		chooser = new SubScene(chooser_root, width-start_scene.width, height);
		chooser.setFill(Color.BLACK);
		chooser.setLayoutX(0);
		Label label = new Label("+Add new bEIGHT");
		label.setFont(new Font(height*0.05));
		root.getChildren().add(chooser);
		addLogicSubScene(start_scene);
		EventHandler<MouseEvent> click = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("test");
			}
		};
		addEventFilter(MouseEvent.MOUSE_CLICKED, click);
	}
	public LogicSubSceneManager(File file, Group root, int width, int height) {
		super(root, width, height);
		
		this.root = root;
		this.width = width;
		this.height = height;
		
		LogicSubScene start_scene = LogicSubScene.init(file, LogicSubScene.getNearesDot((int) (width * 0.9)), LogicSubScene.getNearesDot(height));
		start_scene.setName("Start bEIGHT");
		actual_sub_scene = start_scene;
		Group chooser_root = new Group();
		chooser = new SubScene(chooser_root, width-start_scene.width, height);
		Label label = new Label("+Add new bEIGHT");
		label.setFont(new Font(height*0.05));
		root.getChildren().add(chooser);
		addLogicSubScene(start_scene);
	}
	
	public void addLogicSubScene(LogicSubScene scene) {
		scene.addX((int) (width*0.1));
		subscenes.add(scene);
		root.getChildren().add(scene);
	}
	
	public void removeLogicSubScene(LogicSubScene scene) {
		root.getChildren().remove(scene);
		subscenes.remove(scene);
	}
	
	public static LogicSubSceneManager init(int width, int height, int multiplier) {
		return new LogicSubSceneManager(new Group(), width, height, multiplier);
	}
	
	public static LogicSubSceneManager init(File file, int width, int height) {
		return new LogicSubSceneManager(file, new Group(), width, height);
	}

	public void addX(int x) {
		this.X=x;
	}
	public void addY(int y) {
		this.Y=y;
	}
	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	public double getXTranslate() {
		return actual_sub_scene.getXTranslate();
	}
	public double getYTranslate() {
		return actual_sub_scene.getYTranslate();
	}
	public void add(FunctionalCanvasComponent component) throws OcupationExeption {
		actual_sub_scene.add(component);
	}
	public JSONObject getJSON() {
		return actual_sub_scene.getJSON();
	}
	public void triggerKeyEvent(KeyEvent ke) {
		
	}

}
