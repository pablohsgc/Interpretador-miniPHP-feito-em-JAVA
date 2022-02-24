package interpreter.value;

public class IntegerValue extends PrimitiveValue<Integer> {

  private Integer value;

  public IntegerValue(Integer value) {
    this.value = value;
  }

  @Override
  public Integer value() {
    return value;
  }
}
