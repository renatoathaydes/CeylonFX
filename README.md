# CeylonFX

CeylonFX is a Ceylon interface for JavaFX.

## Development of CeylonFX has been migrated to https://github.com/ceylonfx/ceylonfx

~~ **Development is currently stopped**.

Please contact me if you would like to contribute or even take over this project!

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

### Let Ceylon know about JavaFX

Before you can use CeylonFX, you need to make JavaFX available to Ceylon. To do that, simply download [the zip file containing the JavaFX jar](javafx.zip) conveniently wrapped in a Ceylon repo format, then extract the files into your ``{user.home}/.ceylon/repo/`` folder.

I hope it's ok for me to distribute the JavaFX jar in such way because anyway, JavaFX is mostly open-source and the jar can be downloaded for free (please let me know if you think I can't do this legally and I will remove it immediately).

### Importing the CeylonFX module

Currently, here's what you need to do from the command-line:

```
git clone git@github.com:renatoathaydes/CeylonFX.git
cd CeylonFX
ceylon compile --out +USER ceylonfx
```

Once we have something that is more or less complete, we will try to make this project available from [Ceylon Herd](http://modules.ceylon-lang.org/) so you won't need to do any of this.

>> To run the demo code shown above, just type ``ceylon run ceylonfx``.


You should then add this declaration to your Ceylon module:

```ceylon
import ceylonfx "2.2.0"
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
``` ~~

