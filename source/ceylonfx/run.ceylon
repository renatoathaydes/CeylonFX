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
    Text,
    font
}
import ceylonfx.stage {
    Stage
}

shared void run() 
        => CeylonFX {
    args = ["args", "for", "App"];
    Stage {
        title = "CeylonFX Demo Application";
        Scene {
            fill = black;
            dimension = [600.0, 150.0];
            Text {
                location= [50.0, 50.0];
                text = "Welcome to CeylonFX";
                font = font("Arial", 48.0);
                fill = LinearGradient { [0.0, blue], [0.75, cyan], [1.0, white] };
            },
            Text {
                location=[50.0, 100.0];
                text="Under construction...";
                font=font("Arial", 12.0);
                fill=white;
            }
        };
    };
};
