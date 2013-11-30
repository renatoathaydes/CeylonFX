import ceylonfx.application {
    CeylonFxAdapter,
    asNodes,
    CeylonNode
}
import ceylonfx.geometry {
    Dimension
}
import ceylonfx.scene.paint {
    Paint,
    white
}

import javafx.scene {
    Node,
    JScene=Scene,
    Group
}

shared class Scene(
	Dimension dimension = [600.0, 400.0],
	Boolean depthBuffer = false,
	Paint fill = white,
	{Node|CeylonNode*} children = [])
		extends CeylonFxAdapter<JScene>() {
	
	shared Float width = dimension[0];
	shared Float height = dimension[1];
	
	Group root = Group();
	root.children.setAll(*asNodes(children));
	
	shared actual JScene createDelegate() {
		value actualScene = JScene(root, width, height, depthBuffer);
		actualScene.fill = fill.delegate;
		return actualScene;
	}
}
