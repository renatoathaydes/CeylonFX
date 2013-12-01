import java.lang { Runnable, Bool=Boolean }
import javafx.application { Platform }
import java.util.concurrent { CountDownLatch }
import ceylonfx.application.java { TypeConverter }

shared object booleanC2J satisfies TypeConverter<Boolean, Bool> {
	shared actual Bool convert(Boolean from) => from then Bool.\iTRUE else Bool.\iFALSE;
}

shared object booleanJ2C satisfies TypeConverter<Bool, Boolean> {
	shared actual Boolean convert(Bool from) => from === Bool.\iTRUE then true else false;
}

shared Value? doInFxThread<Value>(Value toRun(Object* args)) {
	variable Value? result = null;
	CountDownLatch latch = CountDownLatch(1);
	object runnable satisfies Runnable {
		shared actual void run() {
			result = toRun();
			latch.countDown();
		}
	}
	Platform.runLater(runnable);
	latch.await();
	return result;
}

shared Runnable asRunnable(Anything toRun(Object* args)) {
	object runnable satisfies Runnable {
		shared actual void run() { toRun(); }
	}
	return runnable;
}

shared Boolean fromJavaBool(Bool? boolean) {
	if (exists b = boolean) {
		return b.booleanValue();
	}
	return false;
}
