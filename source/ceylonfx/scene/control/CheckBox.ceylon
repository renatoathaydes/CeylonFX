import javafx.scene.control{ JCheckBox=CheckBox }
import ceylonfx.application { CeylonFxAdapter }

shared class CheckBox(
	String text = "",
	Boolean allowIndeterminate = false)
		extends CeylonFxAdapter<JCheckBox>() {
	
	shared actual JCheckBox createDelegate() {
		value actualBox = JCheckBox(text);
		actualBox.allowIndeterminate = allowIndeterminate;
		return actualBox; 
	}
	
}
