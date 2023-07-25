import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.LinkedList;

public class LexScanner {
    HashMap<String, String> keywords;
    LinkedList<Integer> linesIndicators;

    public LexScanner() {
        keywords = new HashMap<>();
        this.keywords.put("=", "=");
        this.keywords.put("<>", "<>");
        this.keywords.put("<", "<");
        this.keywords.put("<=", "<=");
        this.keywords.put(">", ">");
        this.keywords.put(">=", ">=");
        this.keywords.put("loop", "loop");
        this.keywords.put("(", "(");
        this.keywords.put(")", ")");
        this.keywords.put("do", "do");
        this.keywords.put("else", "else");
        this.keywords.put("if", "if");
        this.keywords.put("then", "then");
        this.keywords.put("endif", "endif");
        this.keywords.put("input", "input");
        this.keywords.put("output", "output");
        this.keywords.put("*", "*");
        this.keywords.put("/", "/");
        this.keywords.put("%", "%");
        this.keywords.put("+", "+");
        this.keywords.put("-", "-");
        this.keywords.put(":=", ":=");
        this.keywords.put(";", ";");
        this.keywords.put("start", "start");
        this.keywords.put("end", "end");
        this.keywords.put("routine", "routine");
        this.keywords.put(",", ",");
        this.keywords.put(":", ":");
        this.keywords.put("int", "int");
        this.keywords.put("var", "var");
        this.keywords.put("const", "const");
        this.keywords.put("project", "project");
        this.keywords.put(".", ".");
    }

    public String[] scanner(File file) {
        try {
            Scanner input = new Scanner(file);
            StringBuilder tokensSb = new StringBuilder();
            int linecounter = 1;
            linesIndicators = new LinkedList<>();
            int tokcounter = -1;
            while (input.hasNextLine()) {
                String line = input.nextLine();
                StringBuilder tempSb = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    if (Character.isLetter(line.charAt(i))) {
                        if (tempSb.length() != 0 && Character.isDigit(tempSb.charAt(0))) {
                            System.out
                                    .println("user defined variables names cannot start with a digit, variable name: ("
                                            + tempSb.toString() + Character.toString(line.charAt(i)) + "), Line: "
                                            + linecounter);
                            input.close();
                            return null;
                        } else {
                            tempSb.append(Character.toString(line.charAt(i)));
                        }
                    } else if (Character.isDigit(line.charAt(i))) {
                        tempSb.append(Character.toString(line.charAt(i)));
                    } else if (Character.toString(line.charAt(i)).compareTo(" ") == 0) {
                        if (tempSb.length() != 0) {
                            tokcounter++;
                            String value = keywords.getOrDefault(tempSb.toString(), null);
                            if (value != null) {
                                tokensSb.append(value + " ");
                            } else if (Character.isDigit(tempSb.charAt(0))) {
                                tokensSb.append("integer-value ");
                            } else {
                                tokensSb.append("name ");
                            }
                            tempSb.setLength(0);
                        }
                    } else {
                        if (Character.toString(line.charAt(i)).compareTo("<") == 0
                                || Character.toString(line.charAt(i)).compareTo(">") == 0
                                || Character.toString(line.charAt(i)).compareTo(":") == 0 && i + 1 < line.length()) {
                            String value = keywords.getOrDefault(
                                    Character.toString(line.charAt(i)) + Character.toString(line.charAt(i + 1)),
                                    null);
                            if (value != null) {
                                if (tempSb.length() != 0) {
                                    tokcounter++;
                                    String value2 = keywords.getOrDefault(tempSb.toString(), null);
                                    if (value2 != null) {
                                        tokensSb.append(value2 + " ");
                                    } else if (Character.isDigit(tempSb.charAt(0))) {
                                        tokensSb.append("integer-value ");
                                    } else {
                                        tokensSb.append("name ");
                                    }
                                    tempSb.setLength(0);
                                }
                                tokcounter++;
                                tokensSb.append(value + " ");
                                i++;
                                continue;
                            }
                        }
                        String value = keywords.getOrDefault(Character.toString(line.charAt(i)), null);
                        if (value != null) {
                            if (tempSb.length() != 0) {
                                tokcounter++;
                                String value2 = keywords.getOrDefault(tempSb.toString(), null);
                                if (value2 != null) {
                                    tokensSb.append(value2 + " ");
                                } else if (Character.isDigit(tempSb.charAt(0))) {
                                    tokensSb.append("integer-value ");
                                } else {
                                    tokensSb.append("name ");
                                }
                                tempSb.setLength(0);
                            }
                            tokcounter++;
                            tokensSb.append(value + " ");
                        } else {
                            System.out.println("invalid character detected: (" + Character.toString(line.charAt(i))
                                    + "), Line: " + linecounter);
                            input.close();
                            return null;
                        }
                    }
                }
                linecounter++;
                linesIndicators.addLast(tokcounter + 1);
                if (tempSb.length() != 0) {
                    tokcounter++;
                    String value2 = keywords.getOrDefault(tempSb.toString(), null);
                    if (value2 != null) {
                        tokensSb.append(value2 + " ");
                    } else if (Character.isDigit(tempSb.charAt(0))) {
                        tokensSb.append("integer-value ");
                    } else {
                        tokensSb.append("name ");
                    }
                    tempSb.setLength(0);
                }
            }
            input.close();
            if (tokcounter > -1) {
                return (tokensSb.substring(0, tokensSb.length() - 1)).split(" ");
            } else {
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
