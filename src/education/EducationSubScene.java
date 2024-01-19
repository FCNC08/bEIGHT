package education;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;

public class EducationSubScene extends SubScene{

	public EducationSubScene(Parent root, double width, double height) {
		super(root, width, height);
	}
	
	public static EducationSubScene init(double width, double height) {
		return new EducationSubScene(new Group(), width, height);
	}

}
