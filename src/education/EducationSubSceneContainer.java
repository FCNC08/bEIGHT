package education;

import java.util.ArrayList;

import org.json.JSONObject;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class EducationSubSceneContainer extends SubScene{
	private double width;
	private double height;
	
	private Group root;
	private FlowPane fpane;
	private ArrayList<SubScene> sections = new ArrayList<SubScene>();
	
	
	public EducationSubSceneContainer(double width, double height) {
		super(new Group(), width, height);
		this.root = (Group) getRoot();
		this.width = width;
		this.height = height;
		
		Group main_scene_root = new Group();
		SubScene main_scene = new SubScene(main_scene_root, width, height);
		
		fpane = new FlowPane();
		fpane.setPrefWrapLength(width*0.9);
		fpane.getStyleClass().add("education-icon-container");
		
		main_scene_root.getChildren().addAll(fpane);
		sections.add(main_scene);
		root.getChildren().add(main_scene);
	}
	
	public void addLesson(ZipFile file, JSONObject settings) {
		try {
			EducationSubScene scene = new EducationSubScene(width, height, file, settings, this);
			
			final int number = sections.size();
			sections.add(scene);
			EventHandler<MouseEvent> open_event_handler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					root.getChildren().clear();
					root.getChildren().add(sections.get(number));
				}
			};
			scene.getIcon().addEventHandler(MouseEvent.MOUSE_CLICKED, open_event_handler);
			//editor.getIcon().setLayoutX(hbox.getBoundsInLocal().getWidth());;
			fpane.getChildren().add(scene.getIcon());
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}
	
	public void gotoStart() {
		root.getChildren().clear();
		root.getChildren().add(sections.get(0));
	}

}
