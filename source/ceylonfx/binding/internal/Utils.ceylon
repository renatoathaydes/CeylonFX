import ceylonfx.application.java {
	TypeConverter,
	CeylonListener,
	ListenerBridge {
		convert
	}
}
import ceylonfx.binding {
	Writable
}

import javafx.beans.property {
	ReadOnlyProperty
}

"Used internally by CeylonFX to bind Ceylon properties to their JavaFX counterparts."
shared void bindToJavaFx<in C, out J>(ReadOnlyProperty<J> javaProp, Writable<C> ceylonProp, TypeConverter<J, C> converter)
		given C satisfies Object {
	object selectedListener satisfies CeylonListener<C> {
		shared actual void onChange(C? from, C? to) {
			if (exists to) {
				ceylonProp.set(to);
			}
		}
	}
	javaProp.addListener(convert(selectedListener, converter));
}
