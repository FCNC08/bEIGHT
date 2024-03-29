package education;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import application.Main;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class EducationSubScene extends SubScene{

	public static String tempedu = "temporary/education/";
	public static String tempmod = "temporary/modul/";
	public static String tempext = "temporary/external";
	protected Group root;
	protected ArrayList<ScrollPane> order;
	int position = 0;
	public EducationSubScene(double width, double height, ZipFile questions) throws ZipException {
		super(new Group(), width, height);
		this.root = (Group) getRoot();
		JSONObject jsonobject = null;
		try {
			if(questions.isEncrypted()) {
				throw new IllegalArgumentException();
			}
			InputStream inputStream = questions.getInputStream(questions.getFileHeader("settings.json"));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead;
			while((bytesRead = inputStream.read(buffer))!=-1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			String jsonString = outputStream.toString(StandardCharsets.UTF_8);
			jsonobject = new JSONObject(jsonString);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		VBox choosing_root = new VBox(20);
		ScrollPane choosing_area = new ScrollPane(choosing_root);
		String headline_text = jsonobject.getString("name");
		Text headline = new Text(headline_text);
		headline.setFont(new Font(50));
		headline.setX((width-headline.getBoundsInParent().getWidth())/2);
		headline.setY(50);
		String difficulty_text = jsonobject.getString("difficulty");
		Text difficulty = new Text(difficulty_text);
		difficulty.setFont(new Font(25));
		difficulty.setX((width-difficulty.getBoundsInParent().getWidth())/2);
		difficulty.setY(headline.getBoundsInParent().getHeight()*1.2);
		choosing_root.getChildren().add(difficulty);
		order = new ArrayList<>();
		order.add(choosing_area);
		JSONArray modules = jsonobject.getJSONArray("order");
		questions.extractAll(tempedu);
		for(Object object : modules) {
			if(object instanceof JSONObject) {
				JSONObject modul = (JSONObject) object;
				
				switch(modul.getString("type")) {
				case("lesson"):{
					ZipFile temporary_file = new ZipFile(tempedu+modul.getString("filename"));
					order.add(new EducationLesson(width, height,temporary_file, this));
					System.out.println(order.get(order.size()-1));
					temporary_file = null;
					break;
				}
				case("question"):{
					ZipFile temporary_file = new ZipFile(tempedu+modul.getString("filename"));
					order.add(new Question(width, height, temporary_file, this));
					System.out.println(order.get(order.size()-1));
					temporary_file = null;
					break;
				}
				case("test"):{
					ZipFile temporary_file = new ZipFile(tempedu+modul.getString("filename"));
					order.add(new LogicSubSceneTest(width, height, temporary_file, this));
					System.out.println(order.get(order.size()-1));
					temporary_file = null;
					break;
				}
				}
			}
		}
		VBox ending_root = new VBox(20);
		ScrollPane ending = new ScrollPane(ending_root);
		Text finish = new Text("You got it.");
		finish.setFont(new Font(100));
		Button end = new Button("go to StartScene");
		end.setFont(new Font(60));
		end.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setNext();
				Main.main.changeScene(0);
			}
		});
		finish.setX((width-finish.getBoundsInParent().getWidth())/2);
		finish.setY((height-finish.getBoundsInParent().getHeight()-end.getBoundsInParent().getHeight())/2);
		ending_root.getChildren().add(finish);
		end.setLayoutY((height-finish.getBoundsInParent().getHeight()-end.getBoundsInParent().getHeight())/2+end.getBoundsInParent().getHeight());
		end.setLayoutX((width-end.getBoundsInParent().getWidth())/2);
		ending_root.getChildren().add(end);
		order.add(ending);
		File tempfile = new File(tempedu);
		if(tempfile.isDirectory()&&tempfile.exists()) {
			File[] files = tempfile.listFiles();
			if (files != null && files.length > 0) {
                // Iterate through each file and delete it
                for (File file2 : files) {
                    file2.delete();
                }
            }
		}
		if(order.size()<=0) {
			Text error = new Text("The choosen file doesn't contain any content");
			error.setFont(new Font(20));
			error.setFill(Color.RED);
			error.setX((width-error.getBoundsInParent().getWidth())/2);
			error.setY((height-error.getBoundsInParent().getHeight())/2);
			choosing_root.getChildren().add(error);
		}else {
			Button starting_button = new Button("Start");
			starting_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					root.getChildren().clear();
					position = 1;
					root.getChildren().add(order.get(position));
				}
			});
			starting_button.setFont(new Font(40));
			starting_button.setMinWidth(width*0.3);
			starting_button.setMinHeight(height*0.2);
			starting_button.setLayoutX((width-starting_button.getBoundsInParent().getWidth())/2);
			starting_button.setLayoutY((height-starting_button.getBoundsInParent().getHeight())/2);
			choosing_root.getChildren().add(starting_button);
			
		}
		root.getChildren().add(choosing_area);
	}
	public void setNext() {
		position++;
		if(position>=order.size()) {
			position = 0;
		}
		root.getChildren().clear();
		root.getChildren().add(order.get(position));
	}
	public void setPrev() {
		position--;
		if(position<0) {
			position = order.size()-1;
		}
		root.getChildren().clear();
		root.getChildren().add(order.get(position));
	}
	public void setPane(int number) {
		position = number;
		if(position<0) {
			position = 0;
		}else if(position>=order.size()) {
			position = order.size()-1;
		}
		root.getChildren().clear();
		root.getChildren().add(order.get(position));
	}

}
