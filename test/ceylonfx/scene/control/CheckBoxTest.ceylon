import ceylon.test {
	...
}

import ceylonfx.binding {
	binding
}

shared test void canUseBindings() {
	value checkBox = CheckBox();
	binding(checkBox.selectedProperty, checkBox.textProperty, (Boolean sel) => sel then "on" else "off");
	
	checkBox.selected = true;
	assertEquals(checkBox.text, "on");
	checkBox.selected = false;
	assertEquals(checkBox.text, "off");
}