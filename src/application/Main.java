package application;
	
import java.util.ArrayList;

import canvas.LogicSubScene;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Main extends Application {
	ArrayList<Scene> Scenes = new ArrayList<>();
	ArrayList<Runnable> Runnables = new ArrayList<>();
	Stage MainStage; 
	
	@Override
	public void start(Stage primaryStage) {
		addStartScene();
		addLogismArea(); 
		try {
			MainStage = primaryStage;
			changeScene(1);
			MainStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Menu getThemeChoiceMenu() {
		Menu theme = new Menu("Themes");
		MenuItem dark = new MenuItem("Dark");
		MenuItem bright = new MenuItem("White");
		theme.getItems().add(dark);
		theme.getItems().add(new SeparatorMenuItem());
		theme.getItems().add(bright);
		return theme;
	}
	
	private void addStartScene() {
		MenuBar bar = new MenuBar();
		bar.getMenus().add(getThemeChoiceMenu());
		VBox vbox = new VBox(bar);
		Scene scene = new Scene(vbox);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Runnables.add(new Runnable() {
			@Override
			public void run() {
				MainStage.setMaximized(false);
			}});
		Scenes.add(scene);
	}
	
	private void addLogismArea() {
		MenuBar bar = new MenuBar();
		bar.getMenus().add(getThemeChoiceMenu());
		VBox vbox = new VBox(bar);
		Group root = new Group();
		SubScene MainScene = new SubScene(root,1000,500);
		MainScene.heightProperty().bind(vbox.heightProperty());
		MainScene.widthProperty().bind(vbox.widthProperty());
		MainScene.setFill(Color.GRAY);
		
		
		LogicSubScene logicscene = LogicSubScene.init(LogicSubScene.cross_distance*73, LogicSubScene.cross_distance*40, 2); 
		logicscene.setFill(Color.AQUA);
		
		logicscene.addX(50);
		logicscene.addY(25);
		
		//logicscene.addWire(LogicSubScene.cross_distance*40, LogicSubScene.cross_distance*21,LogicSubScene.cross_distance*39, LogicSubScene.cross_distance*20);
		
		//logicscene.addZTranslate(100);
		
		root.getChildren().add(logicscene);
		vbox.getChildren().add(MainScene);
		Scene scene = new Scene(vbox);
		
		Runnables.add(new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		});
		Scenes.add(scene);
	}
	
	private void changeScene(int SceneNumber) {
		MainStage.setScene(Scenes.get(SceneNumber));
		Runnables.get(SceneNumber).run();
	}
}
