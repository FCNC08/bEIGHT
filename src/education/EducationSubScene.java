package education;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.json.JSONObject;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class EducationSubScene extends SubScene{

	public EducationSubScene(Parent root, double width, double height, File file) throws IllegalArgumentException, ZipException{
		super(root, width, height);
		JSONObject jsonobject;
		try {
			ZipFile questions = new ZipFile(file);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static EducationSubScene init(double width, double height, File questions) throws IllegalArgumentException, ZipException {
		return new EducationSubScene(new Group(), width, height, questions);
	}

}
