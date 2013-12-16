import ceylonfx.binding {
	Binding
}
import ceylonfx.scene {
	Node,
	Cursor
}
import ceylonfx.scene.effect {
	Effect
}

import javafx.scene {
	JNode=Node
}

"All Node direct sub-classes should call this method when creating their delegate to get proper initialization"
shared void initNode(
	Node<JNode> node,
	JNode jnode) {
	if (exists bm = node.blendMode) { jnode.blendMode = bm.delegate; }
	jnode.cacheHint = node.cacheHint.delegate;
	if (exists clip = node.clip) { jnode.clip = clip.delegate; }
	
	if (exists item = node.cursor) {
		switch(item)
		case (is Cursor) { jnode.cursor = item.cursor; }
		case (is Binding<Object, Cursor>) { item.bind(node.cursorProperty); }
	}
	
	jnode.depthTest = node.depthTest.delegate;
	
	if (exists item = node.effect) {
		switch(item)
		case (is Effect) { jnode.effect= item.delegate; }
		case (is Binding<Object, Effect>) { item.bind(node.effectProperty); }
	}
	
	jnode.focusTraversable = node.focusTraversable;
	jnode.id = node.id;
	jnode.layoutX = node.location[0];
	jnode.layoutY = node.location[1];
	jnode.managed = node.managed;
	jnode.mouseTransparent = node.mouseTransparent;
	jnode.pickOnBounds = node.pickOnBounds;
	jnode.rotate = node.rotate;
	jnode.rotationAxis = node.rotationAxis.delegate;
	jnode.scaleX = node.scale[0];
	jnode.scaleY = node.scale[1];
	jnode.scaleZ = node.scale[2];
	jnode.style = node.style;
	jnode.translateX = node.translate[0];
	jnode.translateY = node.translate[1];
	jnode.visible = node.visible;
}
