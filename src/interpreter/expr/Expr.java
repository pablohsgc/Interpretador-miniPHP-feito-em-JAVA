package interpreter.expr;
import interpreter.value.Value;
import java.util.Map;
import java.util.HashMap;

public abstract class Expr{
    public int line;
   
    protected Expr(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    public abstract Value<?> expr() throws Exception;

}