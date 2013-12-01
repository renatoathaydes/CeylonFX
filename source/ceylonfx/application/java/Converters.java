package ceylonfx.application.java;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * This is necessary because Ceylon does not respect Java's generics completely.
 * Also, due to JavaFX's stupid design, an IntegerProperty, for example, is an
 * instance of ObservableValue<? extends Number> but not
 * ObservableValue<Integer>.
 * 
 * @author renato
 */
public class Converters {

	public static ObservableValue<Boolean> fromProperty(BooleanProperty property) {
		return property;
	}

	public static ObservableValue<String> fromProperty(StringProperty property) {
		return property;
	}

	public static ObservableValue<Float> fromProperty(final FloatProperty property) {
		return new ObservableValue<Float>() {
			@Override
			public void addListener(InvalidationListener listener) {
				property.addListener(listener);
			}

			@Override
			public void removeListener(InvalidationListener listener) {
				property.removeListener(listener);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void addListener(ChangeListener<? super Float> listener) {
				property.addListener((ChangeListener<? super Number>) listener);
			}

			@Override
			public Float getValue() {
				return property.getValue();
			}

			@SuppressWarnings("unchecked")
			@Override
			public void removeListener(ChangeListener<? super Float> listener) {
				property.removeListener((ChangeListener<? super Number>) listener);
			}
		};
	}

	public static ObservableValue<Integer> fromProperty(final IntegerProperty property) {
		return new ObservableValue<Integer>() {
			@Override
			public void addListener(InvalidationListener listener) {
				property.addListener(listener);
			}

			@Override
			public void removeListener(InvalidationListener listener) {
				property.removeListener(listener);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void addListener(ChangeListener<? super Integer> listener) {
				property.addListener((ChangeListener<? super Number>) listener);
			}

			@Override
			public Integer getValue() {
				return property.getValue();
			}

			@SuppressWarnings("unchecked")
			@Override
			public void removeListener(ChangeListener<? super Integer> listener) {
				property.removeListener((ChangeListener<? super Number>) listener);
			}
		};
	}

	public static <T> T valueOf(ObjectProperty<T> property) {
		return property.get();
	}

}
