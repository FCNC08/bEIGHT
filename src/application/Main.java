package application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import canvas.LogicSubSceneContainer;
import education.EducationSubSceneContainer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import net.lingala.zip4j.ZipFile;
import util.IllegalInputOutputException;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Main extends Application {

	public static Random random = new Random();
	public static Main main;

	// ArrayList with the different Scenes and Runnables to change Scenes with run
	// for example Maximization
	Scene[] Scenes = new Scene[3];
	Runnable[] Runnables = new Runnable[3];

	// MainStage from start methode

	Stage MainStage;
 
	@Override
	public void start(@SuppressWarnings("exports") Stage primaryStage) {
		main = this;
		System.out.println("Hallo Welt dies ist ein Test vom Laptop");
		// Adding different Scenes
		addStartScene();
		
		primaryStage.getIcons().add(new Image("Icon.png"));
		primaryStage.setTitle("bEIGHT");
		
		// set Scene and saves Stage
		MainStage = primaryStage;
		
		MainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if(new File("temporary/").exists()) {
					try {
						FileUtils.cleanDirectory(new File("temporary/"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					System.out.println("No temp.");
				}
			}
		});
		changeScene(0);
		MainStage.show();

	}

	public static void main(String[] args) {
		// Adding lines to output
		System.setOut(new PrintStream(System.out) {

			private StackTraceElement getCallSite() {
				for (StackTraceElement e : Thread.currentThread().getStackTrace())
					if (!e.getMethodName().equals("getStackTrace") && !e.getClassName().equals(getClass().getName()))
						return e;
				return null;
			}

			@Override
			public void println(String s) {
				println((Object) s);
			}
			
			@Override
			public void println(boolean b) {
				println((Object)b);
			}
			
			@Override
			public void println(double b) {
				println((Object)b);
			}
			@Override
			public void println(int b) {
				println((Object)b);
			}
			@Override
			public void println(long b) {
				println((Object)b);
			}
			@Override
			public void println(char b) {
				println((Object)b);
			}
			
			@Override
			public void println(Object o) {
				StackTraceElement e = getCallSite();
				String callSite = e == null ? "??" : String.format("%s.%s(%s:%d)", e.getClassName(), e.getMethodName(), e.getFileName(), e.getLineNumber());
				super.println(o + "\t\tat " + callSite);
			}
			
		});
		launch(args);
	}

	// Menu to change Themes
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
		// Size of screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		int width = screen.width;
		int height = screen.height;

		// Adding MenuBar
		MenuBar bar = new MenuBar();
		bar.getMenus().add(getThemeChoiceMenu());
		VBox vbox = new VBox(bar);
		vbox.setMinHeight(height);
		vbox.setMinWidth(width);

		// Adding SubScene
		Group root = new Group();
		SubScene MainScene = new SubScene(root, 1000, 500);
		MainScene.heightProperty().bind(vbox.heightProperty());
		MainScene.widthProperty().bind(vbox.widthProperty());
		MainScene.setFill(Color.GRAY);
		
		Pane logic_area = new Pane();
		Rectangle logic_square = new Rectangle(width/5, width/5, Color.TURQUOISE);
		Text logic_text = new Text("Logic-\nSimulation");
		logic_text.setFont(new Font(50));
		
		logic_text.setX((width/5-logic_text.getBoundsInLocal().getWidth())/2);
		logic_text.setY((width/5-logic_text.getBoundsInLocal().getHeight())/2);
		
		logic_area.getChildren().addAll(logic_square, logic_text);
		
		logic_area.setLayoutX(width/5);
		logic_area.setLayoutY((height-width/5)/2);
		
		EventHandler<MouseEvent> logic_click = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(Scenes[1] == null) {
					addLogicArea();
				}
				changeScene(1);
			}
		};
		logic_area.addEventFilter(MouseEvent.MOUSE_CLICKED, logic_click);
		
		Pane education_area = new Pane();
		Rectangle education_square = new Rectangle(width/5, width/5, Color.TURQUOISE);
		Text education_text = new Text("Education-\nUnit");
		education_text.setFont(new Font(50));
		
		education_text.setX((width/5-education_text.getBoundsInLocal().getWidth())/2);
		education_text.setY((width/5-education_text.getBoundsInLocal().getHeight())/2);
		
		education_area.getChildren().addAll(education_square, education_text);
		
		education_area.setLayoutX((width/5)*3);
		education_area.setLayoutY((height-width/5)/2);
		
		EventHandler<MouseEvent> education_click = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				if(Scenes[2]==null) {
					addEducationArea();
				}
				changeScene(2);
			}
		};
		education_area.addEventFilter(MouseEvent.MOUSE_CLICKED, education_click);
		
		root.getChildren().add(logic_area);
		root.getChildren().add(education_area);
		vbox.getChildren().add(MainScene);

		Scene scene = new Scene(vbox);

		// Adding Runnable to maximize and resize
		Runnables[0] = new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		};
		// Adding Scene
		Scenes[0] = scene;

	}

	// Adding LogicScene
	private void addLogicArea() {
		// Size of screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		int width = screen.width;
		int height = screen.height;

		// System.out.println("width: "+width+" height: " +height);

		// Adding MenuBar
		MenuBar bar = new MenuBar();
		VBox vbox = new VBox(bar);
		vbox.setMinHeight(height);
		vbox.setMinWidth(width);
		Menu returning = new Menu("Return");
		MenuItem returning_item = new MenuItem("Return to Start");
		returning_item.setOnAction(me ->{
			changeScene(0);
		});
		returning.getItems().add(returning_item);
		bar.getMenus().add(returning);		

		// Adding SubScene
		Group root = new Group();
		SubScene MainScene = new SubScene(root, 1000, 500);
		MainScene.heightProperty().bind(vbox.heightProperty());
		MainScene.widthProperty().bind(vbox.widthProperty());
		MainScene.setFill(Color.GRAY);

		LogicSubSceneContainer logic_container = LogicSubSceneContainer.init(width, height);
		
		Menu theme = new Menu("Themes");
		MenuItem dark = new MenuItem("Dark");
		dark.setOnAction(me->{
			logic_container.setColor(LogicSubSceneContainer.BLACK);
		});
		MenuItem bright = new MenuItem("White");
		bright.setOnAction(me->{
			logic_container.setColor(LogicSubSceneContainer.WHITE);
		});
		theme.getItems().add(dark);
		theme.getItems().add(new SeparatorMenuItem());
		theme.getItems().add(bright);
		bar.getMenus().add(theme);
		
		Menu file = new Menu("File");
		MenuItem savingpdf = new MenuItem("Save as PDF");
		savingpdf.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("Save as");
			ExtensionFilter extFilter = new ExtensionFilter(".pdf files (*.pdf)", "*.pdf");
	        fc.getExtensionFilters().add(extFilter);
			File selected_file = fc.showSaveDialog(new Stage());
			if (selected_file != null) {
				logic_container.logic_subscene.SaveAsPDF(selected_file);
			}
		});
		
		MenuItem open = new MenuItem("Open");
		open.setOnAction(me->{
			FileChooser fc = new FileChooser();
			fc.setTitle("Open");
			ExtensionFilter extFilter = new ExtensionFilter(".beight files (*.beight)", "*.beight");
			fc.getExtensionFilters().add(extFilter);
			var selected_file = fc.showOpenDialog(new Stage());
			if(selected_file != null) {
				logic_container.open(selected_file);
			}
			fc = null;
		});
		
		MenuItem save = new MenuItem("Save");
		save.setOnAction(me->{
			logic_container.save();
		});
		MenuItem saveas = new MenuItem("Save as");
		saveas.setOnAction(me->{
			logic_container.saveas();
		});
		
		MenuItem saveverilog = new MenuItem("Save as verilog");
		saveverilog.setOnAction(me->{
			FileChooser fc = new FileChooser();
			fc.setTitle("Save as");
			ExtensionFilter extFilter = new ExtensionFilter("Verilog files (*.v)", "*.v");
			fc.getExtensionFilters().add(extFilter);
			File selected_file = fc.showSaveDialog(new Stage());
			if(selected_file != null) {
				try(FileWriter writer = new FileWriter(selected_file)){
					writer.write(logic_container.logic_subscene.getVerilog());
				}catch(IOException e) {
					e.printStackTrace();
				} catch (IllegalInputOutputException e1) {
					Stage stage = new Stage();
					Group scene_root = new Group();
					Scene scene = new Scene(scene_root);
					Label label = new Label("Not enough Inputs/Outputs.\nPlease add new Inputs/Outputs");
					scene_root.getChildren().add(label);
					stage.setScene(scene);
					stage.show();
				}
			}
		});
		MenuItem savearduino = new MenuItem("Export as arduino");
		savearduino.setOnAction(me->{
			FileChooser fc = new FileChooser();
			fc.setTitle("Export Arduino as");
			ExtensionFilter extFilter = new ExtensionFilter("Arduino-Sketch (*ino)", "*.ino");
			fc.getExtensionFilters().add(extFilter);
			File selected_file = fc.showSaveDialog(new Stage());
			if(selected_file != null ) {
				logic_container.saveArduino(selected_file);
			}
		});
		
		MenuItem uploadarduino = new MenuItem("Upload onto Arduino");
		uploadarduino.setOnAction(me->{
			logic_container.uploadArduino();
			
		});
		
		MenuItem savearduinshield = new MenuItem("Export as beighduinoShield");
		savearduinshield.setOnAction(me->{
			FileChooser fc = new FileChooser();
			fc.setTitle("Export Arduino as");
			ExtensionFilter extFilter = new ExtensionFilter("Arduino-Sketch (*ino)", "*.ino");
			fc.getExtensionFilters().add(extFilter);
			File selected_file = fc.showSaveDialog(new Stage());
			if(selected_file != null ) {
				logic_container.saveArduinoShield(selected_file);
			}
		});
		MenuItem uploadarduinoshield = new MenuItem("Upload onto beighduinoShield");
		uploadarduinoshield.setOnAction(me->{
			logic_container.uploadArduinoShield();
		});
		
		file.getItems().add(savingpdf);
		file.getItems().add(open);
		file.getItems().add(save);
		file.getItems().add(saveas);
		file.getItems().add(saveverilog);
		file.getItems().add(savearduino);
		file.getItems().add(uploadarduino);
		file.getItems().add(savearduinshield);
		file.getItems().add(uploadarduinoshield);
		bar.getMenus().add(file);
		
		Menu setting = new Menu("settings");
		MenuItem gc = new MenuItem("collect garbage");
		gc.setOnAction(e->{
			System.gc();
		});
		setting.getItems().add(gc);
		
		bar.getMenus().add(setting);
		
		Menu translate = new Menu("Translations");
		MenuItem standard = new MenuItem("Standard-Zoom");
		standard.setOnAction(e -> {
			e.consume();
			logic_container.logic_subscene.setStandardZoom();
		});
		translate.getItems().add(standard);

		root.getChildren().add(logic_container);
		vbox.getChildren().add(MainScene);
		
		bar.getMenus().add(translate);

		Scene scene = new Scene(vbox);
		scene.getStylesheets().add("logic_style.css");

		EventHandler<KeyEvent> key_event_handler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				logic_container.triggerKeyEvent(ke);
			}
		};

		scene.addEventFilter(KeyEvent.KEY_PRESSED, key_event_handler);

		// Adding Runnable to maximize and resize
		Runnables[1] = new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		};

		// Adding Scene
		Scenes[1] = scene;

	}
	
	private void addEducationArea() {
		// Size of screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		int width = screen.width;
		int height = screen.height;

		// System.out.println("width: "+width+" height: " +height);

		// Adding MenuBar
		MenuBar bar = new MenuBar();
		bar.getMenus().add(getThemeChoiceMenu());
		VBox vbox = new VBox(bar);
		vbox.setMinHeight(height);
		vbox.setMinWidth(width);
		
		Menu returning = new Menu("Return");
		MenuItem returning_item = new MenuItem("Return to Start");
		returning_item.setOnAction(me ->{
			System.out.println("Test");
			changeScene(0);
		});
		returning.getItems().add(returning_item);
		bar.getMenus().add(returning);

		// Adding SubScene
		Group root = new Group();
		SubScene MainScene = new SubScene(root, 1000, 500);
		MainScene.heightProperty().bind(vbox.heightProperty());
		MainScene.widthProperty().bind(vbox.widthProperty());
		MainScene.setFill(Color.GRAY);
		EducationSubSceneContainer subscene = new EducationSubSceneContainer(width, height);
		subscene.addLesson(new ZipFile("testfiles/logikgatter.lct"));
		subscene.addLesson(new ZipFile("testfiles/2bitadder.lct"));
		subscene.addLesson(new ZipFile("testfiles/2x2multiplier.lct"));
		root.getChildren().add(subscene);
		
		vbox.getChildren().add(MainScene);

		Scene scene = new Scene(vbox);
		//scene.getStylesheets().add("education_style.css");

		// Adding Runnable to maximize and resize
		Runnables[2] = new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		};
		// Adding Scene
		Scenes[2] = scene;
	}

	// Changing Scene method
	public void changeScene(int SceneNumber) {
		// Setting Scene to Stage
		MainStage.setScene(Scenes[SceneNumber]);

		// Runs Runnable
		Runnables[SceneNumber].run();
	}
}