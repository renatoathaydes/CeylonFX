import ceylonfx.application {
	CeylonFxAdapter
}
import ceylonfx.binding {
	BooleanProperty,
	WritableStringProperty,
	ObjectProperty
}
import ceylonfx.scene.paint { Paint }
import javafx.scene.control {
	JCheckBox=CheckBox
}
import javafx.scene.paint {
	JPaint=Paint
}
import ceylonfx.scene.paint.utils { toCeylonPaint }

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

	shared ObjectProperty<Paint, JPaint> textFillProperty =>
			ObjectProperty<Paint, JPaint>(delegate.textFillProperty(), toCeylonPaint);
	
	shared WritableStringProperty textProperty => WritableStringProperty(delegate.textProperty());
	
	shared String text => delegate.text;
	
	assign text {
		delegate.text = text;
	}
	
	shared void updateText(String newText) {
		delegate.text = newText;
	}
}
