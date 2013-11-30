import ceylonfx.scene {
	Scene
}

import javafx.stage {
	JStage = Stage,
	JStageStyle=StageStyle {
		...
	}
}

"The Stage class is the top level CeylonFX container."
shared class Stage(scene, stageStyle = decorated, title = "CeylonFX") {
	
	shared StageStyle stageStyle;
	shared Scene scene;
	shared String title;
	variable JStage? actual = null;
	
	shared JStage? delegate => actual;

	assign delegate {
		if (exists delegate) {
			actual = delegate;
			delegate.title = title;
			delegate.scene = scene.delegate;
		}
		
	}

}

JStageStyle forJava(StageStyle style) {
	switch (style)
	case (decorated) { return \iDECORATED; }
	case (transparent) { return \iTRANSPARENT; }
	case (undecorated) { return \iUNDECORATED; }
	case (utility) { return \iUTILITY; }
}

shared abstract class StageStyle()
		of decorated|transparent|undecorated|utility {}

"Defines a normal Stage style with a solid white background and platform decorations."
shared object decorated extends StageStyle() {}

"Defines a Stage style with a transparent background and no decorations."
shared object transparent extends StageStyle() {}

"Defines a Stage style with a solid white background and no decorations."
shared object undecorated extends StageStyle() {}

"Defines a Stage style with a solid white background and minimal platform decorations used for a utility window."
shared object utility extends StageStyle() {}
