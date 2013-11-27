import ceylonfx.application {
	CeylonFX
}
import ceylonfx.scene {
	Scene
}
import ceylonfx.stage {
	Stage
}

import javafx.scene.paint {
	Color {
		black = \iBLACK, cyan = \iCYAN, blue = \iBLUE, white = \iWHITE
	}
}
import ceylonfx.scene.paint{ LinearGradient } 
import ceylonfx.scene.text {
	Text
}
import javafx.scene.text { Font }


"Run the module `ceylonfx`."
shared void run() {
	value largeFont = Font("Arial", 48.0);
	value smallFont = Font("Arial", 12.0);
	value mainFill = LinearGradient { [0.0, blue], [0.75, cyan], [1.0, white] };
	
	CeylonFX {
		args = ["args", "for", "App"];
		Stage {
			title = "CeylonFX Demo Application";
			Scene {
				fill = black; dimension = [600.0, 150.0];
				Text([50.0, 50.0], "Welcome to CeylonFX", largeFont, mainFill),
				Text([50.0, 100.0], "Under construction...", smallFont, white)
			};
			
		};
	};
    
}