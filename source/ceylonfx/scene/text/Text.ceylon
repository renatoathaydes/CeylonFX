import ceylonfx.geometry {
	VerticalPosition,
	verticalBaseline
}
import ceylonfx.scene.paint {
	Paint,
	black
}
import ceylonfx.scene.shape {
	Shape,
	squareLineCap,
	miterLineJoin,
	StrokeType,
	StrokeLineCap,
	StrokeLineJoin,
	centeredStroke
}

import javafx.scene.text {
	JText=Text,
	JFontSmoothingType=FontSmoothingType,
	JTextBoundsType=TextBoundsType,
	JTextAlignment=TextAlignment
}

shared class Text(
	shared String text,
	shared Font font = package.font("Arial", 18.0), 
	shared Boolean underline = false,
	shared Boolean strikethrough = false,
	shared Float? wrappingWidth = null,
	shared FontSmoothing fontSmoothing = graySmoothing,
	shared TextBounds textBounds = logicalBounds,
	shared TextAlignment textAlignment = left,
	shared VerticalPosition textOrigin = verticalBaseline,
	Paint fill = black,
	Boolean smooth = true,
	Float strokeDashOffset = 0.0,
	StrokeLineCap strokeLineCap = squareLineCap,
	StrokeLineJoin strokeLineJoin = miterLineJoin,
	Float strokeMiterLimit = 10.0,
	Paint? stroke = null,
	StrokeType strokeType = centeredStroke,
	Float strokeWidth = 1.0)
		extends Shape<JText>(
		fill, smooth, strokeDashOffset, strokeLineCap, strokeLineJoin,
		strokeMiterLimit, stroke, strokeType, strokeWidth) {
	
	shared actual JText createDelegate() {
		value jtext = JText(location[0], location[1], text);
		jtext.font = font.font;
		jtext.underline = underline;
		jtext.strikethrough = strikethrough;
		jtext.wrappingWidth = wrappingWidth else 0.0;
		jtext.fontSmoothingType = fontSmoothing.type;
		jtext.boundsType = textBounds.type;
		jtext.textAlignment = textAlignment.type;
		jtext.textOrigin = textOrigin.vpos;
		transferPropertiesTo(jtext);
		return jtext;	
	}
	
}

shared abstract class FontSmoothing(shared JFontSmoothingType type)
		of graySmoothing|lcdSmoothing {
	string=>type.string;
}
shared object lcdSmoothing extends FontSmoothing(JFontSmoothingType.\iLCD) {}
shared object graySmoothing extends FontSmoothing(JFontSmoothingType.\iGRAY) {}

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
