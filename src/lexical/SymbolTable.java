package lexical;

import java.util.Map;
import java.util.HashMap;

/*
	Observação apenas para entender o funcionamento desse arquivo!
	Esse arquivo está mapeando as strings possiveis para identificar os tokens.
*/
public class SymbolTable {

    private Map<String, TokenType> st;

    public SymbolTable() {
        st = new HashMap<String, TokenType>();

        // SYMBOLS
        st.put("(", TokenType.OPEN_PAR);
        st.put(")", TokenType.CLOSE_PAR);
        st.put("{", TokenType.OPEN_CUR);
        st.put("}", TokenType.CLOSE_CUR);
        st.put("=>", TokenType.ARROW);
        st.put(",", TokenType.COMMA);
        st.put(";", TokenType.SEMICOLON);
        st.put("[", TokenType.OPEN_BRA);
        st.put("]", TokenType.CLOSE_BRA);
        st.put("$", TokenType.DOLAR);

        // OPERATORS
        st.put("=", TokenType.ASSIGN);
        st.put("+=", TokenType.ASSIGN_ADD);
        st.put("-=", TokenType.ASSIGN_SUB);
        st.put(".=", TokenType.ASSIGN_CONCAT);
        st.put("*=", TokenType.ASSIGN_MUL);
        st.put("/=", TokenType.ASSIGN_DIV);
        st.put("%=", TokenType.ASSIGN_MOD);
        st.put("!", TokenType.NOT);
        st.put("==", TokenType.EQUAL);
        st.put("!=", TokenType.NOT_EQUAL);
        st.put("<", TokenType.LOWER);
        st.put(">", TokenType.GREATER);
        st.put("<=", TokenType.LOWER_EQ);
        st.put(">=", TokenType.GREATER_EQ);
        st.put("+", TokenType.ADD);
        st.put("-", TokenType.SUB);
        st.put(".", TokenType.CONCAT);
        st.put("*", TokenType.MUL);
        st.put("/", TokenType.DIV);
        st.put("%", TokenType.MOD);
        st.put("++", TokenType.INC);
        st.put("--", TokenType.DEC);

        // KEYWORDS
        st.put("if", TokenType.IF);
        st.put("elseif", TokenType.ELSEIF);
        st.put("else", TokenType.ELSE);
        st.put("while", TokenType.WHILE);
        st.put("foreach", TokenType.FOREACH);
        st.put("as", TokenType.AS);
        st.put("echo", TokenType.ECHO);
        st.put("and", TokenType.AND);
        st.put("or", TokenType.OR);
        st.put("array", TokenType.ARRAY);
        st.put("read", TokenType.READ);
    }

    public boolean contains(String token) {
        return st.containsKey(token);
    }

    public TokenType find(String token) {
        return this.contains(token) ?
            st.get(token) : TokenType.INVALID_TOKEN;
    }
}
