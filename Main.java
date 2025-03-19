import java.io.*;
import java.util.*;

class MyLangTokenizer {
    private static final String FILE_PATH = "C:\\Users\\odrps\\New folder\\testing\\src\\program.mlang";  

    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList("var", "print", "if", "else", "while"));
    private static final Set<String> BOOLEAN_VALUES = new HashSet<>(Arrays.asList("true", "false"));
    private static final Set<Character> OPERATORS = new HashSet<>(Arrays.asList('+', '-', '*', '/', '%', '^', '=', '<', '>', '!', '&', '|'));
    private static final Set<Character> PUNCTUATION = new HashSet<>(Arrays.asList('(', ')', '{', '}', ';', ','));

    public static void main(String[] args) {
        try {
            StringBuilder sourceCode = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                sourceCode.append(line).append("\n");
            }
            reader.close();

            List<String> tokens = tokenize(sourceCode.toString());

            for (String token : tokens) {
                System.out.println("TOKEN: " + token + " (Type: " + getTokenType(token) + ")");
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static List<String> tokenize(String code) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean insideString = false;
        boolean insideComment = false;
        boolean insideMultiLineComment = false;

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);

            if (insideMultiLineComment) {
                if (c == '*' && i + 1 < code.length() && code.charAt(i + 1) == '/') {
                    insideMultiLineComment = false;
                    i++; 
                }
                continue;
            } else if (c == '/' && i + 1 < code.length() && code.charAt(i + 1) == '*') {
                insideMultiLineComment = true;
                i++; 
                continue;
            }

            if (insideComment) {
                if (c == '\n') insideComment = false;
                continue;
            } else if (c == '/' && i + 1 < code.length() && code.charAt(i + 1) == '/') {
                insideComment = true;
                continue;
            }

            if (c == '"') {
                insideString = !insideString;
                currentToken.append(c);
                if (!insideString) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                continue;
            }
            if (insideString) {
                currentToken.append(c);
                continue;
            }

            if (Character.isWhitespace(c)) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                continue;
            }

            if (OPERATORS.contains(c) || PUNCTUATION.contains(c)) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                }
                tokens.add(String.valueOf(c));
                continue;
            }

            currentToken.append(c);
        }

        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }

        return tokens;
    }

    private static String getTokenType(String token) {
        if (KEYWORDS.contains(token)) return "Keyword";
        if (BOOLEAN_VALUES.contains(token)) return "Boolean";
        if (token.matches("[0-9]+")) return "Integer";
        if (token.matches("[0-9]+\\.[0-9]{1,5}")) return "Decimal";
        if (token.matches("[a-z][a-z0-9]*")) return "Identifier";
        if (token.length() == 3 && token.startsWith("'") && token.endsWith("'")) return "Character";
        if (OPERATORS.contains(token.charAt(0))) return "Operator";
        if (PUNCTUATION.contains(token.charAt(0))) return "Punctuation";
        if (token.startsWith("//")) return "Single-line Comment";
        if (token.startsWith("/*") && token.endsWith("*/")) return "Multi-line Comment";
        return "Unknown";
    }
}
