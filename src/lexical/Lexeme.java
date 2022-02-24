package lexical;

/*
	Observação apenas para entender o funcionamento desse arquivo!
	Esse arquivo apenas possui um lexema com o token e o tipó desse token para auxiliar o LexicalAnalysis.
*/
public class Lexeme {

    public String token;
    public TokenType type;

    public Lexeme(String token, TokenType type) {
        this.token = token;
        this.type = type;
    }

}
