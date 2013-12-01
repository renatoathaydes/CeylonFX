import ceylonfx.application.java {
	CeylonListener
}

shared class MutableProperty<in Prop>(shared void setValue(Prop prop)) {}

shared void binding<From, To>(
	FxProperty<From> bindable,
	FxMutable<To> toUpdate,
	To(From) transform) {
	
	object listener satisfies CeylonListener<From> {
		shared actual void onChange(From? oldValue, From? newValue) {
			if (exists newValue) {
				toUpdate.set(transform(newValue));	
			}
		}
	}
	bindable.addListener(listener);
}
