package interpreter.expr;

import interpreter.value.ArrayValue;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;
import java.util.Map;

public class AccessExpr extends SetExpr{
    private Expr base;
    private Expr index;

    public AccessExpr(int line, Expr base, Expr index) {
        super(line);
        this.base = base;
        this.index = null;
    }

    @Override
    public void setExpr(Value<?> value) throws Exception { //a função setExpr(Value<?>) salva esse valor na expressão (base + índice se houver).
        System.out.println("setExpr Access");
        Value<?> v1 = base.expr();
        Value<?> v2 = index.expr();
        
        if(v1 instanceof ArrayValue){
            ArrayValue av = (ArrayValue)v1;
            Map<String, Value<?>> mapa = av.value();
            if(v2 instanceof StringValue){
                StringValue sv = (StringValue)v2;
                String s = sv.value();
                mapa.put(s, value);
            } else if(v2 instanceof IntegerValue){
                IntegerValue iv = (IntegerValue)v2;
                String s2 = iv.value().toString();
                mapa.put(s2, value);
            }
        }
    }

    @Override
    public Value<?> expr() throws Exception{ //obter o valor (Value<?>) da expressão do lado direito da igualdade e 
                             //retornar um Value<?> computado para essa expressão (base + índice se houver).
        Value<?> v1 = base.expr();
        Value<?> v2 = index.expr();
                          
        if(index == null){
            return v1;
        }else{
            if(v1 instanceof ArrayValue){
                ArrayValue av = (ArrayValue) v1;
                Map<String, Value<?>> mapa = av.value();
                if(v2 instanceof StringValue){
                    StringValue sv = (StringValue)v2;
                    String s = sv.value();
                    Value<?> v = mapa.get(s);
                    if(v == null){ //se não tem indice
                        return new StringValue("");
                    }else{
                        return v;
                    }
                }
                else if(v2 instanceof IntegerValue){
                    IntegerValue iv = (IntegerValue)v2;
                    String s2 = iv.value().toString();
                    Value<?> val = mapa.get(s2);
                    if(val == null){
                        Integer integer = null;
                        return new IntegerValue(integer);
                    }else{
                        return val;
                    }
                }
            }
        }
        return null; //apenas para a IDE não reclamar de falta de retorno 
    }
}