import java.lang { Runnable, Bool=Boolean, JString=String, JFloat=Float, JInt=Integer }
import javafx.application { Platform }
import java.util.concurrent { CountDownLatch }
import ceylonfx.application.java { TypeConverter }
import ceylon.interop.java { javaString }

shared object booleanC2J satisfies TypeConverter<Boolean, Bool> {
	shared actual Bool convert(Boolean from) => from then Bool.\iTRUE else Bool.\iFALSE;
}

shared object booleanJ2C satisfies TypeConverter<Bool, Boolean> {
	shared actual Boolean convert(Bool from) => from === Bool.\iTRUE then true else false;
}

shared object stringC2J satisfies TypeConverter<String, JString> {
	shared actual JString convert(String from) => javaString(from);
}

shared object stringJ2C satisfies TypeConverter<JString, String> {
	shared actual String convert(JString from) => from.string;
}

shared object integerC2J satisfies TypeConverter<Integer, JInt> {
	shared actual JInt convert(Integer from) => JInt(from);
}

shared object integerJ2C satisfies TypeConverter<JInt, Integer> {
	shared actual Integer convert(JInt from) => from.intValue();
}

shared object floatC2J satisfies TypeConverter<Float, JFloat> {
	shared actual JFloat convert(Float from) => JFloat(from);
}

shared object floatJ2C satisfies TypeConverter<JFloat, Float> {
	shared actual Float convert(JFloat from) => from.floatValue();
}

shared TypeConverter<JavaType, CeylonType> asTypeConverter<JavaType, CeylonType>(
		CeylonType transform(JavaType? from)) {
	object converter satisfies TypeConverter<JavaType, CeylonType> { 
		shared actual CeylonType convert(JavaType? from) => transform(from);
	}
	return converter;
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
