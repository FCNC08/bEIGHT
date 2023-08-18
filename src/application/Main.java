package application;
	
import java.awt.FileDialog;
import java.awt.Frame;
import java.util.ArrayList;

import canvas.ComponentChooser;
import canvas.ComponentGroup;
import canvas.ComponentGroupings;
import canvas.LogicSubScene;
import canvas.LogicSubSceneContainer;
import canvas.components.LogicComponent;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import canvas.components.StandardComponents.LogicComponents.ORGate;
import javafx.application.Application;
import javafx.stage.Stage;
import util.OcupationExeption;
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
		System.out.println("Hallo Welt dies ist ein Test vom Laptop");
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
		LogicSubScene logicscene = LogicSubScene.init(LogicSubScene.cross_distance*60, LogicSubScene.cross_distance*35, 4); 
		logicscene.setFill(Color.WHITE);
		
		logicscene.addX(50);
		logicscene.addY(25);
		
		ComponentGroupings grouping = new ComponentGroupings();
		ComponentGroup group = new ComponentGroup();
		group.add(ANDGate.getANDGATE(LogicComponent.SIZE_MIDDLE ,2, 1));
		group.add(ORGate.getORGATE(LogicComponent.SIZE_MIDDLE ,2, 1));
		group.add(ANDGate.getANDGATE(LogicComponent.SIZE_MIDDLE ,2, 1));
		group.add(ORGate.getORGATE(LogicComponent.SIZE_MIDDLE ,2, 1));
		ComponentGroup group_1 = new ComponentGroup();
		
		grouping.add(group);
		grouping.add(group_1);
		
		ComponentChooser chooser = new ComponentChooser(logicscene, new Group(),400, LogicSubScene.cross_distance*35, grouping);
		chooser.setLayoutX(LogicSubScene.cross_distance*60+50);
		chooser.setLayoutY(25);
		
		ANDGate and = ANDGate.getANDGATE(LogicComponent.SIZE_BIG, 2, 1); 
		
		and.setX(800);
		and.setY(800);
		
		//LogicSubSceneContainer logicscene = new LogicSubSceneContainer(LogicSubScene.cross_distance*70, LogicSubScene.cross_distance*35, 4);
		
		root.getChildren().add(logicscene);
		root.getChildren().add(chooser);
		vbox.getChildren().add(MainScene);
		try {
			logicscene.add(and);
		} catch (OcupationExeption e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Menu translate = new Menu("Translations");
		MenuItem standard = new MenuItem("Standard-Zoom");
		standard.setOnAction(e -> {
			e.consume();
			logicscene.setStandardZoom();
		});
		translate.getItems().add(standard);
		bar.getMenus().add(translate);
		
		Menu file = new Menu("File");
		MenuItem savingpdf = new MenuItem("Save as PDF");
		savingpdf.setOnAction(e ->{
			e.consume();
			FileDialog fd = new FileDialog(new Frame(), "Save as PDF");
			fd.setMode(FileDialog.SAVE);
			fd.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".pdf"));
			fd.setVisible(true);
			
			String directory = fd.getDirectory();
			String filename = fd.getFile();
			if(directory!=null && filename != null) {
				String filepath = directory+filename;
				logicscene.SaveAsPDF(filepath);
			}
		});
		file.getItems().add(savingpdf);
		bar.getMenus().add(file);
		
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
