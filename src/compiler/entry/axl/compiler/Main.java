package axl.compiler;

import axl.adaptive.axolotl.lexical.TokenStream;
import axl.adaptive.axolotl.syntax.IllegalSyntaxException;
import axl.adaptive.axolotl.syntax.impl.DefaultSyntaxAnalyzer;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;

public class Main {

    public static axl.utils.File file;

    @SneakyThrows
    public static void main(String[] args) {
        try {
            File fileAXL = new java.io.File(args[0]);
            String filename = fileAXL.getName();
            String content = Files.readString(fileAXL.toPath());
            file = new axl.utils.File(filename, content);

            TokenStream stream = file.createTokenStream();
            String ast = new DefaultSyntaxAnalyzer(stream).analyze().toString();
            System.out.println(formatString(ast));
        } catch (IllegalSyntaxException e) {
            System.err.println(e.getMessage());
        }
    }

    public static String formatString(Object obj) {
        if (obj == null)
            return "null";

        String input = obj.toString();
        input = input.replace(" ", "");
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;

        for (char c : input.toCharArray()) {
            if (c == '{' || c == '[') {
                if (c == '{')
                    formatted.append(" ");

                formatted.append(c).append("\n");
                formatted.append(" ".repeat(++indentLevel * 4));
            } else if (c == '}' || c == ']') {
                formatted.append("\n").append(" ".repeat(--indentLevel * 4));
                formatted.append(c);
            } else if (c == ',') {
                formatted.append(c).append('\n').append(" ".repeat(indentLevel * 4));
            } else {
                formatted.append(c);
            }
        }

        return formatted.toString();
    }

}