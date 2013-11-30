import ceylonfx.application.java {
	CeylonListener,
	ListenerBridge {
		convert
	}
}
import ceylonfx.utils {
	fromJavaBool
}

import java.lang {
	Bool=Boolean
}

import javafx.beans.property {
	BooleanProperty,
	StringProperty
}

shared void binding(<[BooleanProperty, StringProperty]->String(Boolean)> bind) {
	value bindable = bind.key[0];
	value toUpdate = bind.key[1];
	value updateFunction = bind.item;
	object listener satisfies CeylonListener<Bool> {
		shared actual void onChange(Bool? oldValue, Bool? newValue) {
			toUpdate.setValue(updateFunction(fromJavaBool(newValue)));
		}
	}
	bindable.addListener(convert(listener));
}
