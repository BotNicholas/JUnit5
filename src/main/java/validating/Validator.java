package validating;

public interface Validator<T> {
    Boolean isValid(T object);
}
