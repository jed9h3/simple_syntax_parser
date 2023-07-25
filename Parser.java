import java.util.LinkedList;

class Parser {
    public int token;
    public LinkedList<Integer> linesIndicators;
    public String[] tokens;

    public Parser() {
        this.token = 0;
    }

    public void parse(String[] toks, LinkedList<Integer> indis) {
        this.tokens = toks;
        this.linesIndicators = indis;
        project_def();
        if (tokens[token].compareTo(".") == 0) {
            System.out.println("successful parsing");
        } else {
            System.out.println("parsing failed: unexpected EOF");
        }
    }

    public void error(String expstr) {
        int firstie = linesIndicators.getFirst();
        int linesnum = 1;
        while (firstie < token) {
            linesIndicators.removeFirst();
            firstie = linesIndicators.getFirst();
            linesnum++;
        }
        System.out.println("error accured at token: " + (token + 1) + ",Line: " + linesnum + ", a (" + expstr
                + ") token expected but (" + tokens[token] + ") was scanned instead");
        System.exit(0);
    }

    public void project_def() {
        project_heading();
        declarations();
        compund_stmt();
    }

    public void project_heading() {
        if (tokens[token].compareTo("project") == 0) {
            token++;
            if (tokens[token].compareTo("name") == 0) {
                token++;
                if (tokens[token].compareTo(";") == 0) {
                    token++;
                } else {
                    error(";");
                }
            } else {
                error("variable name");
            }
        } else {
            error("project");
        }
    }

    public void declarations() {
        const_decl();
        var_decl();
        subroutine_decl();
    }

    public void const_decl() {
        if (tokens[token].compareTo("const") == 0) {
            token++;
            const_item();
            if (tokens[token].compareTo(";") == 0) {
                token++;
                while (tokens[token].compareTo("name") == 0) {
                    const_item();
                    if (tokens[token].compareTo(";") == 0) {
                        token++;
                    } else {
                        error(";");
                    }
                }
            } else {
                error(";");
            }
        } else if (tokens[token].compareTo("var") != 0 && tokens[token].compareTo("routine") != 0
                && tokens[token].compareTo("start") != 0) {
            error("const");
        }
    }

    public void const_item() {
        if (tokens[token].compareTo("name") == 0) {
            token++;
            if (tokens[token].compareTo("=") == 0) {
                token++;
                if (tokens[token].compareTo("integer-value") == 0) {
                    token++;
                } else {
                    error("integer-value");
                }
            } else {
                error("=");
            }
        } else {
            error("variable name");
        }
    }

    public void var_decl() {
        if (tokens[token].compareTo("var") == 0) {
            token++;
            var_item();
            if (tokens[token].compareTo(";") == 0) {
                token++;
                while (tokens[token].compareTo("name") == 0) {
                    var_item();
                    if (tokens[token].compareTo(";") == 0) {
                        token++;
                    } else {
                        error(";");
                    }
                }
            } else {
                error(";");
            }
        } else if (tokens[token].compareTo("routine") != 0 && tokens[token].compareTo("start") != 0) {
            error("var");
        }
    }

    public void var_item() {
        name_list();
        if (tokens[token].compareTo(":") == 0) {
            token++;
            if (tokens[token].compareTo("int") == 0) {
                token++;
            } else {
                error("int");
            }
        } else {
            error(":");
        }
    }

    public void name_list() {
        if (tokens[token].compareTo("name") == 0) {
            token++;
            while (tokens[token].compareTo(",") == 0) {
                token++;
                if (tokens[token].compareTo("name") == 0) {
                    token++;
                } else {
                    error("variable name");
                }
            }
            if (tokens[token].compareTo(":") != 0) {
                error(":");
            }
        } else {
            error("variable name");
        }
    }

    public void subroutine_decl() {
        if (tokens[token].compareTo("routine") == 0) {
            subroutine_heading();
            declarations();
            compund_stmt();
            if (tokens[token].compareTo(";") == 0) {
                token++;
            } else {
                error(";");
            }
        } else if (tokens[token].compareTo("start") != 0) {
            error("start");
        }
    }

    public void subroutine_heading() {
        if (tokens[token].compareTo("routine") == 0) {
            token++;
            if (tokens[token].compareTo("name") == 0) {
                token++;
                if (tokens[token].compareTo(";") == 0) {
                    token++;
                } else {
                    error(";");
                }
            } else {
                error("variable name");
            }
        } else {
            error("routine");
        }
    }

    public void compund_stmt() {
        if (tokens[token].compareTo("start") == 0) {
            token++;
            stmt_list();
            if (tokens[token].compareTo("end") == 0) {
                token++;
            } else {
                error("end");
            }
        } else {
            error("start");
        }
    }

    public void stmt_list() {
        while (tokens[token].compareTo("name") == 0 || tokens[token].compareTo("input") == 0
                || tokens[token].compareTo("output") == 0
                || tokens[token].compareTo("if") == 0 || tokens[token].compareTo("loop") == 0
                || tokens[token].compareTo("start") == 0 || tokens[token].compareTo(";") == 0) {
            statement();
            if (tokens[token].compareTo(";") == 0) {
                token++;
            } else {
                error(";");
            }

        }
    }

    public void statement() {
        if (tokens[token].compareTo("name") == 0) {
            ass_stmt();
        } else if (tokens[token].compareTo("input") == 0 || tokens[token].compareTo("output") == 0) {
            inout_stmt();
        } else if (tokens[token].compareTo("if") == 0) {
            if_stmt();
        } else if (tokens[token].compareTo("loop") == 0) {
            loop_stmt();
        } else if (tokens[token].compareTo("start") == 0) {
            compund_stmt();
        } else if (tokens[token].compareTo(";") != 0 && tokens[token].compareTo("else") != 0
                && tokens[token].compareTo("endif") != 0) {
            error("variable name");
        }
    }

    public void ass_stmt() {
        if (tokens[token].compareTo("name") == 0) {
            token++;
            if (tokens[token].compareTo(":=") == 0) {
                token++;
                arith_exp();
            } else {
                error(":=");
            }
        } else {
            error("variable name");
        }
    }

    public void arith_exp() {
        term();
        while (tokens[token].compareTo("+") == 0 || tokens[token].compareTo("-") == 0) {
            add_sign();
            term();
        }
    }

    public void term() {
        factor();
        while (tokens[token].compareTo("*") == 0 || tokens[token].compareTo("/") == 0
                || tokens[token].compareTo("%") == 0) {
            mul_sign();
            factor();
        }
    }

    public void factor() {
        if (tokens[token].compareTo("(") == 0) {
            token++;
            arith_exp();
            if (tokens[token].compareTo(")") == 0) {
                token++;
            } else {
                error(")");
            }
        } else {
            name_value();
        }
    }

    public void name_value() {
        if (tokens[token].compareTo("name") == 0 || tokens[token].compareTo("integer-value") == 0) {
            token++;
        } else {
            error("variable name / integer value");
        }
    }

    public void add_sign() {
        if (tokens[token].compareTo("+") == 0 || tokens[token].compareTo("-") == 0) {
            token++;
        } else {
            error("+ / -");
        }
    }

    public void mul_sign() {
        if (tokens[token].compareTo("*") == 0 || tokens[token].compareTo("/") == 0
                || tokens[token].compareTo("%") == 0) {
            token++;
        } else {
            error("*/ / /%");
        }
    }

    public void inout_stmt() {
        if (tokens[token].compareTo("input") == 0) {
            token++;
            if (tokens[token].compareTo("(") == 0) {
                token++;
                if (tokens[token].compareTo("name") == 0) {
                    token++;
                    if (tokens[token].compareTo(")") == 0) {
                        token++;
                    } else {
                        error(")");
                    }
                } else {
                    error("variable name");
                }
            } else {
                error("(");
            }
        } else if (tokens[token].compareTo("output") == 0) {
            token++;
            if (tokens[token].compareTo("(") == 0) {
                token++;
                name_value();
                if (tokens[token].compareTo(")") == 0) {
                    token++;
                } else {
                    error(")");
                }
            } else {
                error("(");
            }
        } else {
            error("input / output");
        }
    }

    public void if_stmt() {
        if (tokens[token].compareTo("if") == 0) {
            token++;
            if (tokens[token].compareTo("(") == 0) {
                token++;
                bool_exp();
                if (tokens[token].compareTo(")") == 0) {
                    token++;
                    if (tokens[token].compareTo("then") == 0) {
                        token++;
                        statement();
                        else_part();
                        if (tokens[token].compareTo("endif") == 0) {
                            token++;
                        } else {
                            error("endif");
                        }
                    } else {
                        error("then");
                    }
                } else {
                    error(")");
                }
            } else {
                error("(");
            }
        } else {
            error("if");
        }
    }

    public void else_part() {
        if (tokens[token].compareTo("else") == 0) {
            token++;
            statement();
        } else if (tokens[token].compareTo("endif") != 0) {
            error("endif");
        }
    }

    public void loop_stmt() {
        if (tokens[token].compareTo("loop") == 0) {
            token++;
            if (tokens[token].compareTo("(") == 0) {
                token++;
                bool_exp();
                if (tokens[token].compareTo(")") == 0) {
                    token++;
                    if (tokens[token].compareTo("do") == 0) {
                        token++;
                        statement();
                    } else {
                        error("do");
                    }
                } else {
                    error(")");
                }
            } else {
                error("(");
            }
        } else {
            error("loop");
        }
    }

    public void bool_exp() {
        name_value();
        relational_oper();
        name_value();
    }

    public void relational_oper() {
        if (tokens[token].compareTo("=") != 0 && tokens[token].compareTo("<>") != 0
                && tokens[token].compareTo("<") != 0 && tokens[token].compareTo("<=") != 0
                && tokens[token].compareTo(">") != 0 && tokens[token].compareTo(">=") != 0) {
            error("relational operation");
        } else {
            token++;
        }
    }
}
