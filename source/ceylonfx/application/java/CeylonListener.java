package ceylonfx.application.java;

public interface CeylonListener<T> {

	void onChange(T oldValue, T newValue);

}
