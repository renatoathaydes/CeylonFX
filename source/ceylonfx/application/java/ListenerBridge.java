package ceylonfx.application.java;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ListenerBridge {

	public static <T> ChangeListener<T> convert(final CeylonListener<T> listener) {
		return new ChangeListener<T>() {
			@Override
			public void changed(ObservableValue<? extends T> obs, T oldValue, T newValue) {
				listener.onChange(oldValue, newValue);
			}
		};
	}

}
