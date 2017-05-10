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
shared class Stage(shared Scene() scene,
	shared String title = "CeylonFX") {
	
	variable JStage? jStage = null;
	
	shared JStage? delegate => jStage;

	assign delegate {
		"Cannot set Stage delegate to null"
		assert(exists delegate);
		
		jStage = delegate;
		delegate.title = title;
		delegate.setScene(scene().delegate);
	}
	
}


shared abstract class StageStyle(shared JStageStyle delegate)
		of decorated|transparent|undecorated|utility {}

"Defines a normal Stage style with a solid white background and platform decorations."
shared object decorated extends StageStyle(JStageStyle.\iDECORATED) {}

"Defines a Stage style with a transparent background and no decorations."
shared object transparent extends StageStyle(JStageStyle.\iTRANSPARENT) {}

"Defines a Stage style with a solid white background and no decorations."
shared object undecorated extends StageStyle(JStageStyle.\iUNDECORATED) {}

"Defines a Stage style with a solid white background and minimal platform decorations used for a utility window."
shared object utility extends StageStyle(JStageStyle.\iUTILITY) {}
