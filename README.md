# simple_syntax_parser
Simple Recursive Descent syntax parser with its scanner 

---
# About
This parser is built around the following (EBNF) production rules:

project-declaration := project-def     "."
project-def  :=   project-heading        declarations          compound-stmt
project-heading   := project      "name"       ";"
declarations   := const-decl       var-decl       subroutine-decl
const-decl   := const      ( const-item      ";" )+        |     
const-item   :=  "name"   =   "integer-value"
var-decl   := var    (var-item     ";" )+         |     
var-item   :=  name-list       ":"       int
name-list  :=  "name"    ( ","     "name" )* 
subroutine-decl := subroutine-heading      declarations      compound-stmt    “;”   |     
subroutine-heading   := routine      "name"       ";"
compund-stmt := start       stmt-list       end
stmt-list :=   ( statement    ";" )* 
statement := ass-stmt  |  inout-stmt  |  if-stmt  |  loop-stmt   |   compound-stmt    |     
ass-stmt :=” name”     ":="      arith-exp
arith-exp := term    ( add-sign      term )*
term := factor    ( mul-sign       factor  )*
factor :=  "("   arith-exp  ")"   |     name-value
name-value :=  "name"      |        "integer-value"
add-sign :=  "+"    |     "-"
mul-sign := "*"    |      "/"     |        “%”
inout-stmt := input "("    "name"     ")"    |    output  "("   name-value   ")"
if-stmt := if     “(“    bool-exp    “)”    then     statement     else-part       endif
else-part :=  else     statement   |   
loop-stmt := loop   “(“    bool-exp   “)”  do      statement
bool-exp := name-value       relational-oper        name-value 
relational-oper :=      "="     |     "<>"     |     "<"    |     "<="     |     ">"    |     ">="

Noting that:
All “names” and “integer-value” are user defined names and values in the source code.
The words between  “ …”  are terminals (tokens).
the following tokens are reserved words: (loop, do, else, statement, if, then, endif, input, output, start, end, routine, int, var, const, project)

---

# Configure

import javafx libraries.
a copy of a sample program written accourding to the above production rules is attatched.
