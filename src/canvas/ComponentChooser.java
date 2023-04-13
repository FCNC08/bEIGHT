package canvas;

import javafx.scene.Group;
import javafx.scene.SubScene;

public class ComponentChooser extends SubScene{

	protected LogicSubScene logicScene;
	
	public ComponentChooser(LogicSubScene parent_logicScene,Group root, double Width, double Height) {
		super(root, Width, Height);
	}

}
