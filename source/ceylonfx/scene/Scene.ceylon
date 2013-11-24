import ceylonfx.application {
	CeylonFxAdapter
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

shared class Scene(width = 600.0, height = 400.0, depthBuffer = false,
	fill = white, children = [])
		satisfies CeylonFxAdapter<JScene> {
	
	shared Float width;
	shared Float height;
	shared Boolean depthBuffer;
	shared Paint fill;
	shared {Node*} children;
	
	shared actual JScene delegate {
		Group root = Group();
		root.children.setAll(*children);
		value actualScene = JScene(root, width, height, depthBuffer);
		actualScene.fill = fill;
		return actualScene;
	}
	
}
