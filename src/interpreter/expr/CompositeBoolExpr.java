package interpreter.expr;

public class CompositeBoolExpr extends BoolExpr {
    private BoolExpr left;
    private BoolOp op;
    private BoolExpr right;

    public CompositeBoolExpr(int line, BoolExpr left, BoolOp op, BoolExpr right){
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public int getLine(){
       return this.line; //resolver retorno
    }
    
    @Override
    public boolean expr() throws Exception{
        if(op == BoolOp.And)
           return left.expr() && right.expr();
        return left.expr() || right.expr();
    } 
}