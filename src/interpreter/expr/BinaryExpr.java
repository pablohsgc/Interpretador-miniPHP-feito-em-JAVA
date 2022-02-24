package interpreter.expr;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

public class BinaryExpr extends Expr{
    private Expr left;
    private BinaryOp op;
    private Expr right;

    public BinaryExpr(int line, Expr left, BinaryOp op, Expr right){
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Value<?> expr() throws Exception{
        int v1 = 0,v2 = 0;
        if(this.op != BinaryOp.ConcatOp){
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
        }
        
        switch (this.op) {
            case AddOp:
                return new IntegerValue(v1 + v2);
            case SubOp:
                return new IntegerValue(v1 - v2);
            case ConcatOp:
                String s = String.valueOf(this.left.expr().value()) + this.right.expr().value(); // transforma v1 em string para concaternar com v0
                try{
                    return new IntegerValue(Integer.parseInt(s));//Converte para inteiro e retorna IntegerValue
                }catch(NumberFormatException e){
                    return new StringValue(s);    
                }
            case MulOp:
                return new IntegerValue(v1 * v2);
            case DivOp:
                return new IntegerValue(v1 / v2);
            case ModOp:
                return new IntegerValue(v1 % v2);
            default:
                return null;
        }
    }
}