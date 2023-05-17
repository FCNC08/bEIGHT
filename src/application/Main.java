package application;
	
import java.util.ArrayList;

import canvas.LogicSubScene;
import canvas.LogicSubSceneContainer;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	//ArrayList with the different Scenes and Runnables to change Scenes with run for example Maximization
	ArrayList<Scene> Scenes = new ArrayList<>();
	ArrayList<Runnable> Runnables = new ArrayList<>();
	
	//MainStage from start methode
	
	Stage MainStage; 
	
	@Override
	public void start(Stage primaryStage) {
		//Adding different Scenes
		addStartScene();
		addLogismArea(); 
		
		//set Scene and saves Stage
		MainStage = primaryStage;
		changeScene(1);
		MainStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	//Menu to change Themes
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
		//Adding MenuBar and Adding ThemeChooser
		MenuBar bar = new MenuBar();
		bar.getMenus().add(getThemeChoiceMenu());
		
		//VBox to add with MenuBar
		VBox vbox = new VBox(bar);
		Scene scene = new Scene(vbox);
		
		//Adding Runnable to release maximization 
		Runnables.add(new Runnable() {
			@Override
			public void run() {
				MainStage.setMaximized(false);
			}});
		
		//Adding Scene
		Scenes.add(scene);
	}
	
	//Adding LogicScene
	private void addLogismArea() {
		//Adding MenuBar
		MenuBar bar = new MenuBar();
		bar.getMenus().add(getThemeChoiceMenu());
		VBox vbox = new VBox(bar);
		
		//Adding SubScene
		Group root = new Group();
		SubScene MainScene = new SubScene(root,1000,500);
		MainScene.heightProperty().bind(vbox.heightProperty());
		MainScene.widthProperty().bind(vbox.widthProperty());
		MainScene.setFill(Color.GRAY);
		
		//Adding LogicScene
		LogicSubScene logicscene = LogicSubScene.init(LogicSubScene.cross_distance*70, LogicSubScene.cross_distance*35, 4); 
		logicscene.setFill(Color.AQUA);
		
		logicscene.addX(50);
		logicscene.addY(25);
		
		//logicscene.add(ANDGate.getANDGATE(2, 2, new int[2], new int[2], new int[2], new int[2]));
		
		/*ImageView and = new ImageView(ANDGate.LogicComponent_Image);
		and.setLayoutX(1000.0);
		and.setLayoutY(500);
		and.setFitWidth(75);
		and.setFitHeight(75);*/
		
		int[] test = {10,10};
		ANDGate gate = new ANDGate(750, 750, 2, 2, test, test, test, test);
		
		
		//LogicSubSceneContainer logicscene = new LogicSubSceneContainer(LogicSubScene.cross_distance*70, LogicSubScene.cross_distance*35, 4);
		
		root.getChildren().add(logicscene);
		vbox.getChildren().add(MainScene);
		//root.getChildren().add(and);
		//logicscene.add(gate);
		root.getChildren().add(gate.getImageView());
		
		Menu translate = new Menu("Translations");
		MenuItem standard = new MenuItem("Standard-Zoom");
		standard.setOnAction(e -> {
			e.consume();
			logicscene.setStandardZoom();
		});
		
		translate.getItems().add(standard);
		bar.getMenus().add(translate);
		
		Scene scene = new Scene(vbox);
		
		//Adding Runnable to maximize and resize
		Runnables.add(new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		});
		
		//Adding Scene
		Scenes.add(scene);
	}
	
	//Changing Scene method
	private void changeScene(int SceneNumber) {
		//Setting Scene to Stage
		MainStage.setScene(Scenes.get(SceneNumber));
		
		//Runs Runnable
		Runnables.get(SceneNumber).run();
	}
}
