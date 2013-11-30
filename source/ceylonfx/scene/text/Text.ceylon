import ceylonfx.application {
    CeylonFxAdapter,
    asType
}
import ceylonfx.geometry {
    VerticalPosition,
    verticalBaseline
}
import ceylonfx.scene.paint {
    black
}

import javafx.scene.paint {
    Paint
}
import javafx.scene.text {
    JText=Text,
    JFontSmoothingType=FontSmoothingType,
    JTextBoundsType=TextBoundsType,
    JTextAlignment=TextAlignment
}

shared class Text(
    String text,
	Font font = package.font("Arial", 18.0), 
	[Float, Float] location = [0.0, 0.0], 
	Paint|CeylonFxAdapter<Paint> fill = black,
	Boolean underline = false,
	Boolean strikethrough = false,
	Float? wrappingWidth = null,
	FontSmoothing fontSmoothing = graySmoothing,
	TextBounds textBounds = logicalBounds,
	TextAlignment textAlignment = left,
	VerticalPosition textOrigin = verticalBaseline)
        extends CeylonFxAdapter<JText>() {
    
	shared actual JText createDelegate() {
		value jtext = JText(location[0], location[1], text);
		jtext.font = font.font;
		jtext.fill = asType<Paint>(fill);
		jtext.underline = underline;
		jtext.strikethrough = strikethrough;
		jtext.wrappingWidth = wrappingWidth else 0.0;
		jtext.fontSmoothingType = fontSmoothing.type;
		jtext.boundsType = textBounds.type;
		jtext.textAlignment = textAlignment.type;
		jtext.textOrigin = textOrigin.vpos;
		return jtext;	
	}
	
}

shared abstract class FontSmoothing(shared JFontSmoothingType type)
        of graySmoothing|lcdSmoothing {
    string=>type.string;
}
shared object lcdSmoothing extends FontSmoothing(JFontSmoothingType.\iGRAY) {}
shared object graySmoothing extends FontSmoothing(JFontSmoothingType.\iLCD) {}

shared abstract class TextBounds(shared JTextBoundsType type)
        of logicalBounds|visualBounds {
    string=>type.string;
}
shared object logicalBounds extends TextBounds(JTextBoundsType.\iLOGICAL) {}
shared object visualBounds extends TextBounds(JTextBoundsType.\iVISUAL) {}

shared abstract class TextAlignment(shared JTextAlignment type)
        of center|justify|left|right {
    string=>type.string;
}
shared object center extends TextAlignment(JTextAlignment.\iCENTER) {}
shared object justify extends TextAlignment(JTextAlignment.\iJUSTIFY) {}
shared object left extends TextAlignment(JTextAlignment.\iLEFT) {}
shared object right extends TextAlignment(JTextAlignment.\iRIGHT) {}
