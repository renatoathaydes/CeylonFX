import ceylonfx.application {
    CeylonFX
}
import ceylonfx.scene {
    Scene
}
import ceylonfx.scene.paint {
    LinearGradient,
    black,
    blue,
    cyan,
    white
}
import ceylonfx.scene.text {
    Text
}
import ceylonfx.stage {
    Stage
}

import javafx.scene.text {
    Font
}


"Run the module `ceylonfx`."
shared void run() =>
	CeylonFX {
		args = ["args", "for", "App"];
		Stage {
			title = "CeylonFX Demo Application";
			Scene {
				fill = black; dimension = [600.0, 150.0];
				Text {
					location= [50.0, 50.0];
					text = "Welcome to CeylonFX";
					font = Font("Arial", 48.0);
					fill = LinearGradient { [0.0, blue], [0.75, cyan], [1.0, white] };
				},
				Text([50.0, 100.0], "Under construction...", Font("Arial", 12.0), white)
			};
			
		};
	};
    
