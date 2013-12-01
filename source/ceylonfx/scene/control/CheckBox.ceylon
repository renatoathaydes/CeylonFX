import ceylonfx.application {
	CeylonFxAdapter
}
import ceylonfx.binding {
	BooleanProperty,
	WritableStringProperty
}

import javafx.beans.property {
	ObjectProperty
}
import javafx.scene.control {
	JCheckBox=CheckBox
}
import javafx.scene.paint {
	Paint
}

shared class CheckBox(
	String initialText = "",
	Boolean allowIndeterminate = false)
		extends CeylonFxAdapter<JCheckBox>() {
	
	shared actual JCheckBox createDelegate() {
		value actualBox = JCheckBox(initialText);
		actualBox.allowIndeterminate = allowIndeterminate;
		return actualBox; 
	}
	
	shared Boolean selected => delegate.selected;
	
	assign selected {
		delegate.selected = selected;
	}
	
	shared BooleanProperty selectedProperty => BooleanProperty(delegate.selectedProperty());
	
	//TODO implement CeylonFx ObjectProperty and replace this
	shared ObjectProperty<Paint> textFillProperty => delegate.textFillProperty();
	
	shared WritableStringProperty textProperty => WritableStringProperty(delegate.textProperty());
	
	shared String text => delegate.text;
	
	assign text {
		delegate.text = text;
	}
	
	shared void updateText(String newText) {
		delegate.text = newText;
	}
}
