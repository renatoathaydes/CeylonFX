import ceylon.test {
	...
}

import ceylonfx.binding {
	bindConverting
}
import ceylonfx.scene.paint { red }

shared test void propertiesCanBeBound() {
	value checkBox = CheckBox();
	
	bindConverting(checkBox.selectedProperty, checkBox.textProperty, (Boolean selected) =>
			selected then "on" else "off");
	
	checkBox.selectedProperty.set(true);
	assertEquals(checkBox.textProperty.get, "on");
	checkBox.selectedProperty.set(false);
	assertEquals(checkBox.textProperty.get, "off");
}

shared test void propertiesConnectedToDelegate() {
	value checkBox = CheckBox();
	
	checkBox.selectedProperty.set(true);
	assertEquals(checkBox.delegate.selected, true);
	checkBox.selectedProperty.set(false);
	assertEquals(checkBox.delegate.selected, false);
	
	checkBox.textProperty.set("Hi");
	assertEquals(checkBox.delegate.text, "Hi");
	
	checkBox.textFillProperty.set(red);
	assertEquals(checkBox.delegate.textFill, red.delegate);
}

