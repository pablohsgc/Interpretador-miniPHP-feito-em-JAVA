package interpreter.command;

import java.util.HashMap;
import java.util.Map;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.expr.Variable;
import interpreter.value.ArrayValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class ForeachCommand extends Command{
    private Expr expr;
    private Variable key;
    private Variable value;
    private Command cmds;

    
    public ForeachCommand(int line, Expr expr, Command cmds, Variable key, Variable value){
        super(line);
        this.expr = expr;
        this.cmds = cmds;
        this.key = key;
        this.value = value;
    }
    
    @Override
    public void execute() throws Exception{ //foreach ($a as $x => $y) 
    //Para cada par(chave, valor) que está guardado no mapa, vc vai ter que setar a chave na variável $x e setar o valor na variável $y. 
        
        Value<?> v = expr.expr();
        
        if(v instanceof ArrayValue){
            ArrayValue av = (ArrayValue) v;
            Map<String, Value<?>> mapa = av.value();
            
            for(Map.Entry<String, Value<?>> aux : mapa.entrySet()){
                String s = aux.getKey();
                Value<?> val = aux.getValue();
                
                key.setExpr(new StringValue(s));
                
                if(value != null){
                   value.setExpr(val);
                }
                
                cmds.execute();
            }
        }else{
            throw new Exception("Valor incorreto para executar array!");
        }
    } 
}