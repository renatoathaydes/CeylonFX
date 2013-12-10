import ceylonfx.application {
	CeylonFxAdapter,
	CeylonNode,
	asNodes
}
import ceylonfx.binding {
	Binding,
	ObjectProperty
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
    Paint|Binding<Object, Paint> fill = white,
    Boolean depthBuffer = false,
    {Node|CeylonNode*} children = [])
        extends CeylonFxAdapter<JScene>() {
    
    shared ObjectProperty<Paint> fillProperty = ObjectProperty<Paint>(white);
    
    shared actual JScene createDelegate() {
        Group root = Group();
        root.children.setAll(*asNodes(children));
        value jscene = JScene(root, dimension[0], dimension[1], depthBuffer);
        fillProperty.onChange((Paint paint) => jscene.fill = paint.delegate);
        switch(fill)
        case (is Paint) { fillProperty.set(fill); }
        case (is Binding<Object, Paint>) { fill.bind(fillProperty); }
        return jscene;
    }
    
    shared Cursor cursor => Cursor(delegate.cursor);
    assign cursor => delegate.cursor=cursor.cursor;
    
}
