import ceylonfx.application {
	CeylonFX
}
import ceylonfx.scene {
	Scene
}
import ceylonfx.scene.layout {
	VBox,
	VGrowNode,
	always
}
import ceylonfx.scene.paint {
	red,
	yellow
}
import ceylonfx.scene.shape {
	Rectangle
}
import ceylonfx.stage {
	Stage
}


"Run the module `samples` by typing, for example, `ceylon run samples vbox`"
shared void run() {
	value samples = { "vbox" -> runVBox };
	
	value args = process.arguments;
	if (exists sampleToRun = args[0],
		exists run = samples.find((String->Anything() entry) => entry.key == sampleToRun.lowercased)) {
		print("Running ``sampleToRun`` sample");
		run.item();
	} else {
		print("You must specify one of the existing samples to run: ``samples.map((String->Anything() entry) => entry.key)``");
	}
	
}

shared void runVBox() {
	CeylonFX {
		Stage {
			title = "VBox sample";
			Scene {
				VBox {
					minimumSize = [500.0, 500.0];
					spacing = 10;
					Rectangle{ dimension = [250.0, 50.0]; fill = red; },
					VGrowNode(Rectangle{ dimension = [250.0, 50.0]; fill = yellow; }, always)
				}	
			};
		};
	};
}
