#CeylonFX

CeylonFX is a Ceylon interface for JavaFX.

It is currently under active development. Please contact me if you would like to contribute!

Here's a quick example of what code written with CeylonFX looks like *(this example already works)*:

```ceylon
value largeFont = Font("Arial", 48.0);
value smallFont = Font("Arial", 12.0);
value mainFill = LinearGradient { [0.0, blue], [1.0, cyan] };

CeylonFX {
	Stage {
		title = "CeylonFX Demo Application";
		Scene {
			fill = black; width = 600.0; height = 150.0;
			Text(50.0, 50.0, "Welcome to CeylonFX", largeFont, mainFill),
			Text(50.0, 100.0, "Under construction...", smallFont, white)
		};
	
	};
};
```
