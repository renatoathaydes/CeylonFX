import ceylonfx.application {
	CeylonFxAdapter
}
import ceylonfx.binding {
	BooleanProperty,
	ObjectProperty,
	StringProperty
}
import ceylonfx.binding.internal {
	bindToJavaFx
}
import ceylonfx.scene.paint {
	Paint,
	black
}
import ceylonfx.utils {
	booleanJ2C,
	stringJ2C,
	paintJ2C
}

import javafx.scene.control {
	JCheckBox=CheckBox
}

shared class CheckBox(
	Boolean state = false,
	String initialText = "",
	Boolean allowIndeterminate = false,
	Paint textFill = black)
		extends CeylonFxAdapter<JCheckBox>() {
	
	shared BooleanProperty selectedProperty = ObjectProperty(state);
	shared ObjectProperty<Paint> textFillProperty = ObjectProperty(textFill);
	shared StringProperty textProperty = ObjectProperty(initialText);
	
	shared actual JCheckBox createDelegate() {
		value actualBox = JCheckBox(initialText);
		actualBox.allowIndeterminate = allowIndeterminate;
		glueProperties(actualBox);
		return actualBox; 
	}
	
	void glueProperties(JCheckBox delegate) {
		selectedProperty.onChange((Boolean selected) => delegate.selected = selected);
		textFillProperty.onChange((Paint textFill) => delegate.textFill = textFill.delegate);
		textProperty.onChange((String text) => delegate.text = text);
		
		bindToJavaFx(delegate.selectedProperty(), selectedProperty, booleanJ2C);
		bindToJavaFx(delegate.textFillProperty(), textFillProperty, paintJ2C);
		bindToJavaFx(delegate.textProperty(), textProperty, stringJ2C);
	}
	
}
