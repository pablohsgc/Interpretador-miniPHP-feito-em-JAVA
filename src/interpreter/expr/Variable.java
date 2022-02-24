package interpreter.expr;

import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;
import java.util.HashMap;
import java.util.Map;

public class Variable extends SetExpr{
    private static Map<String, Variable> variables;
    private String name;
    private Value<?> value;

    static {
        variables = new HashMap<String,Variable>();
    }
    private Variable(String name) {
        super(-1);
        this.name = name;
        this.value = null;
    }

    public static Variable instance(String name){
        Variable v = variables.get(name);
        if (v == null) {
            v = new Variable(name);
            variables.put(name, v);
        }
        
        return v;
    }

    public String getName() {
        return name;
    }

    @Override
    public Value<?> expr() {
        return value;
    }

    @Override
    public void setExpr(Value<?> value){
       this.value = value;
    }

}