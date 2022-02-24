package lexical;

/*
	Observação apenas para entender o funcionamento desse arquivo!
	Esse arquivo contem um enumerador com os tipos de token.
*/
public enum TokenType {
    // SPECIALS
    UNEXPECTED_EOF,
    INVALID_TOKEN,
    END_OF_FILE,

    // SYMBOLS
    OPEN_PAR,      // (
    CLOSE_PAR,     // )
    OPEN_CUR,      // {
    CLOSE_CUR,     // }
    ARROW,         // =>
    COMMA,         // ,
    SEMICOLON,     // ;
    OPEN_BRA,      // [
    CLOSE_BRA,     // ]
    DOLAR,         // $

    // OPERATORS
    ASSIGN,        // =
    ASSIGN_ADD,    // +=
    ASSIGN_SUB,    // -=
    ASSIGN_CONCAT, // .=
    ASSIGN_MUL,    // *=
    ASSIGN_DIV,    // /=
    ASSIGN_MOD,    // %=
    NOT,           // !
    EQUAL,         // ==
    NOT_EQUAL,     // !=
    LOWER,         // <
    GREATER,       // >
    LOWER_EQ,      // <=
    GREATER_EQ,    // >=
    ADD,           // +
    SUB,           // -
    CONCAT,        // .
    MUL,           // *
    DIV,           // /
    MOD,           // %
    INC,           // ++
    DEC,           // --

    // KEYWORDS
    IF,           // if
    ELSEIF,       // elseif
    ELSE,         // else
    WHILE,        // while
    FOREACH,      // foreach
    AS,           // as
    ECHO,         // echo
    AND,          // and
    OR,           // or
    ARRAY,        // array
    READ,         // read

    // OTHERS
    NUMBER,       // numeros
    STRING,       // strings
    VAR,          // variable

};
