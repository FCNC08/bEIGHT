package education;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class EducationSubScene extends SubScene{

	protected Group root;
	public EducationSubScene(double width, double height, File file) throws IllegalArgumentException, ZipException{
		super(new Group(), width, height);
		this.root = (Group) getRoot();
		JSONObject jsonobject = null;
		ZipFile questions = null;
		try {
			questions = new ZipFile(file);
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
			
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pane choosing_area = new Pane();
		String headline_text = jsonobject.getString("name");
		Text headline = new Text(headline_text);
		headline.setFont(new Font(50));
		headline.setX((width-headline.getBoundsInParent().getWidth())/2);
		headline.setY(50);
		choosing_area.getChildren().add(headline);
		String difficulty_text = jsonobject.getString("difficulty");
		Text difficulty = new Text(difficulty_text);
		difficulty.setFont(new Font(25));
		difficulty.setX((width-difficulty.getBoundsInParent().getWidth())/2);
		difficulty.setY(headline.getBoundsInParent().getHeight()*1.2);
		choosing_area.getChildren().add(difficulty);
		ArrayList<Pane> order = new ArrayList<>();
		JSONArray modules = jsonobject.getJSONArray("order");
		questions.extractAll("temporary/");
		for(Object object : modules) {
			if(object instanceof JSONObject) {
				JSONObject modul = (JSONObject) object;
				switch(modul.getString("type")) {
				case("lesson"):{
					ZipFile temporary_file = new ZipFile("temporary"+modul.getString("filename"));
					order.add(new EducationLesson(width, height,temporary_file));
					System.out.println(order.get(order.size()-1));
					break;
				}
				case("question"):{
					ZipFile temporary_file = new ZipFile("temporary/"+modul.getString("filename"));
					order.add(new Question(width, height, temporary_file));
					System.out.println(order.get(order.size()-1));
					new File("temporary/").delete();
					break;
				}
				}
			}
		}
		File tempfile = new File("temporary/");
		if(tempfile.isDirectory()&&tempfile.exists()) {
			File[] files = tempfile.listFiles();
			if (files != null && files.length > 0) {
                // Iterate through each file and delete it
                for (File file2 : files) {
                    if (file2.isFile()) {
                        file2.delete();
                    }
                }
            } else {
                System.out.println("No files to delete in the directory: " + "temporary/");
            }
		}
		root.getChildren().add(choosing_area);
	}

}
