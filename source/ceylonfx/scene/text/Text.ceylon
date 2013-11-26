import ceylonfx.application { CeylonFxAdapter, asType }
import javafx.scene.text { JText = Text, Font }
import javafx.scene.paint { Paint, Color { black = \iBLACK } }

shared class Text(Float x = 0.0, Float y = 0.0, String text = "",
	Font font = Font("Arial", 18.0), Paint|CeylonFxAdapter<Paint> textFill = black)
		satisfies CeylonFxAdapter<JText> {
	
	value actualText = JText(x, y, text);
	actualText.font = font;
	actualText.fill = asType<Paint>(textFill);
	
	shared actual JText delegate => actualText;
	
}
