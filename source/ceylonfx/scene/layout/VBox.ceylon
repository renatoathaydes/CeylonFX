import ceylonfx.application {
	CeylonFxAdapter,
	CeylonNode
}
import ceylonfx.geometry {
	Position,
	topLeft,
	Insets
}

import javafx.scene {
	Node
}
import javafx.scene.layout {
	JVBox=VBox
}

"VBox lays out its children in a single vertical column."
shared class VBox(
	Integer spacing = 0,
	Position alignment = topLeft,
	Insets insets = Insets(),
	{Node|CeylonNode|[CeylonNode, Priority]*} children = [])
		extends CeylonFxAdapter<JVBox>() {
	
	shared actual JVBox createDelegate() {
		value jvbox = JVBox();
		jvbox.spacing = spacing.float;
		jvbox.alignment = alignment.pos;
		jvbox.children.setAll(*transform(children));
		return jvbox;
	}
	
	{Node*} transform({<Node|CeylonNode|[CeylonNode, Priority]>*} children) {
		variable {Node*} result = {};
		for(child in children) {
			switch (child)
			case (is Node) { result = result.chain([child]); }
			case (is CeylonNode) { result = result.chain([child.delegate]); }
			case (is [CeylonNode, Priority]) {
				JVBox.setVgrow( child.first.delegate, child[1].priority );
				result = result.chain([child.first.delegate]);
			} //else { throw;}
		}
		return result;
	}
	
}