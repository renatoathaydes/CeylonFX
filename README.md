#CeylonFX

CeylonFX is a Ceylon interface for JavaFX.

It is currently under active development. Please contact me if you would like to contribute!

Here's a quick example of what code written with CeylonFX looks like *(this example already works)*:

```ceylon
CeylonFX {
	args = process.arguments;
	Stage {
		title = "CeylonFX Demo Application";
		Scene {
			fill = black;
			dimension = [600.0, 150.0];
			VBox {
				spacing = 20;
				insets = Insets { top = 15.0; };
				minimumSize = [600.0, 0.0];
				alignment = center;
				Text {
					text = "Welcome to CeylonFX";
					underline = true;
					font = font("Arial", 48.0);
					fill = LinearGradient { [0.0, blue], [0.75, cyan], [1.0, white] };
				},
				Text {
					text = "Under construction...";
					font = font("Arial", 12.0);
					fill = white;
				}   
			}
		};
	};
};
```

See more [here](samples/samples/run.ceylon)!

## Getting started

### Let Ceylon know about JavaFX

Before you can use CeylonFX, you need to make JavaFX available to Ceylon. To do that, simply download [the zip file containing the JavaFX jar](javafx.zip) conveniently wrapped in a Ceylon repo format, then extract the files into your ``{user.home}/.ceylon/repo/`` folder.

I hope it's ok for me to distribute the JavaFX jar in such way because anyway, JavaFX is mostly open-source and the jar can be downloaded for free (please let me know if you think I can't do this legally and I will remove it immediately).

### Importing the CeylonFX module

Unfortunately, for now you will have to clone this project using git and install it in your local machine with Ceylon tools.

However, once we have something that is more or less complete, we will try to make this project available from [Ceylon Herd](http://modules.ceylon-lang.org/).

You can then add this declaration to your Ceylon module:

```ceylon
import ceylonfx "2.2.0"
```

