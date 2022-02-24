package interpreter.expr;

public class NotBoolExpr extends BoolExpr {

    private BoolExpr expr;

    public NotBoolExpr(int line, BoolExpr expr) {
        super(line);
        this.expr = expr;
    }

    public int getLine(){
        return this.line;
    }

    public boolean expr() throws Exception{
        return !expr.expr();
    }   

}