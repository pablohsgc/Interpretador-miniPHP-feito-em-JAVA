package interpreter.expr;

import interpreter.value.IntegerValue;
import interpreter.value.StringValue;

public class SingleBoolExpr extends BoolExpr{
    private Expr left;
    private RelOp op;
    private Expr right;

    public SingleBoolExpr(int line, Expr left, RelOp op, Expr right){
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;        
    }

    @Override
    public boolean expr() throws Exception{
        int v1 = 0,v2 = 0;

        if(this.left.expr() instanceof IntegerValue || this.left.expr() instanceof IntegerValue){
            v1 = (Integer) this.left.expr().value();
            v2 = (Integer) this.right.expr().value();
        }else if(this.left.expr() instanceof StringValue || this.left.expr() instanceof StringValue ){
            String var1,var2;
            var1 = String.valueOf(this.left.expr().value());
            var2 = String.valueOf(this.right.expr().value());
            try{
                v1 = Integer.parseInt(var1);
                v2 = Integer.parseInt(var2);
            }catch(NumberFormatException e){
                throw new Exception("Erro: Tipo de operadores incorreto!");
            }
        }else{
            throw new Exception("Erro: Tipo de operadores incorreto!");
        }

        switch (op) {
            case Equal:
                return v1 == v2;
            case NotEqual:
                return v1 != v2;
            case LowerThan:
                return v1 < v2;
            case LowerEqual:
                return v1 <= v2;
            case GreaterThan:
                return v1 > v2;
            case GreaterEqual:
                return v1 >= v2;
            default:
                return false;
        }
    }

}