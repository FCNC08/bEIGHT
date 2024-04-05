package education;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;

public class EducationLesson extends ScrollPane{
	
	protected VBox vbox = new VBox(20);
	public EducationLesson(double width, double height, ZipFile file, EducationSubScene scene) {
		super();
		vbox.setAlignment(Pos.CENTER);
		setContent(vbox);
		setMaxHeight(height-50);
		setMaxWidth(width);
		JSONObject jsonobjekt = null;
		try {
			if(file.isEncrypted()) {
				throw new IllegalArgumentException();
			}
			InputStream inputStream = file.getInputStream(file.getFileHeader("content.json"));
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead;
			while((bytesRead=inputStream.read(buffer))!= -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			String jsonString = outputStream.toString(StandardCharsets.UTF_8);
			jsonobjekt = new JSONObject(jsonString);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String type = jsonobjekt.getString("type");
		for(char t : type.toCharArray()) {
			switch(t) {
			case('h'):{
				Text headline = new Text(jsonobjekt.getString("headline"));
				headline.setFont(new Font(50));
				vbox.getChildren().add(headline);
				break;
			}
			case('i'):{
				Image image = null;
				try {
					image = new Image(file.getInputStream(file.getFileHeader(jsonobjekt.getString("image"))));
				} catch (JSONException | IOException e) {
					e.printStackTrace();
				}
				ImageView view = new ImageView(image);
				if(image.getHeight()>height*0.5) {
					view.setFitHeight(height*0.5);
					view.setFitWidth(image.getWidth()*(height*0.5/image.getHeight()));
				}else {
					view.setFitHeight(image.getHeight());
				}
				vbox.getChildren().add(view);
				break;
			}
			case('t'):{
				Text text = new Text(jsonobjekt.getString("text"));
				text.setFont(new Font(25));
				vbox.getChildren().add(text);
				break;
			}
			}
			
		}
		HBox button_box = new HBox();
		Button button_back = new Button("Back");
		button_back.setFont(new Font(25));
		button_back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				scene.setPrev();
			}
		});		
		Button button_next = new Button("Next");
		button_next.setFont(new Font(25));
		button_next.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				scene.setNext();
			}
		});
		button_box.getChildren().addAll(button_back, button_next);
		vbox.getChildren().add(button_box);
		vbox.requestLayout();
		scene.root.requestLayout();
	}
}
