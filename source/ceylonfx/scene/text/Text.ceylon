import ceylonfx.application { CeylonFxAdapter, asType }
import javafx.scene.text { JText = Text, Font }
import javafx.scene.paint { Paint, Color { black = \iBLACK } }

shared class Text([Float, Float] location = [0.0, 0.0], String text = "",
	Font font = Font("Arial", 18.0), Paint|CeylonFxAdapter<Paint> fill = black)
		extends CeylonFxAdapter<JText>() {
		
	shared actual JText createDelegate() {
		value actualText = JText(location[0], location[1], text);
		actualText.font = font;
		actualText.fill = asType<Paint>(fill);
		return actualText;	
	}
	
}
