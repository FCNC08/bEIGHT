package canvas.components.ExternalComponents;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;

import canvas.LogicSubScene;
import canvas.components.FunctionalCanvasComponent;
import javafx.scene.image.Image;
import net.lingala.zip4j.ZipFile;

public class ExternalComponent extends FunctionalCanvasComponent {
	public static String json_file = "settings.json";
	
	public static int width_point_multiplier_small = 1;
	public static int width_point_multiplier_middle = 2;
	public static int width_point_multiplier_big = 4;
	public static int height_small = 1*LogicSubScene.cross_distance;
	public static int height_middle = 2*LogicSubScene.cross_distance;
	public static int height_big = 4*LogicSubScene.cross_distance;
	
	protected int[][] truth_tabel;
	protected String name;
	protected Image image;
	public ExternalComponent(String size, int width, int height, int input_count, int output_count, int[][] truth_tabel, String name) throws IllegalAccessException {
		super(width, height, input_count, output_count);
		this.truth_tabel = truth_tabel;
		this.name = name;
		this.size = size;
	}
	
	public ExternalComponent(String size, int width, int height, int input_count, int output_count, int[][] truth_tabel, String name, Image image) throws IllegalAccessException {
		super(width, height, input_count, output_count);
		this.truth_tabel = truth_tabel;
		this.image = image;
		this.name = name;
		this.size = size;
	}

	@Override
	public void setFocus(boolean status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void simulate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FunctionalCanvasComponent getClone(String size) {
		ExternalComponent component = null;
		try {
			component = new ExternalComponent(size, point_width, height, input_count, output_count, truth_tabel, name);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return component;
	}

	@Override
	protected void createInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setVerilogString(short[] comp_count) {
		verilog_string = "External"+comp_count[0];
		comp_count[0]++;
	}

	@Override
	protected void resetStandardImage() {
		// TODO Auto-generated method stub
		
	}
	
	public static ExternalComponent init(String size, ZipFile file) {
		ExternalComponent component = null;
		JSONObject jsonobject = null;
		try {
			if(file.isEncrypted()) throw new IllegalArgumentException();
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
			height=height_small;
			break;
		}
		case "MIDDLE": {
			width*=width_point_multiplier_middle;
			height=height_middle;
			break;
		}
		case "BIG": {
			width*=width_point_multiplier_big;
			height=height_big;
			break;
		}
		default:
			height = 1;
			break;
		}
		if(jsonobject.getBoolean("image_used")) {
			try {
				component = new ExternalComponent(size, width, height, input_count, output_count, null, name, new Image(file.getInputStream(file.getFileHeader(jsonobject.getString("image")))));
			}catch(IllegalAccessException | JSONException | IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				component = new ExternalComponent(size, width, height, input_count, output_count, null, name);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return component;
	}
}
