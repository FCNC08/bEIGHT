package education;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class EducationLesson extends Pane{
	public EducationLesson(double width, double height, ZipFile file, EducationSubScene scene) {
		super();
		JSONObject jsonobjekt = null;
		try {
			if(file.isEncrypted()) {
				throw new IllegalArgumentException();
			}
			InputStream inputStream = file.getInputStream(file.getFileHeader("content.json"));
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead;
			try {
				while((bytesRead=inputStream.read(buffer))!= -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String jsonString = outputStream.toString(StandardCharsets.UTF_8);
			jsonobjekt = new JSONObject(jsonString);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String type = jsonobjekt.getString("type");
		double y = 50;
		for(char t : type.toCharArray()) {
			switch(t) {
			case('h'):{
				Text headline = new Text(jsonobjekt.getString("headline"));
				headline.setFont(new Font(50));
				headline.setX((width-headline.getBoundsInParent().getWidth())/2);
				headline.setY(y);
				y+=headline.getBoundsInParent().getHeight();
				getChildren().add(headline);
				break;
			}
			case('i'):{
				Image image = null;
				try {
					image = new Image(file.getInputStream(file.getFileHeader(jsonobjekt.getString("image"))));
				} catch (JSONException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ImageView view = new ImageView(image);
				view.setFitHeight(height*0.7);
				view.setX((width-image.getWidth())/2);
				view.setY(y);
				y+=view.getFitHeight()+50;
				getChildren().add(view);
				break;
			}
			case('t'):{
				Text text = new Text(jsonobjekt.getString("text"));
				text.setFont(new Font(30));
				text.setX((width-text.getBoundsInParent().getWidth())/2);
				text.setY(y);
				y+=text.getBoundsInParent().getHeight()+50;
				getChildren().add(text);
				break;
			}
			}
			Button button_back = new Button("Back");
			button_back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					scene.setPrev();
				}
			});
			button_back.setLayoutY(height-button_back.getHeight()-150);
			button_back.setLayoutX(25);
			getChildren().add(button_back);
			
			Button button_next = new Button("Next");
			button_next.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					scene.setNext();
				}
			});
			button_next.setLayoutY(height-button_next.getHeight()-150);
			button_next.setLayoutX(width-button_next.getWidth()-50);
			getChildren().add(button_next);
		}
		
	}
}
