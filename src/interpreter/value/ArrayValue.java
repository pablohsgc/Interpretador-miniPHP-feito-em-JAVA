package interpreter.value;

import interpreter.expr.Expr;
import java.util.Map;
import java.util.HashMap;

public class ArrayValue extends CompositeValue<Map<String, Value<?>>> {

  private Map<String,Value<?>> value;

  public ArrayValue(Map<String, Value<?>> value) {
    this.value = new HashMap<String,Value<?>>(value);
  }

  @Override
  public Map<String,Value<?>> value() {
    return value;
  }
}
