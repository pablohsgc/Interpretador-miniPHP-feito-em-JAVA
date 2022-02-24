package interpreter.expr;
import interpreter.value.Value;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;

public class ConstExpr extends Expr{
    private Value<?> value;

    public ConstExpr(int line, Value<?> value){
        super(line);
        this.value = value;
    }

    @Override
    public Value<?> expr(){
        if(value instanceof StringValue)
            return ((StringValue)value);
        return value;
    }
}