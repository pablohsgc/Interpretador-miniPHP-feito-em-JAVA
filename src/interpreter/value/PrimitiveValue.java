package interpreter.value;

public abstract class PrimitiveValue<T> extends Value<T> {

  protected PrimitiveValue() {
  }

  public abstract T value();
}
