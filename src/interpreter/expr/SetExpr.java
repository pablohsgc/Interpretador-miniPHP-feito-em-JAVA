package interpreter.expr;
import interpreter.value.Value;
import java.util.Map;
import java.util.HashMap;

public abstract class SetExpr extends Expr{
    private Map<Expr, Expr> array;

    protected SetExpr(int line){
        super(line);
    }

    public abstract Value<?> expr() throws Exception;

    public abstract void setExpr(Value<?> value) throws Exception;

}