package ceylonfx.application.java;

public interface TypeConverter<From, To> {
	public To convert(From from);
}