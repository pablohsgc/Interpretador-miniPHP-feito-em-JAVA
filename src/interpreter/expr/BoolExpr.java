package interpreter.expr;

public abstract class BoolExpr{
    protected int line; //deve ser protected para que os filhos possam acessar ela

    public BoolExpr(int line){
        this.line = line; 
    }

    public BoolExpr(){

    }

    public int getLine(){
        return this.line;
    }

    public abstract boolean expr() throws Exception;
}