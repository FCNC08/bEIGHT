package application;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;

import canvas.LogicSubSceneContainer;
import canvas.components.LogicComponent;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import util.OcupationExeption;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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

	// ArrayList with the different Scenes and Runnables to change Scenes with run
	// for example Maximization
	ArrayList<Scene> Scenes = new ArrayList<>();
	ArrayList<Runnable> Runnables = new ArrayList<>();

	// MainStage from start methode

	Stage MainStage;

	@Override
	public void start(Stage primaryStage) {
		System.out.println("Hallo Welt dies ist ein Test vom Laptop");
		// Adding different Scenes
		addStartScene();
		addLogicArea();
		addEducationArea();
		// set Scene and saves Stage
		MainStage = primaryStage;
		changeScene(0);
		MainStage.show();

	}

	public static void main(String[] args) {
		// Adding lines to output
		System.setOut(new java.io.PrintStream(System.out) {

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
				changeScene(2);
			}
		};
		education_area.addEventFilter(MouseEvent.MOUSE_CLICKED, education_click);
		
		root.getChildren().add(logic_area);
		root.getChildren().add(education_area);
		vbox.getChildren().add(MainScene);

		Scene scene = new Scene(vbox);

		// Adding Runnable to maximize and resize
		Runnables.add(new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		});
		// Adding Scene
		Scenes.add(scene);

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
		bar.getMenus().add(getThemeChoiceMenu());
		VBox vbox = new VBox(bar);
		vbox.setMinHeight(height);
		vbox.setMinWidth(width);
		Menu returning = new Menu("Return");
		MenuItem returning_item = new MenuItem("Return to Start");
		returning_item.setOnAction(me ->{
			System.out.println("TEst");
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

		ANDGate and = null;
		try {
			and = ANDGate.getANDGATE(LogicComponent.SIZE_BIG, 2);
		} catch (IllegalArgumentException e2) {
		}

		and.setX(1300);
		and.setY(1000);

		// LogicSubSceneContainer

		root.getChildren().add(logic_container);
		vbox.getChildren().add(MainScene);
		try {
			logic_container.logic_subscene.add(and);
		} catch (OcupationExeption e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Menu translate = new Menu("Translations");
		MenuItem standard = new MenuItem("Standard-Zoom");
		standard.setOnAction(e -> {
			e.consume();
			logic_container.logic_subscene.setStandardZoom();
		});
		translate.getItems().add(standard);
		bar.getMenus().add(translate);

		Menu file = new Menu("File");
		MenuItem savingpdf = new MenuItem("Save as PDF");
		savingpdf.setOnAction(e -> {
			e.consume();
			FileDialog fd = new FileDialog(new Frame(), "Save as PDF");
			fd.setMode(FileDialog.SAVE);
			fd.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".pdf"));
			fd.setVisible(true);

			String directory = fd.getDirectory();
			String filename = fd.getFile();
			if (directory != null && filename != null) {
				String filepath = directory + filename;
				logic_container.logic_subscene.SaveAsPDF(filepath);
			}
		});
		file.getItems().add(savingpdf);
		bar.getMenus().add(file);

		Scene scene = new Scene(vbox);

		EventHandler<KeyEvent> key_event_handler = new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				logic_container.triggerKeyEvent(ke);
			}
		};

		scene.addEventFilter(KeyEvent.KEY_PRESSED, key_event_handler);

		// Adding Runnable to maximize and resize
		Runnables.add(new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		});

		// Adding Scene
		Scenes.add(scene);

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
			System.out.println("TEst");
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
		
		
		vbox.getChildren().add(MainScene);

		Scene scene = new Scene(vbox);

		// Adding Runnable to maximize and resize
		Runnables.add(new Runnable() {
			public void run() {
				MainStage.setMaximized(true);
				MainStage.setResizable(true);
			}
		});
		// Adding Scene
		Scenes.add(scene);
	}

	// Changing Scene method
	private void changeScene(int SceneNumber) {
		// Setting Scene to Stage
		MainStage.setScene(Scenes.get(SceneNumber));

		// Runs Runnable
		Runnables.get(SceneNumber).run();
	}
}
