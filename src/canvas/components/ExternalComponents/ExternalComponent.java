package canvas.components.ExternalComponents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import canvas.LogicSubScene;
import canvas.components.Dot;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.State;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import net.lingala.zip4j.ZipFile;
import util.ErrorStateExeption;

public class ExternalComponent extends FunctionalCanvasComponent {
	public static String json_file = "settings.json";
	
	public static int width_point_multiplier_small = 1;
	public static int width_point_multiplier_middle = 2;
	public static int width_point_multiplier_big = 4;
	public static int height_small = 1*LogicSubScene.cross_distance;
	public static int height_middle = 2*LogicSubScene.cross_distance;
	public static int height_big = 4*LogicSubScene.cross_distance;
	
	protected Truthtabel truth_tabel;
	protected String name;
	protected Image image;
	public ExternalComponent(String size, int width, int height, int input_count, int output_count, Truthtabel truth_tabel, String name) throws IllegalAccessException {
		super(width, height, input_count, output_count, size);
		this.truth_tabel = truth_tabel;
		this.name = name;
		createContextMenu();
	}
	
	public ExternalComponent(String size, int width, int height, int input_count, int output_count, Truthtabel truth_tabel, String name, Image image) throws IllegalAccessException {
		super(width, height, input_count, output_count, size);
		this.truth_tabel = truth_tabel;
		this.image = image;
		this.name = name;
		resetStandardImage();
		createContextMenu();
	}

	@Override
	public void simulater() {
		try {
			State[] output_states = truth_tabel.getState(getInputStates());
			for(int i = 0; i<output_count; i++) {
				this.outputs[i].setState(output_states[i]);
			}
		} catch (ErrorStateExeption e) {
			for(Dot d : outputs) {
				d.setState(State.ERROR);
			}
			e.printStackTrace();
		}
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		ExternalComponent component = null;
		try {
			int new_width = Math.max(input_count, output_count)*LogicSubScene.cross_distance;
			int new_height;
			
			switch (size) {
			case "SMALL": {
				new_width*=width_point_multiplier_small;
				break;
			}
			case "MIDDLE": {
				new_width*=width_point_multiplier_middle;
				break;
			}
			case "BIG": {
				new_width*=width_point_multiplier_big;
				break;
			}
			default:
				break;
			}
			new_height = new_width;
			if(image != null) {
				component = new ExternalComponent(size, new_width, new_height, input_count, output_count, truth_tabel, name, image);
			}else {
				component = new ExternalComponent(size, new_width, new_height, input_count, output_count, truth_tabel, name);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return component;
	}


	@Override
	protected void setVerilogString(short[] comp_count) {
		//verilog_string = "External"+comp_count[0];
		//comp_count[0]++;
	}
	
	@Override
	protected void setArduinoString(short[] comp_count) {
		//arduino_string = "External"+comp_count[0];
		//comp_count[0]++;
	}

	@Override
	protected void resetStandardImage() {
		if(image != null) {
			ImageView view = new ImageView(image);
			view.setFitHeight(height);
			view.setFitWidth(width);
			view.snapshot(null, this);
		}else {
			for(int x = 0; x<width; x++) {
				for(int y = 0; y<height; y++) {
					pwriter.setColor(x, y, Color.BLACK);
				}
			}
		}

	}
	
	public static ExternalComponent init(String size, ZipFile file) {
		ExternalComponent component = null;
		JSONObject jsonobject = null;
		try {
			if(file.isEncrypted()) throw new IllegalArgumentException();
			System.out.println(file.getFileHeaders());
			InputStream is = file.getInputStream(file.getFileHeader(json_file));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead;
			while((bytesRead = is.read(buffer))!=-1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			String jsonString = outputStream.toString(StandardCharsets.UTF_8);
			jsonobject = new JSONObject(jsonString);
		}catch(IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		String name = jsonobject.getString("name");
		int input_count = jsonobject.getInt("input_count");
		int output_count = jsonobject.getInt("output_count");
		int width = Math.max(input_count, output_count)*LogicSubScene.cross_distance;
		int height;
		
		switch (size) {
		case "SMALL": {
			width*=width_point_multiplier_small;
			break;
		}
		case "MIDDLE": {
			width*=width_point_multiplier_middle;
			break;
		}
		case "BIG": {
			width*=width_point_multiplier_big;
			break;
		}
		default:
			break;
		}
		height = width;
		JSONArray truth = jsonobject.getJSONArray("truth_table");
		if(jsonobject.getBoolean("image_used")) {
			try {
				component = new ExternalComponent(size, width, height, input_count, output_count, new Truthtabel(truth), name, new Image(file.getInputStream(file.getFileHeader(jsonobject.getString("image")))));
			}catch(IllegalAccessException | JSONException | IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				component = new ExternalComponent(size, width, height, input_count, output_count, new Truthtabel(truth), name);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return component;
	}
	
	@Override
	protected void createContextMenu() {
		Label name_label = new Label(name);
		name_label.getStyleClass().add("cm-header");
		name_label.setMouseTransparent(true);
		CustomMenuItem name_item = new CustomMenuItem(name_label);
		name_item.getStyleClass().add("cm-header-item");
		menu.getItems().clear();
		menu.getItems().add(name_item);
		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(turn);
	}

	@Override
	public void createLayerGate() {		
	}
}
