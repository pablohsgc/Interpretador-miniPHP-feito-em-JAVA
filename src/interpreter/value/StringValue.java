package interpreter.value;

public class StringValue extends PrimitiveValue<String> {

  private String value;

  public StringValue(String value) {
    this.value = value;
  }
  
  @Override
  public String value() {
    return value;
  }
}
