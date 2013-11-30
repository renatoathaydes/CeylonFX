import ceylonfx.scene.control { CheckBox }
import ceylonfx.binding { binding }
import ceylon.test { ... }

shared test void bindTest() {
	value cb = CheckBox();
	binding(cb.selectedProperty, cb.textProperty, (Boolean sel) => sel then "on" else "off");
	cb.selected = true;
	assertEquals(cb.text, "on");
	
	cb.selected = false;
	assertEquals(cb.text, "off");
	
}