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
	
	Group root = Group();
	root.children.setAll(*asNodes(children));
	value jscene = JScene(root, dimension[0], dimension[1], depthBuffer);
	jscene.fill = fill.delegate;
	
	shared Cursor cursor => Cursor(jscene.cursor);
	assign cursor => jscene.cursor=cursor.cursor;
	
	createDelegate() => jscene;
}
