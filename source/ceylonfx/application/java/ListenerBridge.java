package ceylonfx.application.java;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ListenerBridge {

	public static <J, C> ChangeListener<J> convert(final CeylonListener<C> listener, final TypeConverter<J, C> typeConverter) {
		return new ChangeListener<J>() {
			@Override
			public void changed(ObservableValue<? extends J> obs, J oldValue, J newValue) {
				listener.onChange(typeConverter.convert(oldValue), typeConverter.convert(newValue));
			}
		};
	}

}
