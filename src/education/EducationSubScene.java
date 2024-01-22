package education;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import net.lingala.zip4j.ZipFile;

public class EducationSubScene extends SubScene{

	public EducationSubScene(Parent root, double width, double height, ZipFile questions) {
		super(root, width, height);
	}
	
	public static EducationSubScene init(double width, double height, ZipFile questions) {
		return new EducationSubScene(new Group(), width, height, questions);
	}

}
