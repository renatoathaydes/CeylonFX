import ceylonfx.application {
	CeylonFX
}
import ceylonfx.binding {
	Binding, bindConverting
}
import ceylonfx.scene {
	Scene
}
import ceylonfx.scene.control {
	CheckBox
}
import ceylonfx.scene.layout {
	VBox,
	VGrowNode,
	always
}
import ceylonfx.scene.paint {
	red,
	yellow,
	black,
	white
}
import ceylonfx.scene.shape {
	Rectangle
}
import ceylonfx.stage {
	Stage
}


"Run the module `samples` by typing, for example, `ceylon run samples vbox`"
shared void run() {
	value samples = { "vbox" -> vBox, "checkbox" -> checkBox };
	
	value args = process.arguments;
	if (exists sampleToRun = args[0],
		exists run = samples.find((String->Anything() entry) => entry.key == sampleToRun.lowercased)) {
		print("Running ``sampleToRun`` sample");
		run.item();
	} else {
		print("You must specify one of the existing samples to run: ``samples.map((String->Anything() entry) => entry.key)``");
	}
	
}

void vBox() {
	CeylonFX {
		Stage {
			title = "VBox sample";
			Scene {
				VBox {
					minimumSize = [500.0, 500.0];
					spacing = 10;
					Rectangle {
						dimension = [250.0, 50.0];
						fill = red;
					},
					VGrowNode {
						vgrow=always;
						node=Rectangle{
							dimension = [250.0, 50.0]; 
							fill = yellow;
						}; 
					},
					Rectangle {
						dimension = [250.0, 50.0]; 
						fill = red;
					}
				}
			};
		};
	};
}

void checkBox() {
	value checkBox = CheckBox();
	
	bindConverting(checkBox.selectedProperty, checkBox.textFillProperty,
		(Boolean sel) => sel then black else yellow);
	bindConverting(checkBox.selectedProperty, checkBox.textProperty,
		(Boolean sel) => sel then "Change to black theme" else "Change to white theme");
	
	CeylonFX {
		Stage {
			title = "VBox sample";
			Scene {
				fill = Binding(checkBox.selectedProperty -> 
						((Boolean sel) => sel then white else black));
				checkBox
			};
		};
	};
}


