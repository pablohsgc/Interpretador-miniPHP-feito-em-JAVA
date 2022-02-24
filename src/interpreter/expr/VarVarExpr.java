package interpreter.expr;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class VarVarExpr extends SetExpr{
    private Expr varvar;

    public VarVarExpr(int line, Expr varvar){
        super(line);
        this.varvar = varvar;
    }
    //<varvar> ::= '$' <varvar> | <var>
    @Override
    public void setExpr(Value<?> value) throws Exception{
        Value<?> v1 = varvar.expr();
        StringValue sv = (StringValue)v1;
        if(v1 instanceof StringValue){
            String s = sv.value();
            Variable var = Variable.instance("$"+s);
            if(var == null){
                var = Variable.instance("");//se a variavel n existir, criar com string vazia
            }
            Value<?> v2 = var.expr();
            var.setExpr(value);//trocar o valor da varialve
        }
    }
    
    @Override
    public Value<?> expr() throws Exception{
        Value<?> value1 = varvar.expr();
        
        if(value1 instanceof StringValue){
            StringValue sv = (StringValue)value1;
            String s = sv.value();
            Variable var = Variable.instance("$"+s);
            if(var == null){
                var = Variable.instance("");//se a variavel n existir, criar com string vazia
            }
            Value<?> value2 = var.expr();
            return value2;
        }
        else{
            return new StringValue(""); //retornar string vazia;
        }
    }

}