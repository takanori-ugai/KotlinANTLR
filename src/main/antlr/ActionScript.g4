grammar ActionScript;

document
   : title = LINE_TEXT NEWLINE description = LINE_TEXT NEWLINE NEWLINE NEWLINE actionLine (NEWLINE actionLine)* NEWLINE? EOF
   ;

actionLine
   : ACTION_LINE
   ;

ACTION_LINE
   : '<' IDENTIFIER '>' ' ' '[' ACTION_NAME ']' ' ' '<' IDENTIFIER '>' ' ' '(' NUMBER ')'
   ;

LINE_TEXT
   : ~ [\r\n]+
   ;

NEWLINE
   : '\r'? '\n'
   ;

WS
   : [ \t]+ -> skip
   ;

fragment IDENTIFIER
   : [A-Za-z] [A-Za-z0-9_]*
   ;

fragment ACTION_NAME
   : [A-Z_]+
   ;

fragment NUMBER
   : [0-9]+
   ;

