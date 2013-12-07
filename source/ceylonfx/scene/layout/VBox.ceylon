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
	{Node|CeylonNode|VGrowNode*} children = [])
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
	
	{Node*} transform({Node|CeylonNode|VGrowNode*} children) {
		Node|CeylonNode process(Node|CeylonNode|VGrowNode child) {
			switch (child)
			case (is Node|CeylonNode) { return child; }
			case (is VGrowNode) {
				JVBox.setVgrow( child.node.delegate, child.vgrow.delegate );
				return child.node.delegate;
			}
		}
		return asNodes(children.map(process));
	}
	
}

"A Node which can be added to the children of a [[VBox]] with a vgrow constraint."
shared class VGrowNode(shared CeylonNode node, shared Priority vgrow) {}
