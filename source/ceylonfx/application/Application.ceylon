
import ceylonfx.application.java {
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
	Node
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
		given Delegate satisfies Object {
	
	shared formal Delegate? delegate;
	
}

shared {Node*} asNodes({Node|CeylonFxAdapter<Node>*} mixed) {
	value nodes = { for(node in mixed) asType<Node>(node) };
	return { for(node in nodes) if (exists node) node };
}

shared Type? asType<out Type>(Type|CeylonFxAdapter<Type> toConvert)
	given Type satisfies Object {
	if (is Type toConvert) {
		return toConvert;
	}
	if (is CeylonFxAdapter<Type> toConvert, exists del = toConvert.delegate) {
		return del;
	}
	return null;
}
