package education;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import application.Main;
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
	
	private JSONObject settings;
	private JSONArray lections;
	
	public String username;
	
	private Group root;
	private FlowPane fpane;
	private ArrayList<SubScene> sections = new ArrayList<SubScene>();
	
	
	public EducationSubSceneContainer(double width, double height, JSONObject settings) {
		super(new Group(), width, height);
		this.root = (Group) getRoot();
		this.width = width;
		this.height = height;
		this.settings = settings;
		
		Group main_scene_root = new Group();
		SubScene main_scene = new SubScene(main_scene_root, width, height);
		
		fpane = new FlowPane();
		fpane.setPrefWrapLength(width*0.9);
		fpane.getStyleClass().add("education-icon-container");
		
		main_scene_root.getChildren().addAll(fpane);
		sections.add(main_scene);
		root.getChildren().add(main_scene);
		
		username = settings.getString("username");
		
		//Adding all lct Files
		lections = settings.getJSONArray("lections");
	    if (lections != null) {
	        for (Object l : lections) {
	        	if(l instanceof JSONObject) {
	        		JSONObject o = (JSONObject) l;
	        		addLesson(new ZipFile(new File(Main.education_dir + o.getString("name"))), o);
	        	}
	        }
	    }
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
	
	public void addFiles(List<File> files) {
	    File eduDir = new File(Main.education_dir);
	    new File(eduDir, "").mkdirs();
	    
	    //Copy every file to the 
	    for (File f : files) {
	        File target = new File(eduDir, f.getName());
	        try {
				Files.copy(f.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		        JSONObject lection = new JSONObject();
		        lection.put("name", f.getName());
		        lection.put("completed", false);
		        lections.put(lection);
		        addLesson(new ZipFile(target), lection);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public void gotoStart() {
		root.getChildren().clear();
		root.getChildren().add(sections.get(0));
	}
	
	public void saveSettings() {
        try (FileWriter fw = new FileWriter(new File(Main.education_settings_file))) {
            fw.write(settings.toString(2));
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

}
