package education;

import java.io.ByteArrayOutputStream;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;

public class Question extends Pane{
	private int correctanswer;
	private int jumpto;
	EducationSubScene subscene;
	public Question(double width, double height, ZipFile file, EducationSubScene scene) {
		super();
		subscene = scene;
		JSONObject jsonobject = null;
		try {
			if(file.isEncrypted()) {
				throw new IllegalArgumentException();
			}
			InputStream inputStream = file.getInputStream(file.getFileHeader("question.json"));
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead;
			while((bytesRead = inputStream.read(buffer))!= -1) {
				outputStream.write(buffer);
			}
			String jsonString = outputStream.toString(StandardCharsets.UTF_8);
			jsonobject = new JSONObject(jsonString);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		String type = jsonobject.getString("type");
		double y = 50;
		
		for(char t : type.toCharArray()) {
			switch(t) {
			case('h'):{
				Text headline = new Text(jsonobject.getString("headline"));
				headline.setFont(new Font(40));
				headline.setX((width-headline.getBoundsInParent().getWidth())/2);
				headline.setY(y);
				y+=headline.getBoundsInParent().getHeight();
				getChildren().add(headline);
				break;
			}
			case('i'):{
				Image image = null;
				try {
					image = new Image(file.getInputStream(file.getFileHeader(jsonobject.getString("image"))));
				}catch(JSONException | IOException e) {
					e.printStackTrace();
				}
				ImageView view = new ImageView(image);
				if(image.getHeight()>height*0.5) {
					view.setFitHeight(height*0.5);
					view.setFitWidth(image.getWidth()*(height*0.5/image.getHeight()));
				}else {
					view.setFitHeight(image.getHeight());
					view.setFitWidth(image.getWidth());
				}
				view.setX((width-view.getFitWidth())/2);
				view.setY(y);
				y+=view.getFitHeight()+50;
				getChildren().add(view);
				break;
			}
			case('q'):{
				Text text = new Text(jsonobject.getString("question"));
				text.setFont(new Font(25));
				text.setX((width-text.getBoundsInParent().getWidth())/2);
				text.setY(y);
				y+=text.getBoundsInParent().getHeight()+50;
				getChildren().add(text);
				break;
			}
			case('o'):{
				VBox box = new VBox();
				box.setMaxWidth(width);
				correctanswer = jsonobject.getInt("correctanswer");
				jumpto = jsonobject.getInt("jumpto");
				int number = 0;
				for(Object c : jsonobject.getJSONArray("options")) {
					if(c instanceof String) {
						if(number>0) {
							Rectangle space = new Rectangle(50, 10);
							space.setFill(Color.TRANSPARENT);
							box.getChildren().add(space);
						}
						String option = (String) c;
						Text text = new Text(option);
						text.setFont(new Font(25));
						final int nummer = number;
						text.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								submitAnswer(nummer);
							}
						});
						number++;
						box.getChildren().add(text);
					}
				}
				box.setLayoutY(y);
				y+=box.getHeight()+50;
				getChildren().add(box);
				break;
			}
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
	}
	
	public void submitAnswer(int answer) {
		if(answer == correctanswer) {
			subscene.setNext();
		}else {
			subscene.setPane(jumpto);
		}
	}
}
