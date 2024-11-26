package axl.adaptive.axolotl.syntax.states;

import axl.utils.lexical.Token;
import axl.adaptive.axolotl.lexical.TokenType;
import axl.utils.lexical.TokenStream;
import axl.utils.syntax.IllegalSyntaxException;
import axl.utils.syntax.SyntaxAnalyzer;

import java.util.ArrayList;
import java.util.List;

public class StateUtils {

    public static List<Token> getLocation(SyntaxAnalyzer<?> analyzer) {
        TokenStream stream = analyzer.getStream();
        List<Token> result = new ArrayList<>();

        if (!stream.hasNext())
            throw new IllegalSyntaxException("Unknown token", stream);

        Token token = stream.next();
        if (token.getType() != TokenType.IDENTIFY)
            throw new IllegalSyntaxException("Unknown token", stream.back());

        result.add(token);
        while (stream.hasNext() && stream.get().getType() == TokenType.DOT) {
            stream.next();

            if (!stream.hasNext())
                throw new IllegalSyntaxException("Unknown token", stream);

            token = stream.get();
            if (token.getType() == TokenType.MULTIPLY) {
                stream.decrement();
                return result;
            }

            if (token.getType() == TokenType.IDENTIFY) {
                stream.next();
                result.add(token);
                continue;
            }

            throw new IllegalSyntaxException("Unknown token", stream.back());
        }
        return result;
    }
}
