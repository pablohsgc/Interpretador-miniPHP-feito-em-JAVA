package interpreter.command;
import interpreter.expr.Expr;

public class EchoCommand extends Command{
    private Expr expr;

    public EchoCommand(int line, Expr expr){
        super(line);
        this.expr = expr;
    }
    
    @Override
    public void execute() throws Exception{
        System.out.print(expr.expr().value());
    }
}