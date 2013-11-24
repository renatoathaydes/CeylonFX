import ceylonfx.application {
	JavaApp {
		initialize
	}
}
import ceylonfx.stage {
	Stage
}
import ceylonfx.utils {
	doInFxThread
}

import javafx.scene {
	Node,
	Scene
}
import javafx.stage {
	Window
}

"Application class from which CeylonFX applications extend."
shared class CeylonFX(stage, Boolean showNow = true, String?* args) {

	shared Stage stage;
	
	print("Created CeylonApp");
	value actualStage = initialize(*args);
	
	doInFxThread(void(Object* args) {
		stage.delegate = actualStage;
		if (exists d = stage.delegate) {
			print("App started!!!");
			if (showNow) {
				d.centerOnScreen();
			}
		} else {
			throw Exception("Could not start CeylonFX Application");	
		}
	});
		
	shared void show() {
		actualStage.show();
	}
	
	shared void hide() {
		actualStage.hide();
	}
	
}

shared interface CeylonFxAdapter<out Delegate>
		given Delegate of Node|Window|Scene {
	
	shared formal Delegate? delegate;
	
}
