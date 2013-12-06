import ceylonfx.application {
	CeylonFxAdapter,
	CeylonNode,
	asNodes
}
import ceylonfx.geometry {
	Position,
	topLeft,
	Insets,
	Dimension
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
	Insets? insets = null,
	Dimension? minimumSize = null,
	Dimension? preferredSize = null,
	Dimension? maximumSize = null,
	{Node|CeylonNode|NodeWithConstraint*} children = [])
		extends CeylonFxAdapter<JVBox>() {
	
	shared actual JVBox createDelegate() {
		value jvbox = JVBox();
		jvbox.spacing = spacing.float;
		jvbox.alignment = alignment.pos;
		if (exists minimumSize) { jvbox.setMinSize(*minimumSize); }
		if (exists preferredSize) { jvbox.setPrefSize(*preferredSize); }
		if (exists maximumSize) { jvbox.setMaxSize(*maximumSize); }
		jvbox.children.setAll(*transform(children));
		if (exists insets, !children.empty) {
			jvbox.setMargin(jvbox.children.get(0), insets.delegate);
		}
		return jvbox;
	}
	
	{Node*} transform({Node|CeylonNode|NodeWithConstraint*} children) {
		Node|CeylonNode process(Node|CeylonNode|NodeWithConstraint child) {
			switch (child)
			case (is Node|CeylonNode) { return child; }
			case (is NodeWithConstraint) {
				JVBox.setVgrow( child.node.delegate, child.priority.delegate );
				return child.node.delegate;
			}
		}
		return asNodes(children.map(process));
	}
	
}