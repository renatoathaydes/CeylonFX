import ceylonfx.application {
    CeylonFxAdapter,
    asType
}
import ceylonfx.scene.paint {
    black
}

import javafx.scene.paint {
    Paint
}
import javafx.scene.text {
    JText=Text, FontSmoothingType, TextBoundsType, TextAlignment
}
import javafx.geometry { VPos }

shared class Text(
    String text = "",
	Font font = package.font("Arial", 18.0), 
	[Float, Float] location = [0.0, 0.0], 
	Paint|CeylonFxAdapter<Paint> fill = black,
	Boolean underline = false,
	Boolean strikethrough = false,
	Float? wrappingWidth = null,
	FontSmoothingType fontSmoothingType = FontSmoothingType.\iGRAY,
	TextBoundsType textBoundsType = TextBoundsType.\iLOGICAL,
	TextAlignment textAlignment = TextAlignment.\iLEFT,
	VPos textOrigin = VPos.\iBASELINE)
        extends CeylonFxAdapter<JText>() {
		
	shared actual JText createDelegate() {
		value jtext = JText(location[0], location[1], text);
		jtext.font = font.font;
		jtext.fill = asType<Paint>(fill);
		jtext.underline = underline;
		jtext.strikethrough = strikethrough;
		jtext.wrappingWidth = wrappingWidth else 0.0;
		jtext.fontSmoothingType = fontSmoothingType;
		jtext.boundsType = textBoundsType;
		jtext.textAlignment = textAlignment;
		jtext.textOrigin = textOrigin;
		return jtext;	
	}
	
}
