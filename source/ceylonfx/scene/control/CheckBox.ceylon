import javafx.scene.control{ JCheckBox=CheckBox }
import ceylonfx.application { CeylonFxAdapter }
import javafx.beans.property { BooleanProperty, ObjectProperty, StringProperty }
import javafx.scene.paint { Paint }

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
	
	shared BooleanProperty selectedProperty => delegate.selectedProperty();
	
	shared ObjectProperty<Paint> textFillProperty => delegate.textFillProperty();
	
	shared StringProperty textProperty => delegate.textProperty();
	
	shared String text => delegate.text;
}
