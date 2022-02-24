package interpreter.expr;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;
import java.util.Scanner;

public class ReadExpr extends Expr{
    private Expr msg;
    private static Scanner in;
    
    static {
        in = new Scanner(System.in);
    }

    public ReadExpr(int line, Expr msg){
        super(line);
        this.msg = msg;
    }

    @Override
    public Value<?> expr() throws Exception{
        String mensagem = String.valueOf(this.msg.expr().value());
        
        System.out.print(mensagem);
        String input = in.next();
        
        Value<?> retorno;
        
        try{
            retorno = new IntegerValue(Integer.parseInt(input));    
        }catch(NumberFormatException e){
            retorno = new StringValue(input);   
        }
        
        return retorno;
    }
}