/*  
Lexer Specification
Author: Wu Wenqi    A0124278A
*/

/* --------------------------Usercode Section------------------------ */
package wwu.compiler;

import java_cup.runtime.*;
      
%%
   
/* -----------------Options and Declarations Section----------------- */

%class Lexer
%line
%column
%cup
   
%{   
  StringBuffer string = new StringBuffer();

  // This is used for debugging.
  private String getSymbolDesc(int type) {
    switch (type) {
      case sym.SEMI:            return "SEMI";
      case sym.LPAREN:          return "LPAREN";
      case sym.RPAREN:          return "RPAREN";
      case sym.LBRACE:          return "LBRACE";
      case sym.RBRACE:          return "RBRACE";
      case sym.COMMA:           return "COMMA";
      case sym.PERIOD:          return "PERIOD";
      case sym.PLUS:            return "PLUS";
      case sym.MINUS:           return "MINUS";
      case sym.TIMES:           return "TIMES";
      case sym.DIVIDE:          return "DIVIDE";
      case sym.ASSIGN:          return "ASSIGN";
      case sym.EQUAL:           return "EQUAL";
      case sym.NOT_EQUAL:       return "NOT_EQUAL";
      case sym.LESS:            return "LESS";
      case sym.GREATER:         return "GREATER";
      case sym.LESS_EQUAL:      return "LESS_EQUAL";
      case sym.GREATER_EQUAL:   return "GREATER_EQUAL";
      case sym.AND:             return "AND";
      case sym.OR:              return "OR";
      case sym.NOT:             return "NOT";
      case sym.IF:              return "IF";
      case sym.ELSE:            return "ELSE";
      case sym.WHILE:           return "WHILE";
      case sym.RETURN:          return "RETURN";
      case sym.READ:            return "READ";
      case sym.PRINT:           return "PRINT";
      case sym.CLASS:           return "CLASS";
      case sym.THIS:            return "THIS";
      case sym.NEW:             return "NEW";
      case sym.MAIN:            return "MAIN";
      case sym.NULL:            return "NULL";
      case sym.VOID:            return "VOID";
      case sym.BOOL:            return "BOOL";
      case sym.INT:             return "INT";
      case sym.CNAME:           return "CNAME";
      case sym.ID:              return "ID";
      case sym.STRING:          return "STRING";
      case sym.INT_LITERAL:     return "INT_LITERAL";
      case sym.BOOL_LITERAL:    return "BOOL_LITERAL"; 
      case sym.STRING_LITERAL:  return "STRING_LITERAL";
      default:                  return "unknown";
    }
  }

  private Symbol symbol(int type) {
    // System.out.println(getSymbolDesc(type));
    return new Symbol(type, yyline, yycolumn);
  }

  private Symbol symbol(int type, Object value) {
    // System.out.println(getSymbolDesc(type));
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* Comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

Identifier = [a-z][A-Za-z_0-9]*
Cname = [A-Z][A-Za-z_0-9]*
IntegerLiteral = 0 | [1-9][0-9]*

%state STRING

%%

/* ------------------------Lexical Rules Section---------------------- */

<YYINITIAL> {
  ";"                 { return symbol(sym.SEMI); }
  "("                 { return symbol(sym.LPAREN); }
  ")"                 { return symbol(sym.RPAREN); }
  "{"                 { return symbol(sym.LBRACE); }
  "}"                 { return symbol(sym.RBRACE); }
  ","                 { return symbol(sym.COMMA); }
  "."                 { return symbol(sym.PERIOD); }
  
  /* Arithmetic operators */
  "+"                 { return symbol(sym.PLUS); }
  "-"                 { return symbol(sym.MINUS); }
  "*"                 { return symbol(sym.TIMES); }
  "/"                 { return symbol(sym.DIVIDE); }
  "="                 { return symbol(sym.ASSIGN); }

  /* Relational operators */
  "=="                { return symbol(sym.EQUAL); }
  "!="                { return symbol(sym.NOT_EQUAL); }
  "<"                 { return symbol(sym.LESS); }
  ">"                 { return symbol(sym.GREATER); }
  "<="                { return symbol(sym.LESS_EQUAL); }
  ">="                { return symbol(sym.GREATER_EQUAL); }

  /* Boolean operators */
  "&&"                { return symbol(sym.AND); }
  "||"                { return symbol(sym.OR); }
  "!"                 { return symbol(sym.NOT); }

  /* Control flow */
  "if"                { return symbol(sym.IF); }
  "else"              { return symbol(sym.ELSE); }
  "while"             { return symbol(sym.WHILE); }
  "return"            { return symbol(sym.RETURN); }

  /* Built-in types */
  "Int"               { return symbol(sym.INT); }
  "Bool"              { return symbol(sym.BOOL); }
  "String"            { return symbol(sym.STRING); }

  /* Built-in functions */
  "readln"            { return symbol(sym.READ); }
  "println"           { return symbol(sym.PRINT); }

  "class"             { return symbol(sym.CLASS); }
  "this"              { return symbol(sym.THIS); }
  "new"               { return symbol(sym.NEW); }
  "main"              { return symbol(sym.MAIN); }
  "null"              { return symbol(sym.NULL); }
  "Void"              { return symbol(sym.VOID); }

  "true"              { return symbol(sym.BOOL_LITERAL, Boolean.TRUE); }
  "false"             { return symbol(sym.BOOL_LITERAL, Boolean.FALSE); }

  {IntegerLiteral}    { return symbol(sym.INT_LITERAL, Integer.valueOf(yytext())); }

  {Cname}             { return symbol(sym.CNAME, yytext()); }
  
  {Identifier}        { return symbol(sym.ID, yytext()); }
  
  /* Change to STRING state and start parsing string */
  \"                  { string.setLength(0); yybegin(STRING); }

  {Comment}           { /* do nothing */ }
  {WhiteSpace}        { /* do nothing */ }
}

<STRING> {
  \"                  { yybegin(YYINITIAL); return symbol(sym.STRING_LITERAL, string.toString()); }
  [^\n\r\"\\]+        { string.append( yytext() ); }
  \\t                 { string.append('\t'); }
  \\n                 { string.append('\n'); }

  \\r                 { string.append('\r'); }
  \\\"                { string.append('\"'); }
  \\                  { string.append('\\'); }
}

/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^]                   { throw new Error("Illegal character <"+yytext()+">"); }
