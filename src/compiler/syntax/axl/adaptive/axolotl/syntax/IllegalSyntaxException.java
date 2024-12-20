package axl.adaptive.axolotl.syntax;

import axl.utils.File;
import axl.adaptive.axolotl.lexical.Token;
import axl.adaptive.axolotl.lexical.TokenStream;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class IllegalSyntaxException extends RuntimeException {

    private final @NonNull String message;

    public IllegalSyntaxException(String message, TokenStream tokenStream) {
        super(message);
        if (!tokenStream.hasNext())
            tokenStream.decrement();
        this.message = formatErrorMessage(tokenStream.getFile(), tokenStream.get(), message);
    }

    public IllegalSyntaxException(String message, File file, Token token) {
        super(message);
        this.message = formatErrorMessage(file, token, message);
    }

    private final static String format = """
                Syntax error in row %d and column %d. %s
                
                %s ╭╶───╴ %s:%d:%d ╶───╴
                %d │ %s
                %s │ %s╰ %s
                """;

    private static String formatErrorMessage(File file, Token token, String message) {
        int leftWhitespaces = getDigits(token.getLine());
        String whitespaces = " ".repeat(leftWhitespaces);
        String[] contentLines = file.getContent().split("\n", token.getLine() + 1);
        String errorLine = contentLines[token.getLine() - 1];

        return format.formatted(
                token.getLine(), token.getColumn(), message,
                whitespaces, file.getFilename(), token.getLine(), token.getColumn(),
                token.getLine(), errorLine,
                whitespaces, " ".repeat(token.getColumn() - 1), message
        );
    }

    private static int getDigits(int n) {
        if (n < 10)
            return 1;

        return 1 + getDigits(n / 10);
    }
}
