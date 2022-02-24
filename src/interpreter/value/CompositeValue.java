package interpreter.value;

public abstract class CompositeValue<T> extends Value<T> {

  protected CompositeValue() {
  }

  public abstract T value();
}
