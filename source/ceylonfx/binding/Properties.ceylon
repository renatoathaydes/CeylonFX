import javafx.beans.\ivalue { ObservableValue }
import java.lang { JBool=Boolean, JString=String, JFloat=Float, JInt=Integer }
import javafx.beans.property {
	JBooleanProp=BooleanProperty,
	JStringProp=StringProperty,
	JIntegerProp=IntegerProperty,
	JFloatProp=FloatProperty
}
import ceylonfx.utils { fromJavaBool, booleanJ2C, stringJ2C, integerJ2C, floatJ2C }
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
	get() => fromJavaBool(delegate.\ivalue);
	addListener(CeylonListener<Boolean> listener) =>
		delegate.addListener(convert(listener, booleanJ2C));
}

shared class WritableBooleanProperty(JBooleanProp delegate)
		satisfies FxMutable<Boolean> {
	set(Boolean prop) => delegate.set(prop);
}

shared class StringProperty(shared ObservableValue<JString> delegate)
		satisfies FxProperty<String> {
	get() => delegate.\ivalue.string;
	addListener(CeylonListener<String> listener) =>
		delegate.addListener(convert(listener, stringJ2C));
}

shared class WritableStringProperty(JStringProp delegate)
		extends StringProperty(delegate) satisfies FxMutable<String> {
	set(String prop) => delegate.setValue(prop);
}

shared class IntegerProperty(shared ObservableValue<JInt> delegate)
		satisfies FxProperty<Integer> {
	get() => delegate.\ivalue.intValue();
	addListener(CeylonListener<Integer> listener) =>
		delegate.addListener(convert(listener, integerJ2C));
}

shared class WritableIntegerProperty(JIntegerProp delegate)
		extends IntegerProperty(fromProperty(delegate)) satisfies FxMutable<Integer> {
	set(Integer prop) => delegate.set(prop);
}

shared class FloatProperty(shared ObservableValue<JFloat> delegate)
		satisfies FxProperty<Float> {
	get() => delegate.\ivalue.floatValue();
	addListener(CeylonListener<Float> listener) =>
		delegate.addListener(convert(listener, floatJ2C));
}

shared class WritableFloatProperty(JFloatProp delegate)
		extends FloatProperty(fromProperty(delegate)) satisfies FxMutable<Float> {
	set(Float prop) => delegate.set(prop);
}

