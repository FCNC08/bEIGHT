package canvas;

import javafx.scene.Group;
import javafx.scene.SubScene;

public class ComponentChooser extends SubScene{

	protected LogicSubScene logic_Scene;
	
	public ComponentChooser(LogicSubScene parent_logicScene,Group root, double Width, double Height) {
		super(root, Width, Height);
	}

}
