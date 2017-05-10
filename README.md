# CeylonFX

CeylonFX is a Ceylon interface for JavaFX.

> STATUS: This project is in early stages and is not currently ready for production.
  Want to help get it ready? Make a pull request or contact me to know how to best contribute!

Here's a quick example of what code written with CeylonFX looks like *(this example already works)*:

```ceylon
Application {
	args = process.arguments;
	Stage {
		title = "CeylonFX Demo Application";
		() => Scene {
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

### Importing the CeylonFX module

Currently, here's what you need to do from the command-line:

```
git clone git@github.com:renatoathaydes/CeylonFX.git
cd CeylonFX
ceylon compile --out +USER ceylonfx
```

Once we have something that is more or less complete, we will try to make this project available from [Ceylon Herd](http://modules.ceylon-lang.org/) so you won't need to do any of this.

> To run the demo code shown above, just type ``ceylon run ceylonfx``.


You should then add this declaration to your Ceylon module:

```ceylon
import ceylonfx "0.2.0"
```

And that's it! You're ready to start using CeylonFX in your project.


## CeylonFX samples

A few basic CeylonFX samples can be found in the [samples](samples/samples/) directory.

To compile the samples module, go to the CeylonFX root directory and type:

```
ceylon compile --src samples/ samples
```

To see a list of available samples, just type:

```
ceylon run samples
```


To run a sample, ``checkbox`` for example, run:

```
ceylon run samples checkbox
```

