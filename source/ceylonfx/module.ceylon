"CeylonFX exposed the JavaFX interface into Ceylon"
native("jvm")
module ceylonfx "0.2.0" {
	shared import java.base "8";
	// TODO stop re-exporting JavaFX
	shared import javafx.base "8";
	shared import javafx.graphics "8";
	shared import javafx.controls "8";
	import ceylon.interop.java "1.3.2";
	import ceylon.test "1.3.2";
}
