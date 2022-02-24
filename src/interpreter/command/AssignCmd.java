package interpreter.command;
import interpreter.expr.Expr;
import interpreter.expr.SetExpr;
import interpreter.expr.Variable;
import interpreter.value.IntegerValue;
import interpreter.value.StringValue;

//Atribuir o resultado de uma expressão inteira em uma variável
public class AssignCmd extends Command{
    private Expr left;
    private AssignOp op;
    private Expr right;

    public AssignCmd(int line, Expr left, AssignOp op, Expr right) {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right ;
    }
    
    @Override
    public void execute()throws Exception{
        int v1 = 0,v2 = 0;
        IntegerValue v;
        
        if(op != AssignOp.StdAssignOp && op != AssignOp.ConcatAssignOp && op != AssignOp.NoAssignOp){
           if(this.left.expr() instanceof IntegerValue && this.right.expr() instanceof IntegerValue){//Verifica se for inteiro e faz casting direto
                v1 = (Integer) this.left.expr().value();
                v2 = (Integer) this.right.expr().value();
            }else if(this.left.expr() instanceof StringValue || this.right.expr() instanceof StringValue){// Se algum deles for string,converte os dois para string
                String var1,var2;
                var1 = String.valueOf(this.left.expr().value());
                var2 = String.valueOf(this.right.expr().value());
                try{ // Aproveitando que ambos são string é possivel tentar converter para inteiro. 
                    v1 = Integer.parseInt(var1);
                    v2 = Integer.parseInt(var2);
                }catch(NumberFormatException e){ // Caso algum dos valores seja string entao excecao deve ser lancada.
                    throw new Exception("Erro: Tipo de Operadores incorreto!");
                }
            }else{ // Caso seja um array nao sera possivel fazer as operacoes de inteiros.
               throw new Exception("Erro: Tipo de Operadores incorreto!");
           }
        }
        
        switch(op){
            case StdAssignOp: // expr1 = expr2
               ((SetExpr)this.left).setExpr(this.right.expr());
               break;
            case AddAssignOp: // var1 = var1 + var2
               v = new IntegerValue(v1+v2);
               ((SetExpr)this.left).setExpr(v);
               break;
            case SubAssignOp: // var1 = var1 - var2 
                v = new IntegerValue(v1-v2);
               ((SetExpr)this.left).setExpr(v);
               break;
            case ConcatAssignOp: // var1 = var . var2
               String resultado = String.valueOf(this.left.expr().value()) + this.right.expr().value();
               
               try{ // Tenta converter para inteiro se possivel o valor torna inteiro
                    v = new IntegerValue(Integer.parseInt(resultado));
                    ((SetExpr)this.left).setExpr(v);
               }catch(NumberFormatException e){ // Caso seja uma string será escrito como string
                   StringValue s = new StringValue(resultado);
                   ((SetExpr)this.left).setExpr(s);
               }
               break;
            case MulAssignOp: // var1 = var1 * var2 
               v = new IntegerValue(v1*v2);
               ((SetExpr)this.left).setExpr(v);
               break;
            case DivAssignOp: // var1 = var1 / var2 
               v = new IntegerValue(v1/v2);
               ((SetExpr)this.left).setExpr(v);
               break;
            case ModAssignOp: // var1 = var1 % var2 
               v = new IntegerValue(v1%v2);
               ((SetExpr)this.left).setExpr(v);
               break;
            default:
                this.left.expr();//Chama a instrucao interna que nao possui atribuicao
        }
    }
    
}