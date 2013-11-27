import ceylonfx.application {
	CeylonFxAdapter,
	asNodes, CeylonNode
}

import javafx.scene {
	Node,
	JScene=Scene,
	Group
}
import javafx.scene.paint {
	Paint,
	Color {
		white=WHITE
	}
}

shared class Scene(dimension = [600.0, 400.0], depthBuffer = false,
	fill = white, children = [])
		satisfies CeylonFxAdapter<JScene> {
	
	shared [Float, Float] dimension;
	shared Float width = dimension[0];
	shared Float height = dimension[1];
	shared Boolean depthBuffer;
	shared Paint fill;
	shared {Node|CeylonNode*} children;
	
	Group root = Group();
	root.children.setAll(*asNodes(children));
	
	shared actual JScene delegate => createActualScene();
	
	JScene createActualScene() {
		value actualScene = JScene(root, width, height, depthBuffer);
		actualScene.fill = fill;
		return actualScene;
	}
}
