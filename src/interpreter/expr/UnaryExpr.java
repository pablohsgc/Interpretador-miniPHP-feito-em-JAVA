package interpreter.expr;

import interpreter.value.Value;
import interpreter.expr.UnaryOp;
import interpreter.value.IntegerValue;

public class UnaryExpr extends Expr{
    private Expr expr;
    private UnaryOp op;

    public UnaryExpr(int line, Expr expr, UnaryOp op){
        super(line);
        this.expr = expr;
        this.op = op;
    }

    @Override
    public Value<?> expr() throws Exception{ // Verificar se é necessario exceção
        String valor = String.valueOf(this.expr.expr().value());
        int n = Integer.parseInt(valor);
        IntegerValue v;
        IntegerValue retorno = null,posRetorno;
        switch (op) {
            case PreIncOp: //soma e depois atribui
                v =  new IntegerValue(++n);
                retorno = v;
                ((SetExpr)this.expr).setExpr(retorno);
                break;
            case PreDecOp: //sub e depois atribui
                v =  new IntegerValue(--n);
                retorno = v;
                ((SetExpr)this.expr).setExpr(retorno);
                break;
            case PosIncOp: //atribui e depois soma
                retorno = new IntegerValue(n);
                posRetorno =  new IntegerValue(++n);
                ((SetExpr)this.expr).setExpr(posRetorno);
                break;
            case PosDecOp: //atribui e depois sub
                retorno = new IntegerValue(n);
                posRetorno =  new IntegerValue(--n);
                ((SetExpr)this.expr).setExpr(posRetorno);
                break;
            default:
                throw new Exception("Operacao invalida!");
        }
        
        return retorno;
    }

}