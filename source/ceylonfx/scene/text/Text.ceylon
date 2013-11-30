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
    JText=Text
}

shared class Text(
    [Float, Float] location = [0.0, 0.0], 
    String text = "",
	Font font = package.font("Arial", 18.0), 
	Paint|CeylonFxAdapter<Paint> fill = black)
        extends CeylonFxAdapter<JText>() {
		
	shared actual JText createDelegate() {
		value actualText = JText(location[0], location[1], text);
		actualText.font = font.font;
		actualText.fill = asType<Paint>(fill);
		return actualText;	
	}
	
}
