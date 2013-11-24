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
		blue=BLUE
	}
}
import javafx.scene.text {
	Text
}

"Run the module `ceylonfx`."
shared void run() {
	
	CeylonFX {
		Stage {
			title = "CeylonFX Demo Application";
			Scene {
				fill = blue;
				Text(50.0, 200.0, "Welcome to CeylonFX"),
				Text(50.0, 250.0, "Under construction...")
			};
			
		};
	};
    
}