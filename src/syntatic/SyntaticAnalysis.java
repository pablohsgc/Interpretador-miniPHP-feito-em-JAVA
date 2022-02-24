package syntatic;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import lexical.Lexeme;
import lexical.TokenType;
import lexical.LexicalAnalysis;
import lexical.LexicalException;

import interpreter.command.Command;
import interpreter.command.AssignCmd;
import interpreter.command.AssignOp;
import interpreter.command.BlocksCommand;
import interpreter.command.EchoCommand;
import interpreter.command.ForeachCommand;
import interpreter.command.IfCommand;
import interpreter.command.WhileCommand;


import interpreter.value.ArrayValue;
import interpreter.value.CompositeValue;
import interpreter.value.IntegerValue;
import interpreter.value.PrimitiveValue;
import interpreter.value.StringValue;
import interpreter.value.Value;

import interpreter.expr.AccessExpr;
import interpreter.expr.ArrayExpr;
import interpreter.expr.BinaryExpr;
import interpreter.expr.BinaryOp;
import interpreter.expr.BoolExpr;
import interpreter.expr.BoolOp;
import interpreter.expr.CompositeBoolExpr;
import interpreter.expr.ConstExpr;
import interpreter.expr.Expr;
import interpreter.expr.NotBoolExpr;
import interpreter.expr.ReadExpr;
import interpreter.expr.RelOp;
import interpreter.expr.SetExpr;
import interpreter.expr.SingleBoolExpr;
import interpreter.expr.UnaryExpr;
import interpreter.expr.UnaryOp;
import interpreter.expr.Variable;
import interpreter.expr.VarVarExpr;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) throws LexicalException, IOException {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public BlocksCommand start() throws LexicalException, IOException {
        BlocksCommand programa = procCode();
        
        matchToken(TokenType.END_OF_FILE);
	return programa;
    }

    private void matchToken(TokenType type) throws LexicalException, IOException {
        //System.out.println("Match token: " + current.type + " -> " + type + " (\"" + current.token + "\")");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }
	
    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    // <code> ::= { <statement> }
    private BlocksCommand procCode() throws LexicalException, IOException { // Alterar conforme a programação do bloco start
        BlocksCommand bc = new BlocksCommand(-1);
        
        while(current.type == TokenType.IF ||
              current.type == TokenType.WHILE ||
              current.type == TokenType.FOREACH ||
              current.type == TokenType.ECHO ||
              current.type == TokenType.VAR ||
              current.type == TokenType.DOLAR ||
              current.type == TokenType.INC ||
              current.type == TokenType.DEC ||
              current.type == TokenType.OPEN_PAR){ // O ultimo parametro é necessario para variavel-variavel
            bc.addCommand(procStatement());
        }
        
        return bc;
    }

    // <statement> ::= <if> | <while> | <foreach> | <echo> | <assign>
    private Command procStatement() throws LexicalException, IOException {
        Command c = null;
        if(current.type == TokenType.IF){
                c = procIf();
        }else if(current.type == TokenType.WHILE){
                c = procWhile();
        }else if(current.type == TokenType.FOREACH){
                c = procForeach();
        }else if(current.type == TokenType.ECHO){
                c = procEcho();
        }else if(current.type == TokenType.VAR || 
                 current.type == TokenType.DOLAR||
                 current.type == TokenType.INC ||
                 current.type == TokenType.DEC){ // O ultimo parametro é necessário para variável-variável
                c = procAssign();
        }else{
                showError();
        }		
        return c;
    }

    // <if> ::= if '(' <boolexpr> ')' '{' <code> '}'
    //              { elseif '(' <boolexpr> ')' '{' <code> '}' }
    //              [ else '{' <code> '}' ]
    private IfCommand procIf() throws LexicalException, IOException {
        Command thenCmds,elseCmds; 
        matchToken(TokenType.IF);
        int line = lex.getLine();

        matchToken(TokenType.OPEN_PAR);
        BoolExpr cond = procBoolExpr();
        matchToken(TokenType.CLOSE_PAR);
        matchToken(TokenType.OPEN_CUR);
        thenCmds = procCode();
        matchToken(TokenType.CLOSE_CUR);
        
        IfCommand ifCommand = new IfCommand(line, cond, thenCmds);
        while(current.type == TokenType.ELSEIF){
            matchToken(TokenType.OPEN_PAR);
            cond = procBoolExpr();
            matchToken(TokenType.CLOSE_PAR);
            //Será necessario adicionar mais argumentos na classe if comand pra executar um elseif comand e ir adicionando elseifComands
            matchToken(TokenType.OPEN_CUR);
            thenCmds = procCode();
            matchToken(TokenType.CLOSE_CUR);
        }

        if(current.type == TokenType.ELSE){
            matchToken(TokenType.ELSE);
            matchToken(TokenType.OPEN_CUR);
            elseCmds = procCode();
            matchToken(TokenType.CLOSE_CUR);
            ifCommand.addElseCommand(elseCmds);
        }
        return ifCommand;
    }

    // <while> ::= while '(' <boolexpr> ')' '{' <code> '}'
    private WhileCommand procWhile() throws LexicalException, IOException {
        matchToken(TokenType.WHILE);
        int line = lex.getLine();

        matchToken(TokenType.OPEN_PAR);
        BoolExpr cond = procBoolExpr();
        matchToken(TokenType.CLOSE_PAR);

        matchToken(TokenType.OPEN_CUR);
        Command cmds = procCode();
        matchToken(TokenType.CLOSE_CUR);

        return new WhileCommand(line, cond, cmds);
    }

    // <foreach> ::= foreach '(' <expr> as <var> [ '=>' <var> ] ')' '{' <code> '}'
    private ForeachCommand procForeach() throws LexicalException, IOException {
        matchToken(TokenType.FOREACH);
        int line = lex.getLine();
        
        matchToken(TokenType.OPEN_PAR);
        Expr expr = procExpr();
        matchToken(TokenType.AS);
        Variable key = procVar();
        Variable value = null;
        
        if(current.type == TokenType.ARROW){
            matchToken(TokenType.ARROW);
            value = procVar();
        }
        
        matchToken(TokenType.CLOSE_PAR);

        matchToken(TokenType.OPEN_CUR);
        Command cmds = procCode();
        matchToken(TokenType.CLOSE_CUR);

        return new ForeachCommand(line, expr, cmds, key, value);//Verificar os parametros
    }

    // <echo> ::= echo <expr> ';'
    private EchoCommand procEcho() throws LexicalException, IOException {
        matchToken(TokenType.ECHO);
        int line = lex.getLine();
        Expr expr = procExpr();
        matchToken(TokenType.SEMICOLON);
        return new EchoCommand(line, expr);
    }

    // <assign> ::= <value> [ ('=' | '+=' | '-=' | '.=' | '*=' | '/=' | '%=') <expr> ] ';'
    private AssignCmd procAssign() throws LexicalException, IOException {
        int line = lex.getLine();
        Expr left = procValue();
        Expr right = null;
        AssignOp op = AssignOp.NoAssignOp;
        
        if(current.type == TokenType.ASSIGN ||        
           current.type == TokenType.ASSIGN_ADD ||    
           current.type == TokenType.ASSIGN_SUB  ||
           current.type == TokenType.ASSIGN_CONCAT ||
           current.type == TokenType.ASSIGN_MUL ||
           current.type == TokenType.ASSIGN_DIV ||
           current.type == TokenType.ASSIGN_MOD){
        
            switch(current.type){
                case ASSIGN:
                    op = AssignOp.StdAssignOp;
                    matchToken(TokenType.ASSIGN);
                    break;
                case ASSIGN_ADD:
                    op = AssignOp.AddAssignOp;
                    matchToken(TokenType.ASSIGN_ADD);
                    break;    
                case ASSIGN_SUB:  
                    op = AssignOp.SubAssignOp; 
                    matchToken(TokenType.ASSIGN_SUB);
                    break; 
                case ASSIGN_CONCAT:
                    op = AssignOp.ConcatAssignOp;
                    matchToken(TokenType.ASSIGN_CONCAT);
                    break; 
                case ASSIGN_MUL:    
                    op = AssignOp.MulAssignOp;
                    matchToken(TokenType.ASSIGN_MUL);
                    break;
                case ASSIGN_DIV:    
                    op = AssignOp.DivAssignOp;
                    matchToken(TokenType.ASSIGN_DIV);
                    break;
                case ASSIGN_MOD:
                    op = AssignOp.ModAssignOp;
                    matchToken(TokenType.ASSIGN_MOD);
                    break;
                default: 
                    showError();
            }
        }
        
        if(op != AssignOp.NoAssignOp)
            right = procExpr();
        
        matchToken(TokenType.SEMICOLON);
        return new AssignCmd(line, left, op, right);
    }

    // <boolexpr> ::= [ '!' ] <cmpexpr> [ (and | or) <boolexpr> ]
    private BoolExpr procBoolExpr() throws LexicalException, IOException {
        int line = lex.getLine();
        BoolExpr left = procCmpExpr();
        BoolExpr right;
        
        if(current.type == TokenType.NOT){
            matchToken(TokenType.NOT);//Implementar lógica aqui not deve vir emcima daqui
            left = new NotBoolExpr(line,left);
        }

        BoolOp op = null;
        
        if(current.type == TokenType.AND){
            matchToken(TokenType.AND);
            right = procBoolExpr();
            left =  new CompositeBoolExpr(line, left, BoolOp.And, right);
        }else if(current.type == TokenType.OR){
            matchToken(TokenType.OR);
            right = procBoolExpr();
            left =  new CompositeBoolExpr(line, left, BoolOp.Or, right);
        }
        
        return left;
    }

    // <cmpexpr> ::= <expr> ('==' | '!=' | '<' | '>' | '<=' | '>=') <expr>
    private BoolExpr procCmpExpr() throws LexicalException, IOException {
        int line = lex.getLine();
        Expr left = procExpr();
        RelOp op = null;
        
        if(current.type == TokenType.EQUAL ||         
           current.type == TokenType.NOT_EQUAL ||     
           current.type == TokenType.LOWER ||         
           current.type == TokenType.GREATER ||       
           current.type == TokenType.LOWER_EQ ||      
           current.type == TokenType.GREATER_EQ){
            switch(current.type){
                case EQUAL:       
                    op = RelOp.Equal;
                    matchToken(TokenType.EQUAL);
                    break;
                case NOT_EQUAL:
                    op = RelOp.NotEqual;
                    matchToken(TokenType.NOT_EQUAL);
                    break;     
                case LOWER:
                    op = RelOp.LowerThan;
                    matchToken(TokenType.LOWER);
                    break;         
                case GREATER:
                    op = RelOp.GreaterThan;
                    matchToken(TokenType.GREATER);
                    break;      
                case LOWER_EQ:
                    op = RelOp.LowerEqual;
                    matchToken(TokenType.LOWER_EQ);
                    break;      
                case GREATER_EQ:
                    op = RelOp.GreaterEqual;  
                    matchToken(TokenType.GREATER_EQ);
                    break;
            }
        }
        
        Expr right = procExpr();
        return new SingleBoolExpr(line,left,op,right);
    }

    // <expr> ::= <term> { ('+' | '-' | '.') <term> }
    private Expr procExpr() throws LexicalException, IOException {
        int line = lex.getLine();
        Expr left = procTerm();
        Expr right = null;
        
        while(current.type == TokenType.ADD ||
              current.type == TokenType.SUB ||
              current.type == TokenType.CONCAT){
            BinaryOp op = null;
            switch(current.type){
                case ADD:
                    op = BinaryOp.AddOp;
                    matchToken(TokenType.ADD);
                    break;
                case SUB:
                    op = BinaryOp.SubOp;
                    matchToken(TokenType.SUB);
                    break;
                case CONCAT:
                    op = BinaryOp.ConcatOp;
                    matchToken(TokenType.CONCAT);
                    break;
            }
            right = procTerm();
            left = new BinaryExpr(line, left, op, right);
        }
        
        return left;
    }

    // <term> ::= <factor> { ('*' | '/' | '%') <factor> }
    private Expr procTerm() throws LexicalException, IOException {
        int line = lex.getLine();
        BinaryOp op = null;
        Expr left = procFactor();
        Expr right = null;
        
        while(current.type == TokenType.MUL ||
              current.type == TokenType.DIV ||
              current.type == TokenType.MOD){
            switch(current.type){
                case MUL:
                    op = BinaryOp.MulOp;
                    matchToken(TokenType.MUL);
                    break;
                case DIV:
                    op = BinaryOp.DivOp;
                    matchToken(TokenType.DIV);
                    break;
                case MOD:
                    op = BinaryOp.ModOp;
                    matchToken(TokenType.MOD);
                    break;
            }
            right = procFactor();
            left = new BinaryExpr(line, left, op, right);
        }
        
        return left;
    }

    // <factor> ::= <number> | <string> | <array> | <read> | <value>
    private Expr procFactor() throws LexicalException, IOException {
        if(current.type == TokenType.NUMBER){
                return procNumber();
               
        }else if(current.type == TokenType.STRING){
                return procString();
        }else if(current.type == TokenType.ARRAY){
                return procArray();
        }else if(current.type == TokenType.READ){
                return procRead();
        }else{
                return procValue();
        }	
    }

    // <array> ::= array '(' [ <expr> '=>' <expr> { ',' <expr> '=>' <expr> } ] ')'
    private ArrayExpr procArray() throws LexicalException, IOException { 
        matchToken(TokenType.ARRAY);
        int line = lex.getLine();

        matchToken(TokenType.OPEN_PAR);
        Expr key,value;
        ArrayExpr arr = new ArrayExpr(line);

        while(current.type != TokenType.CLOSE_PAR){
                key = procExpr();
                matchToken(TokenType.ARROW);
                value = procExpr();
                try {
                    System.out.println("Key: " + key.expr().value() + "  Value:"+ value.expr().value() );
                } catch (Exception ex) {
                    
                }
                arr.insert(key, value);
                if(current.type != TokenType.CLOSE_PAR) matchToken(TokenType.COMMA);
        }
        matchToken(TokenType.CLOSE_PAR);
        return arr;
    }

    // <read> ::= <expr>
    private Expr  procRead() throws LexicalException, IOException {
		matchToken(TokenType.READ);
		int line = lex.getLine();
		return new ReadExpr(line,procExpr());
    }

    // <value> ::= [ ('++' | '—-') ] <access> | <access> [ ('++' | '--') ]
    private Expr procValue() throws LexicalException, IOException {
        int line = lex.getLine();
        Expr expr;
        
        if(current.type == TokenType.INC){
            matchToken(TokenType.INC);
            expr = procAccess();
            return new UnaryExpr(line,expr,UnaryOp.PreIncOp);
        }else if(current.type == TokenType.DEC){
            matchToken(TokenType.DEC);
            expr = procAccess();
            return new UnaryExpr(line,expr,UnaryOp.PreDecOp);
        }
        
        expr = procAccess();
        
        if(current.type == TokenType.INC){     
            matchToken(TokenType.INC);
            return new UnaryExpr(line,expr,UnaryOp.PosIncOp);
        }else if(current.type == TokenType.DEC){
            matchToken(TokenType.DEC);
            return new UnaryExpr(line,expr,UnaryOp.PosDecOp);
        }
        
        return expr;
    }

    // <access> ::= ( <varvar> | '(' <expr> ')' ) [ '[' <expr> ']' ]
    private Expr procAccess() throws LexicalException, IOException {
        int line = lex.getLine();
        Expr base,index = null;
        
        if(current.type == TokenType.OPEN_PAR){
            matchToken(TokenType.OPEN_PAR);
            base = procExpr();
            matchToken(TokenType.CLOSE_PAR);
        }else{
            return procVarVar();
        }

        if(current.type == TokenType.OPEN_BRA){
            matchToken(TokenType.OPEN_BRA);
            index = procExpr();
            matchToken(TokenType.CLOSE_BRA);
        }
        return new AccessExpr(line, base, index);
    }

    // <varvar> ::= '$' <varvar> | <var>
    private SetExpr procVarVar() throws LexicalException, IOException {
        if(current.type == TokenType.DOLAR){
            matchToken(TokenType.DOLAR);
            SetExpr v = procVarVar();
            return new VarVarExpr(lex.getLine(),v);
        }
        return procVar();
    }

    private ConstExpr procNumber() throws LexicalException, IOException {
        IntegerValue value = new IntegerValue(Integer.parseInt(current.token));
        matchToken(TokenType.NUMBER);
        return new ConstExpr(lex.getLine(),value);
    }

    private ConstExpr procString() throws LexicalException, IOException {
        StringValue value = new StringValue(current.token);
        matchToken(TokenType.STRING);
        return new ConstExpr(lex.getLine(),value);
    }

    private Variable procVar() throws LexicalException, IOException {
        String name = current.token;
        matchToken(TokenType.VAR);
        return Variable.instance(name);
    }
}