import ceylonfx.scene.paint {
	Paint,
	rgb,
	white
}

import javafx.scene.paint {
	JPaint=Paint,
	JColor=Color
}

//FIXME this is temporary... we will have to change all Paint instances so that it's possible
// create any Paint from the Java instance - so that bindings and property conversion can work
Paint fromJavaPaint(JPaint paint) {
	switch(paint)
	case (is JColor) {
		return rgb(intColor(paint.red), intColor(paint.green), intColor(paint.blue), paint.opacity);
	} else {
		return white;
	}
}

shared Paint toCeylonPaint(JPaint? paint) {
	if (exists paint) {
		return fromJavaPaint(paint);
	} else {
		throw Exception("No Paint provided, cannot convert to Ceylon Paint");
	}
}

// why does JavaFX only allows the creation of colors using components in the range 0..255
// but when you get the components of an existing color, they are in the range 0..1.0?????
"Converts a color component in the range 0..1 to 0..255"
Integer intColor(Float component) {
	value normalized = min({ max({ component, 0.0 }), 1.0 });
	return (normalized * 255.0).integer;
}