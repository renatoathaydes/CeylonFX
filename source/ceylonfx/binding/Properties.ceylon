import javafx.beans.\ivalue { ObservableValue }
import java.lang { JBool=Boolean, JString=String, JFloat=Float, JInt=Integer }
import javafx.beans.property {
	JBooleanProp=BooleanProperty,
	JStringProp=StringProperty,
	JIntegerProp=IntegerProperty,
	JFloatProp=FloatProperty
}
import ceylonfx.utils { fromJavaBool, booleanJ2C }
import ceylonfx.application.java { CeylonListener, Converters { fromProperty }, ListenerBridge {
		convert
	}
}

shared interface FxProperty<Prop> {
	shared formal Prop get();
	//shared formal ObservableValue<JavaProp> delegate;
	shared formal void addListener(CeylonListener<Prop> listenr);
}

shared interface FxMutable<in Prop> {
	shared formal void set(Prop prop);
}

shared class BooleanProperty(shared ObservableValue<JBool> delegate)
		satisfies FxProperty<Boolean> {
	shared actual Boolean get() => fromJavaBool(delegate.\ivalue);

	shared actual void addListener(CeylonListener<Boolean> listener) {
		delegate.addListener(convert(listener, booleanJ2C));
	}
	
}

shared class WritableBooleanProperty(JBooleanProp delegate)
		satisfies FxMutable<Boolean> {
	shared actual void set(Boolean prop) {
		delegate.set(prop);
	}	
}

shared class StringProperty(shared ObservableValue<JString> delegate)
		satisfies FxProperty<String> {
	shared actual String get() => delegate.\ivalue.string;

	shared actual void addListener(CeylonListener<String> listenr) {}
	
}

shared class WritableStringProperty(JStringProp delegate)
		extends StringProperty(delegate) satisfies FxMutable<String> {
	shared actual void set(String prop) {
		delegate.setValue(prop);
	}
}

shared class IntegerProperty(shared ObservableValue<JInt> delegate)
		satisfies FxProperty<Integer> {
	shared actual Integer get() => delegate.\ivalue.intValue();

	shared actual void addListener(CeylonListener<Integer> listenr) {}
	
}

shared class WritableIntegerProperty(JIntegerProp delegate)
		extends IntegerProperty(fromProperty(delegate)) satisfies FxMutable<Integer> {
	shared actual void set(Integer prop) {
		delegate.set(prop);
	}
}

shared class FloatProperty(shared ObservableValue<JFloat> delegate)
		satisfies FxProperty<Float> {
	shared actual Float get() => delegate.\ivalue.floatValue();

	shared actual void addListener(CeylonListener<Float> listenr) {}
	
}

shared class WritableFloatProperty(JFloatProp delegate)
		extends FloatProperty(fromProperty(delegate)) satisfies FxMutable<Float> {
	shared actual void set(Float prop) {
		delegate.set(prop);
	}
}

