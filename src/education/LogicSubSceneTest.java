package education;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

import canvas.ComponentGroup;
import canvas.ComponentGroupings;
import canvas.LogicSubSceneContainer;
import canvas.components.LogicComponent;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import canvas.components.StandardComponents.LogicComponents.NANDGate;
import canvas.components.StandardComponents.LogicComponents.NORGate;
import canvas.components.StandardComponents.LogicComponents.NOTGate;
import canvas.components.StandardComponents.LogicComponents.ORGate;
import canvas.components.StandardComponents.LogicComponents.XNORGate;
import canvas.components.StandardComponents.LogicComponents.XORGate;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class LogicSubSceneTest extends Pane{
	public LogicSubSceneTest(double width, double height, ZipFile file, EducationSubScene scene) throws ZipException {
		super();
		JSONObject jsonobject = null;
		final ComponentGroupings groupings;
		LogicSubSceneContainer container;
		try {
			if(file.isEncrypted()) {
				throw new IllegalArgumentException();
			}
			InputStream inputstream = file.getInputStream(file.getFileHeader("test.json"));
			
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[4096];
			int byteRead;
			while((byteRead = inputstream.read(buffer))!= -1) {
				outputstream.write(buffer);
			}
			String jsonString = outputstream.toString(StandardCharsets.UTF_8);
			jsonobject = new JSONObject(jsonString);
		}catch(IOException e) {
			e.printStackTrace();
		}
		String headlineString = jsonobject.getString("headline");
		Text headline = new Text(headlineString);
		headline.setFont(new Font(25));
		file.extractAll(EducationSubScene.tempmod);
		ZipFile temporary_file = new ZipFile(EducationSubScene.tempmod+jsonobject.getString("space"));
		JSONObject space = null;
		try {
			if(temporary_file.isEncrypted()) {
				throw new IllegalArgumentException();
			}
			InputStream inputStream = temporary_file.getInputStream(temporary_file.getFileHeader("space.json"));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[4096];
			int byteRead;
			while((byteRead = inputStream.read(buffer)) !=-1) {
				outputStream.write(buffer);
			}
			String jsonString = outputStream.toString();
			space = new JSONObject(jsonString);
		}catch (IOException e) {
			e.printStackTrace();
		}
		JSONArray defaultcomponents = space.getJSONArray("defaultcomponents");
		ComponentGroupings grouping = new ComponentGroupings();
		ComponentGroup defaults = new ComponentGroup();
		for(Object defaultcomponent : defaultcomponents) {
			if(defaultcomponent instanceof String) {
				try {
					switch((String) defaultcomponent) {
					case("AND"):{
						defaults.add(ANDGate.getSolidANDGATE(LogicComponent.SIZE_MIDDLE, 2));
						break;
					}
					case("NAND"):{
						defaults.add(NANDGate.getSolidNANDGATE(LogicComponent.SIZE_MIDDLE, 2));
						break;
					}
					case("NOR"):{
						defaults.add(NORGate.getSolidNORGATE(LogicComponent.SIZE_MIDDLE, 2));
						break;
					}
					case("NOT"):{
						defaults.add(NOTGate.getSolidNOTGATE(LogicComponent.SIZE_MIDDLE));
						break;
					}
					case("OR"):{
						defaults.add(ORGate.getSolidORGATE(LogicComponent.SIZE_MIDDLE, 2));
						break;
					}
					case("XNOR"):{
						defaults.add(XNORGate.getSolidXNORGATE(LogicComponent.SIZE_MIDDLE, 2));
						break;
					}
					case("XOR"):{
						defaults.add(XORGate.getSolidXORGATE(LogicComponent.SIZE_MIDDLE, 2));
						break;
					}
					case("NONE"):{
						break;
					}
					}
				}catch(IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		grouping.add(defaults);
		groupings = grouping;
		/*JSONArray externalcomponents = jsonobject.getJSONArray("externalcomponents");
		ComponentGroup external = new ComponentGroup();
		*/
		container = new LogicSubSceneContainer((int)width, (int)(height-headline.getBoundsInParent().getHeight()-75), new Group(), grouping, 2);
		File tempfile = new File(EducationSubScene.tempmod);
		if(tempfile.isDirectory()&&tempfile.exists()) {
			File[] files = tempfile.listFiles();
			if (files != null && files.length > 0) {
                // Iterate through each file and delete it
                for (File file2 : files) {
                    file2.delete();
                }
            }
		}
		getChildren().add(container);
		headline.setX((width-headline.getBoundsInParent().getWidth())/2);
		headline.setY(container.getHeight());
		getChildren().add(headline);
		Button button_back = new Button("Back");
		button_back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				getChildren().remove(container);
				LogicSubSceneContainer container = new LogicSubSceneContainer((int)width, (int)(height-headline.getBoundsInParent().getHeight()-75), new Group(), groupings, 2);
				getChildren().add(container);
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
				getChildren().remove(container);
				LogicSubSceneContainer container = new LogicSubSceneContainer((int)width, (int)(height-headline.getBoundsInParent().getHeight()-75), new Group(), groupings, 2);
				getChildren().add(container);
				scene.setNext();
			}
		});
		button_next.setLayoutY(height-button_next.getHeight()-150);
		button_next.setLayoutX(width-button_next.getWidth()-50);
		getChildren().add(button_next);
	}
}
