import ceylon.test {
	...
}

import ceylonfx.application {
	Application
}
import ceylonfx.scene {
	Scene
}
import ceylonfx.scene.paint {
	red
}
import ceylonfx.scene.shape {
	Rectangle
}
import ceylonfx.stage {
	Stage
}

import java.util.concurrent {
	CountDownLatch,
	TimeUnit
}

import javafx.application {
	Platform
}


test void canRunInFxThreadBlocking() {
	value app = Application {
		Stage {
			() => Scene {
				Rectangle {
					dimension = [300.0, 300.0];
					fill = red;
				}
			};
		};
	};
	
	value taskCounter = CountDownLatch(1);
	span( void() {
		doInFxThread(() => taskCounter.countDown());
	});
	
	value taskCompleted = taskCounter.await(5, TimeUnit.\iSECONDS);
	Platform.runLater(asRunnable((Object* args) => app.close()));
	
	assertTrue(taskCompleted);
}

test void testNullSafeEquals() {
	assertTrue(nullSafeEquals(null, null));
	assertTrue(nullSafeEquals(1, 1));
	assertTrue(nullSafeEquals("", ""));
	assertTrue(nullSafeEquals(false, false));
	assertTrue(nullSafeEquals(0.0, 0.0));
	
	assertFalse(nullSafeEquals(0, null));
	assertFalse(nullSafeEquals(null, 0));
	assertFalse(nullSafeEquals(0, 1));
	assertFalse(nullSafeEquals("", "a"));
	assertFalse(nullSafeEquals(0.1, 1.0));
}
