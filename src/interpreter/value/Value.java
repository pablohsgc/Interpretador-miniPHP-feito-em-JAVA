package interpreter.value;

public abstract class Value<T> {

  protected Value() {
    
  }

  public abstract T value();

    public Iterable<Value<?>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

  
  
