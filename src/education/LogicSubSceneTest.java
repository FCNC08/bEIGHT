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
import canvas.components.FunctionalCanvasComponent;
import canvas.components.LogicComponent;
import canvas.components.ExternalComponents.ExternalComponent;
import canvas.components.StandardComponents.Input;
import canvas.components.StandardComponents.Output;
import canvas.components.StandardComponents.LogicComponents.ANDGate;
import canvas.components.StandardComponents.LogicComponents.NANDGate;
import canvas.components.StandardComponents.LogicComponents.NORGate;
import canvas.components.StandardComponents.LogicComponents.NOTGate;
import canvas.components.StandardComponents.LogicComponents.ORGate;
import canvas.components.StandardComponents.LogicComponents.XNORGate;
import canvas.components.StandardComponents.LogicComponents.XORGate;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class LogicSubSceneTest extends ScrollPane{
	protected VBox vbox = new VBox(20);
	public LogicSubSceneTest(double width, double height, ZipFile file, EducationSubScene scene) throws ZipException {
		super();
		setContent(vbox);
		setPrefHeight(height-75);
		setPrefWidth(width);
		setFitToWidth(true);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(0,0,50,0));
		
		JSONObject jsonobject = null;
		final ComponentGroupings groupings;
		LogicSubSceneContainer container;
		try (InputStream inputstream = file.getInputStream(file.getFileHeader("test.json"));
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream()) {
			
			byte[] buffer = new byte[4096];
			int byteRead;
			while((byteRead = inputstream.read(buffer))!= -1) {
				outputstream.write(buffer, 0, byteRead);
			}
			String jsonString = new String(outputstream.toByteArray(),StandardCharsets.UTF_8);
			jsonobject = new JSONObject(jsonString);
		}catch(IOException e) {
			e.printStackTrace();
		}
		String headlineString = jsonobject.getString("headline");
		Label headline = new Label(headlineString);
		headline.setWrapText(true);
		headline.setMaxWidth(width*0.85);
		headline.setFont(new Font(25));
		
		double headlineHeight = headline.prefHeight(width * 0.85);
		
		file.extractAll(EducationSubScene.tempmod);
		@SuppressWarnings("resource")
		ZipFile temporary_file = new ZipFile(EducationSubScene.tempmod+jsonobject.getString("space"));
		JSONObject space = null;
		try (InputStream inputStream = temporary_file.getInputStream(temporary_file.getFileHeader("space.json"))){
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[4096];
			int byteRead;
			while((byteRead = inputStream.read(buffer)) !=-1) {
				outputStream.write(buffer, 0, byteRead);
			}
			String jsonString = outputStream.toString();
			space = new JSONObject(jsonString);
		}catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(space);
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
		ComponentGroup inoutput = new ComponentGroup();
		inoutput.add(Input.getInput(FunctionalCanvasComponent.SIZE_MIDDLE));
		inoutput.add(Output.getOutput(FunctionalCanvasComponent.SIZE_MIDDLE));
		grouping.add(inoutput);
		try{
			ComponentGroup external = new ComponentGroup();
			JSONArray externalcomponents = space.getJSONArray("externalcomponents");
			temporary_file.extractAll(EducationSubScene.tempext);
			for(Object externalcomponent : externalcomponents) {
				if(externalcomponent instanceof String) {
					ZipFile external_file = new ZipFile(EducationSubScene.tempext+(String)externalcomponent);
					ExternalComponent comp = ExternalComponent.init(ExternalComponent.SIZE_MIDDLE, external_file);
					external.add(comp);
				}
			}
			//new File(EducationSubScene.tempext).delete();
			grouping.add(external);
		}catch (Exception e) {
			e.printStackTrace();
		}
		groupings = grouping;
		
		int containerHeight = (int) Math.max(150, (height - 75 - headlineHeight)*0.71); // clamp to a sane min height
		container = new LogicSubSceneContainer((int) width, containerHeight, new Group(), grouping, 2);
		
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
		HBox button_box = new HBox();
		Button button_back = new Button("Back");
		button_back.setFont(new Font(25));
		button_back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        vbox.getChildren().clear();
		        // Recreate container with the same sizing logic
		        int containerHeight = (int) Math.max(150, (height - 75 - headline.prefHeight(width * 0.85)));
		        LogicSubSceneContainer container =
		            new LogicSubSceneContainer((int) width, containerHeight, new Group(), groupings, 2);
		        vbox.getChildren().addAll(headline, container, button_box);
		        scene.setPrev();
		    }
		});
		
		Button button_next = new Button("Next");
		button_next.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        vbox.getChildren().clear();
		        int containerHeight = (int) Math.max(150, (height - 75 - headline.prefHeight(width * 0.85)));
		        LogicSubSceneContainer container =
		            new LogicSubSceneContainer((int) width, containerHeight, new Group(), groupings, 2);
		        vbox.getChildren().addAll(headline, container, button_box);
		        scene.setNext();
		    }
		});
		button_next.setFont(new Font(25));
		button_box.getChildren().addAll(button_back, button_next);
		
		button_box.setPadding(Insets.EMPTY);
		vbox.getChildren().addAll(headline, container, button_box);
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setPadding(new Insets(20, 0, 50, 0));
		requestLayout();
	}
}
