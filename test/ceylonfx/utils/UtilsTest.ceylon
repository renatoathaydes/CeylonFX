import ceylon.test { ... }
import ceylonfx.application { CeylonFX }
import ceylonfx.stage { Stage }
import ceylonfx.scene { Scene }
import javafx.scene.shape { Rectangle }
import javafx.scene.paint { Color }


test void canRunInFxThread() {
	CeylonFX {
		Stage {
			Scene {
				Rectangle(50.0, 50.0, Color.\iBLUE)
			};
		};
	};
}
