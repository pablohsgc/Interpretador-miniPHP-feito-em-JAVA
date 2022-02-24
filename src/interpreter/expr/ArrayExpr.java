package interpreter.expr;
import interpreter.value.ArrayValue;
import interpreter.value.Value;
import java.util.HashMap;
import java.util.Map;
import interpreter.value.StringValue;
import interpreter.value.IntegerValue;

public class ArrayExpr extends Expr{
    private Map<Expr, Expr> array; 

    public ArrayExpr(int line){
        super(line);
        this.array = new HashMap<Expr,Expr>();
    }

    public void insert(Expr key, Expr value){   
        array.put(key, value);
    }

    @Override
    public Value<?> expr() throws Exception{ 
        Map<String, Value<?>> mapa = new HashMap<>(); 
        Expr key;
        Expr value;
        
        for (Map.Entry<Expr, Expr> aux : array.entrySet()){
            key = aux.getKey();
            value = aux.getValue();

            Value<?> valueKey = key.expr();
            Value<?> valueValue =  value.expr();
            
            System.out.println(valueKey.value() +"  => "+valueValue.value());
            
            if (valueKey instanceof StringValue){
                mapa.put(String.valueOf(valueKey.value()), valueValue);
            }else if (valueKey instanceof IntegerValue){
                IntegerValue i = (IntegerValue) valueKey;
                mapa.put(Integer.toString(i.value()), valueValue);
            }else{
                System.out.print("Operação Inválida");//Lançar excecao
            }
        }
        
        return new ArrayValue(mapa);
    }
}