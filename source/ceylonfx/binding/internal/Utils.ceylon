import ceylonfx.application.java {
    TypeConverter
}
import ceylonfx.binding {
    Writable
}

import javafx.beans.property {
    ReadOnlyProperty
}

"Used internally by CeylonFX to bind Ceylon properties to their JavaFX counterparts."
shared void bindToJavaFx<in C, out J>(
        ReadOnlyProperty<J> javaProp,
        Writable<C> ceylonProp,
        TypeConverter<J,C> converter)
        given C satisfies Object {
    javaProp.addListener(void(observable, oldValue, newValue) {
        ceylonProp.set(converter.convert(newValue));
    });
}
